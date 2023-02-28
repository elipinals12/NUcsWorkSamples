/*
Eli Pinals
CS3650
Project 1 - shell.c
*/
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>

#include "tokenizer.h"

#define MAX_LINE_LENGTH 255

// execute a command given arg array
void exec_command_tokens(char **tokens, int num_tokens);

// handle the redirects: <, >, |
void handle_in_redirect(char **tokens, int redirect_index) {
    pid_t pid;
    int status;

    // fork child process
    pid = fork();
    if (pid < 0) {
        // error occurred
        printf("Fork failed.\n");
        return;
    } else if (pid == 0) {
        // child process

        // get file descriptor of file to redirect input from
        int fd_in = open(tokens[redirect_index + 1], O_RDONLY);
        if (fd_in < 0) {
            printf("Failed to open file: %s\n", tokens[redirect_index + 1]);
            exit(0);
        }

        // replace file descriptor for stdin with fd_in
        if (dup2(fd_in, STDIN_FILENO) < 0) {
            printf("Failed to redirect input.\n");
            exit(0);
        }

        // close fd_in
        if (close(fd_in) < 0) {
            printf("Failed to close input file.\n");
            exit(0);
        }

        // remove the input redirection symbol and filename from tokens
        tokens[redirect_index] = NULL;
        tokens[redirect_index + 1] = NULL;

        // execute the command before the redirect
        exec_command_tokens(tokens, redirect_index);

        // exit child process
        exit(0);
    } else {
        // parent process, wait for child to finish
        waitpid(pid, &status, 0);
    }
}

void handle_out_redirect(char **tokens, int redirect_index) {
    pid_t pid;
    int status;

    // fork child process
    pid = fork();
    if (pid < 0) {
        // error occurred
        printf("Fork failed.\n");
        exit(1);
    }
    else if (pid == 0) {
        // child process

        // get file descriptor of file to redirect output to
        int fd_out = open(tokens[redirect_index + 1], O_WRONLY | O_CREAT | O_TRUNC, 0644);
        if (fd_out == -1) {
            // error occurred
            printf("Error opening file.\n");
            exit(1);
        }

        // replace file descriptor for stdout with fd_out
        if (dup2(fd_out, STDOUT_FILENO) == -1) {
            // error occurred
            printf("Error redirecting output.\n");
            exit(1);
        }

        // close the file descriptor for fd_out
        if (close(fd_out) == -1) {
            // error occurred
            printf("Error closing file.\n");
            exit(1);
        }

        // remove the output redirection symbol and filename from tokens
        tokens[redirect_index] = NULL;
        tokens[redirect_index + 1] = NULL;

        // recursively execute the command before the redirect
        exec_command_tokens(tokens, redirect_index);

        // exit child process
        exit(0);
    }
    else {
        // parent process, wait for child to finish
        waitpid(pid, &status, 0);
    }
}

void handle_pipe(char **tokens, int redirect_index) {
    int pipefd[2];
    pid_t pid_a, pid_b;
    int status;

    if (pipe(pipefd) < 0) {
        perror("pipe");
        exit(1);
    }

    pid_a = fork();
    if (pid_a < 0) {
        perror("fork");
        exit(1);
    } else if (pid_a == 0) {
        // child A process
        pid_b = fork();
        if (pid_b < 0) {
            perror("fork");
            exit(1);
        } else if (pid_b == 0) {
            // child B process
            close(pipefd[0]); // close unused read end of the pipe
            dup2(pipefd[1], STDOUT_FILENO); // Hook pipe to stdout
            close(pipefd[1]); // close write end of the pipe

            // find the index of the pipe symbol in the tokens array
            int i = 0;
            while (tokens[i] != NULL) {
                if (strcmp(tokens[i], "|") == 0) {
                    tokens[i] = NULL;
                    break;
                }
                i++;
            }

            // execute the command before the pipe
            execvp(tokens[0], tokens);

            // if execvp returns, there was an error
            perror(tokens[0]);
            exit(1);
        } else {
            // parent process A
            close(pipefd[1]); // close unused write end of the pipe
            waitpid(pid_b, &status, 0); // wait for child B to finish

            // redirect input from the pipe to stdin
            dup2(pipefd[0], STDIN_FILENO);
            close(pipefd[0]); // Close read end of the pipe

            // remove the pipe symbol and everything before it from the tokens array
            int i = 0;
            while (tokens[i] != NULL) {
                if (strcmp(tokens[i], "|") == 0) {
                    tokens[i] = NULL;
                    break;
                }
                i++;
            }
            memmove(tokens, tokens + i + 1, sizeof(char *) * (redirect_index - i));

            // execute the command after the pipe
            execvp(tokens[0], tokens);

            // if execvp returns, there was an error
            perror(tokens[0]);
            exit(1);
        }
    } else {
        // parent process B
        waitpid(pid_a, &status, 0); // wait for child A to finish
    }
}

void exec_semi_colon(char *line)
{
  // remove newline character from line
  line[strcspn(line, "\n")] = '\0';

  char **tokens;
  int num_tokens = 0;
  char *token;
  int i;

  // tokenize input line
  tokens = tokenize(line);
  while (tokens[num_tokens] != NULL)
  {
    ++num_tokens;
  }

  int start = 0;
  for (i = 0; i < num_tokens; i++)
  {
    if (strcmp(tokens[i], ";") == 0)
    {
      tokens[i] = NULL;
      exec_command_tokens(tokens + start, i - start);
      start = i + 1;
    }
  }

  if (start < num_tokens)
  {
    exec_command_tokens(tokens + start, num_tokens - start);
  }
}

// function to print help page
void help()
{
  printf("Built-in commands:\n");
  printf("cd [path] - Change the current working directory to [path]\n");
  printf("source [filename] - Execute a script located at [filename]\n");
  printf("prev - Print the previous command line and execute it again\n");
  printf("help - Display this help message\n");
}

// var to store the previous command line
char prev_command[MAX_LINE_LENGTH] = "";

// function to execute a script
void execute_script(char *filename)
{
  char line[MAX_LINE_LENGTH];

  FILE *fp = fopen(filename, "r"); // file path, fopen works best, read mode
  if (fp == NULL)
  {
    printf("%s: file not found\n", filename);
    return;
  }

  while (fgets(line, MAX_LINE_LENGTH, fp) != NULL)
  {
    exec_semi_colon(line);
  }

  fclose(fp);
}

// execute a command given arg array and token count
void exec_command_tokens(char **tokens, int num_tokens)
{
  if (num_tokens == 0)
  {
    // empty line, skip to next iteration
    return;
  }

  // check for redirection and piping
  int input_index = 0;
  int output_index = 0;
  int pipe_index = 0;
  int i;
  for (i = 0; i < num_tokens; i++)
  {
    if (strcmp(tokens[i], "<") == 0)
    {
      input_index = i;
    }
    else if (strcmp(tokens[i], ">") == 0)
    {
      output_index = i;
    }
    else if (strcmp(tokens[i], "|") == 0)
    {
      pipe_index = i;
    }
  }

  if (input_index > 0)
  {
    handle_in_redirect(tokens, input_index);
    // printf("!!!input redir!!!\n%d\n", input_index);
  }

  if (output_index > 0)
  {
    // printf("!!!output redir!!!\n");
    handle_out_redirect(tokens, output_index);
  }

  if (pipe_index > 0)
  {
    // printf("!!!pipe redir!!!\n%d\n", pipe_index);
    handle_pipe(tokens, pipe_index);
  }

  if (strcmp(tokens[0], "prev") == 0)
  {
    if (prev_command[0] == '\0')
    {
      printf("No previous command.\n");
    }
    else
    {
      printf("%s\n", prev_command);
      system(prev_command);
    }
  }
  else
  {
    // update the prev command
    int i;
    strcpy(prev_command, "");
    for (i = 0; i < num_tokens; i++)
    {
      strcat(prev_command, tokens[i]);
      strcat(prev_command, " ");
    }
    // remove trailing whitespace
    prev_command[strlen(prev_command) - 1] = '\0';
  }

  if (strcmp(tokens[0], "exit") == 0) //  || fgets(line, MAX_LINE_LENGTH, stdin) == NULL
  {
    // exit
    printf("Bye bye.\n");
    exit(0);
  }

  if (strcmp(tokens[0], "cd") == 0)
  {
    // cd
    if (num_tokens < 2)
    {
      // no argument, print error message
      printf("cd: missing operand\n");
    }
    else if (chdir(tokens[1]) != 0)
    {
      // error changing directory
      printf("cd: error changing directory\n");
    }
    return;
  }

  if (strcmp(tokens[0], "source") == 0)
  {
    // source
    if (num_tokens < 2)
    {
      // no argument, print error message
      printf("source: missing file operand\n");
    }
    else
    {
      execute_script(tokens[1]);
    }
    return;
  }

  if (strcmp(tokens[0], "help") == 0)
  {
    help();
    return;
  }

  pid_t pid = fork();
  if (pid == 0)
  {
    // child process
    execvp(tokens[0], tokens);
    if (strcmp(tokens[0], "prev") != 0)
    {
      printf("%s: command not found\n", tokens[0]);
    }
    exit(1);
  }
  else if (pid > 0)
  {
    // parent process
    waitpid(pid, NULL, 0);
  }
  else
  {
    // error forking
    perror("fork");
    exit(1);
  }

  return;
}

// the main function
// ....................................................................
int main(int argc, char **argv)
{
  char line[MAX_LINE_LENGTH];

  printf("Welcome to mini-shell.\n");

  while (1)
  {
    printf("shell $ ");

    if (fgets(line, MAX_LINE_LENGTH, stdin) == NULL)
    {
      // error or EOF, exit
      break;
    }

    exec_semi_colon(line);
  }

  return 0;
}
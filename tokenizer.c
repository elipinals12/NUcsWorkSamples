/*
Eli Pinals
Tokenizer - Project 1
CS3650
*/

#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <stdlib.h>

#include "tokenizer.h"

// is char c a digit?
int is_digit(char c)
{
  return c >= '0' && c <= '9';
}

// is char c a string token character (letter, _, -, .)
int is_s_token(char c)
{
  return isalpha(c) || c == '_' || c == '-' || c == '.';
}

// returns the length of the string, copy in to out only if its a digit
int read_next_int(const char *input, char *output)
{
  int i = 0;
  // while input is not over && current char is a digit
  while (input[i] != '\0' && is_digit(input[i]))
  {
    output[i] = input[i]; // copy in to out
    ++i;
  }
  output[i] = '\0'; // add terminating byte at the end

  return i;
}

// tokenize function, returns array of tokens
char **tokenize(char *expr)
{
  char string_token[256] = {0}; // the string token being built
  char quote_token[256] = {0};  // the quote token being built
  int donewquote = 1;
  int token_count = 0;

  char **tokens = malloc(256 * sizeof(char *)); // allocate space for 256 tokens
  if (!tokens)
  {
    perror("malloc failed");
    exit(1);
  }

  int i = 0; // current position in string
  while (expr[i] != '\0')
  { // while the end of string is not reached

    if (expr[i] == '"')
    {
      donewquote = !donewquote;
      if (donewquote)
      {
        tokens[token_count] = malloc((strlen(quote_token) + 1) * sizeof(char));
        strcpy(tokens[token_count], quote_token);
        quote_token[0] = '\0';
        ++token_count;
        ++i;
        continue;
      }
      else
      {
        ++i;
        continue;
      }
    }

    if (donewquote)
    {
      // first check if the current char is a digit
      if (is_digit(expr[i]))
      {
        char buf[256] = {0}; // temp buffer
        int read = read_next_int(&expr[i], buf);
        tokens[token_count] = malloc((read + 1) * sizeof(char));
        strncpy(tokens[token_count], buf, read + 1);
        ++token_count;
        i += read;
        continue; // skip the rest of this iteration
      }

      // then check if it is a string token char
      if (is_s_token(expr[i]))
      {
        strncat(string_token, &expr[i], 1); // append the current char to string_token
        if (!is_s_token(expr[i + 1]))
        {
          tokens[token_count] = malloc((strlen(string_token) + 1) * sizeof(char));
          strcpy(tokens[token_count], string_token);
          string_token[0] = '\0'; // reset string_token
          ++token_count;
        }
        ++i; // this TINY line cost me multiple hours embarrassingly
        continue;
      }
    }
    else
    {
      strncat(quote_token, &expr[i], 1); // append, concat, same word
      ++i;
      continue;
    }

    // if not number or s_token char, consider the current character and print its type
    switch (expr[i])
    {
    case '(':
      tokens[token_count] = malloc(2 * sizeof(char)); // allocate 2 bytes for "(" + terminating byte
      strncpy(tokens[token_count], "(", 2);
      ++token_count;
      break;
    case ')':
      tokens[token_count] = malloc(2 * sizeof(char)); // allocate 2 bytes for ")" + terminating byte
      strncpy(tokens[token_count], ")", 2);
      ++token_count;
      break;
    case '<':
      tokens[token_count] = malloc(2 * sizeof(char)); // allocate 2 bytes for "<" + terminating byte
      strncpy(tokens[token_count], "<", 2);
      ++token_count;
      break;
    case '>':
      tokens[token_count] = malloc(2 * sizeof(char)); // allocate 2 bytes for ">" + terminating byte
      strncpy(tokens[token_count], ">", 2);
      ++token_count;
      break;
    case ';':
      tokens[token_count] = malloc(2 * sizeof(char)); // allocate 2 bytes for ";" + terminating byte
      strncpy(tokens[token_count], ";", 2);
      ++token_count;
      break;
    case '|':
      tokens[token_count] = malloc(2 * sizeof(char)); // allocate 2 bytes for "|" + terminating byte
      strncpy(tokens[token_count], "|", 2);
      ++token_count;
      break;
    case '"':
      // ignore the actual quotes
      break;
    case ' ':
      // do nothing for spaces
      break;
    default: // should really never be triggered
      printf("%c: command not found\n", expr[i]);
      break;
    }
    ++i;
  }
  return tokens;
}

// free the tokens from mem
void free_tokens(char **tokens)
{
  if (tokens)
  {
    for (int i = 0; tokens[i]; i++)
    {
      free(tokens[i]);
    }
    free(tokens);
  }
}
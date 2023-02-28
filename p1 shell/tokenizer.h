#ifndef TOKENIZER_H
#define TOKENIZER_H

char **tokenize(char *input);
int is_digit(char c);
int is_s_token(char c);
int read_next_int(const char *input, char *output);
void free_tokens(char **tokens);

#endif
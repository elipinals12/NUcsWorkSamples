/*
Eli Pinals
Tokenize DRIVER - Project 1
CS3650
*/

#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <stdlib.h>

#include "tokenizer.h"

// main function, just print the elements of the array
int main(int argc, char **argv)
{
  char expr[256];
  fgets(expr, sizeof(expr), stdin);
  expr[strlen(expr) - 1] = '\0';
  char buf[256]; // temp buffer

  char **tokens = tokenize(expr);

  for (int i = 0; tokens[i]; ++i)
  {
    printf("%s\n", tokens[i]);
  }

  free_tokens(tokens);
  return 0;
}
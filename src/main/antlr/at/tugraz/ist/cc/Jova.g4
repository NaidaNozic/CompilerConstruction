grammar Jova;

STRING : '"' (~[\n\r\\"] | ESC_SEQ)* '"';
fragment ESC_SEQ : '\\' [nrtbf"'\\];
COMMENT : '//' ~[\n\r]* -> skip;
WS : [ \n\t\r] -> skip;

program : EOF;
  // TODO: implement Task 1.1 here...
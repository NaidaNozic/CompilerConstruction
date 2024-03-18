grammar Jova;

STRING : '"' (~[\n\r\\"] | ESC_SEQ)* '"';
fragment ESC_SEQ : '\\' [nrtbf"'\\];
COMMENT : '//' ~[\n\r]* -> skip;
WS : [ \n\t\r] -> skip;

//Keywords
KEY_IF : 'if' ;
KEY_ELSE : 'else' ;
KEY_WHILE : 'while' ;
KEY_RETURN : 'return' ;
KEY_INT : 'int' ;
KEY_BOOL : 'bool' ;
KEY_STRING : 'string' ;
KEY_NIX : 'nix' ;
KEY_THIS : 'this' ;
KEY_NEW : 'new' ;

//Operators
ADDOP : '+' | '-' ;
ASSIGN : '=' ;
RELOP : '<' | '>' | '==' | '!=' ;
MULOP : '*' | '/' | '%' ;
OR : '||' ;
AND : '&&' ;
NOT : '!' ;
DOT : '.';

//Numerical literals
INT : DIGIT0 | DIGIT (DIGIT0)* ;
fragment DIGIT0 : [0-9] ;
fragment DIGIT : [1-9] ;

//Boolean literals
BOOL : 'true' | 'false' ;

//Identifiers
CLASS_ID : UPPERCASE (LETTER | DIGIT0 | '_' )* ;
ID : LOWERCASE (LETTER | DIGIT0 | '_' )* ;
fragment LETTER : (LOWERCASE | UPPERCASE) ;
fragment LOWERCASE : [a-z] ;
fragment UPPERCASE : [A-Z] ;

 // parser rules
 program : class_decl+;// EOF;
 class_decl : CLASS_ID (':' CLASS_ID )? '{' class_body '}' ;
 class_body : (decl | method)* ;
 method : param '(' param_list? ')' block ;
 type : KEY_INT | KEY_BOOL | KEY_STRING | CLASS_ID ;
 param : type ID;
 param_list : param (',' param)* ;
 block : '{' (decl | if_stmt | while_stmt | return_stmt | expr ';')* '}' ;
 decl : (param (',' ID)* | expr) ';' ;
 if_stmt : KEY_IF '(' expr ')' block (KEY_ELSE block)? ;
 while_stmt : KEY_WHILE '(' expr ')' block ;
 return_stmt : KEY_RETURN expr ';' ;

 expr : literal
        | id_expr
        | KEY_NEW CLASS_ID
        | '(' expr ')'
        | expr DOT expr
        | (ADDOP | NOT) expr
        | expr MULOP expr
        | expr ADDOP expr
        | expr RELOP expr
        | expr AND expr
        | expr OR expr
        | expr ASSIGN expr;

 id_expr : ID ( '(' ( (expr ',')* expr )? ')')? ;
 literal : INT | BOOL | STRING | KEY_NIX | KEY_THIS ;

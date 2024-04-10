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
 decl : (param (',' ID)* ) ';' ;
 if_stmt : KEY_IF '(' expr ')' block (KEY_ELSE block)? ;
 while_stmt : KEY_WHILE '(' expr ')' block ;
 return_stmt : KEY_RETURN expr ';' ;

 expr : literal                #LiteralExpression
        | id_expr              #IdExpression
        | KEY_NEW CLASS_ID     #NewClassExpression
        | '(' expr ')'         #ParanthesisExpression
        | expr DOT expr        #DotOperator
        | (ADDOP | NOT) expr   #AddNotExpression
        | expr MULOP expr      #MultiplicationOperator
        | expr ADDOP expr      #AddOperator
        | expr RELOP expr      #RelopOperator
        | expr AND expr        #AndOperator
        | expr OR expr         #OrOperator
        | expr ASSIGN expr     #AssignOperator
        ;

 id_expr : ID ( '(' ( (expr ',')* expr )? ')')? ;
 literal : INT | BOOL | STRING | KEY_NIX | KEY_THIS ;

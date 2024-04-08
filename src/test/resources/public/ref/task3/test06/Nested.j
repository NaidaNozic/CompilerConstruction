.source test06.jova
.class public Nested
.super java/lang/Object

.method public <init>()V
        aload_0
        invokespecial java/lang/Object/<init>()V
        return
.end method

.method public fortyTwo()I
.limit stack 1
.limit locals 1
        bipush 42
        ireturn 
.end method

.method public nestedIsland()I
.limit stack 4
.limit locals 3
        iconst_4 
        bipush 8
        bipush 15
        imul 
        bipush 16
        bipush 23
        isub 
        imul 
        aload_0 
        invokevirtual Nested/fortyTwo()I
        idiv 
        iadd 
        istore 1
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "The result of the nested expression is "
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        iconst_0 
        pop 
        getstatic java/lang/System/out Ljava/io/PrintStream;
        iload 1
        invokevirtual java/io/PrintStream/print(I)V
        iconst_0 
        istore 2
        iload 2
        ireturn 
.end method


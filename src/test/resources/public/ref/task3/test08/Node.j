.source test08.jova
.class public Node
.super java/lang/Object

.method public <init>()V
        aload_0
        invokespecial java/lang/Object/<init>()V
        return
.end method

.method public print()I
.limit stack 2
.limit locals 1
        getstatic java/lang/System/out Ljava/io/PrintStream;
        iconst_4 
        invokevirtual java/io/PrintStream/print(I)V
        iconst_0 
        pop 
        iconst_0 
        ireturn 
.end method


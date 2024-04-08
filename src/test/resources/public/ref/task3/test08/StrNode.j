.source test08.jova
.class public StrNode
.super Node

.method public <init>()V
        aload_0
        invokespecial Node/<init>()V
        return
.end method

.method public print()I
.limit stack 2
.limit locals 1
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "2"
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        iconst_0 
        pop 
        iconst_0 
        ireturn 
.end method


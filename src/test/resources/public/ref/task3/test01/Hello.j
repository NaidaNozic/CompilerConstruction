.source test01.jova
.class public Hello
.super java/lang/Object

.method public <init>()V
        aload_0
        invokespecial java/lang/Object/<init>()V
        return
.end method

.method public static main([Ljava/lang/String;)V
.limit stack 2
.limit locals 1
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "Hello World!\n"
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        iconst_0 
        pop
        return 
.end method


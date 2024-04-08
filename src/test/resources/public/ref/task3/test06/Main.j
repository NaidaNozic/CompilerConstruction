.source test06.jova
.class public Main
.super java/lang/Object

.method public <init>()V
        aload_0
        invokespecial java/lang/Object/<init>()V
        return
.end method

.method public static main([Ljava/lang/String;)V
.limit stack 2
.limit locals 2
        new Nested
        dup 
        invokespecial Nested/<init>()V
        astore 1
        aload 1
        invokevirtual Nested/nestedIsland()I
        pop
        return 
.end method


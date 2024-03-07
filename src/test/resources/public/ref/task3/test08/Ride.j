.source test08.jova
.class public Ride
.super java/lang/Object

.method public <init>()V
        aload_0
        invokespecial java/lang/Object/<init>()V
        return
.end method

.method public static main([Ljava/lang/String;)V
.limit stack 2
.limit locals 3
        new Node
        dup 
        invokespecial Node/<init>()V
        astore 1
        new StrNode
        dup 
        invokespecial StrNode/<init>()V
        astore 2
        aload 1
        invokevirtual Node/print()I
        pop 
        aload 2
        invokevirtual StrNode/print()I
        pop 
        aload 2
        astore 1
        aload 1
        invokevirtual Node/print()I
        pop
        return 
.end method


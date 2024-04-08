.source test07.jova
.class public HiddenStrNode
.super Node

.field public value Ljava/lang/String;

.method public <init>()V
        aload_0
        invokespecial Node/<init>()V
        return
.end method

.method public setup()Ljava/lang/String;
.limit stack 2
.limit locals 1
        aload_0 
        ldc "world"
        putfield HiddenStrNode/value Ljava/lang/String;
        ldc "hello"
        areturn 
.end method


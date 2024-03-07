.source test07.jova
.class public Node
.super java/lang/Object

.field public value I

.method public <init>()V
        aload_0
        invokespecial java/lang/Object/<init>()V
        return
.end method

.method public setup()I
.limit stack 2
.limit locals 1
        aload_0 
        bipush 42
        putfield Node/value I
        iconst_3 
        ireturn 
.end method


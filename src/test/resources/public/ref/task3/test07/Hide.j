.source test07.jova
.class public Hide
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
        new HiddenStrNode
        dup 
        invokespecial HiddenStrNode/<init>()V
        astore 2
        getstatic java/lang/System/out Ljava/io/PrintStream;
        aload 1
        invokevirtual Node/setup()I
        invokevirtual java/io/PrintStream/print(I)V
        iconst_0 
        pop 
        getstatic java/lang/System/out Ljava/io/PrintStream;
        aload 1
        getfield Node/value I
        invokevirtual java/io/PrintStream/print(I)V
        iconst_0 
        pop 
        getstatic java/lang/System/out Ljava/io/PrintStream;
        aload 2
        invokevirtual HiddenStrNode/setup()Ljava/lang/String;
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        iconst_0 
        pop 
        getstatic java/lang/System/out Ljava/io/PrintStream;
        aload 2
        getfield HiddenStrNode/value Ljava/lang/String;
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        iconst_0 
        pop 
        aload 2
        astore 1
        getstatic java/lang/System/out Ljava/io/PrintStream;
        aload 1
        invokevirtual Node/setup()I
        invokevirtual java/io/PrintStream/print(I)V
        iconst_0 
        pop 
        getstatic java/lang/System/out Ljava/io/PrintStream;
        aload 1
        getfield Node/value I
        invokevirtual java/io/PrintStream/print(I)V
        iconst_0 
        pop
        return 
.end method


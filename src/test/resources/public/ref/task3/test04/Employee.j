.source test04.jova
.class public Employee
.super Person

.field public office Ljava/lang/String;

.method public <init>()V
        aload_0
        invokespecial Person/<init>()V
        return
.end method

.method public printInfo()I
.limit stack 2
.limit locals 1
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "Employee:\n"
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        iconst_0 
        pop 
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "Name: "
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        getstatic java/lang/System/out Ljava/io/PrintStream;
        aload_0 
        getfield Employee/name Ljava/lang/String;
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "\n"
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "Office: "
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        getstatic java/lang/System/out Ljava/io/PrintStream;
        aload_0 
        getfield Employee/office Ljava/lang/String;
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "\n"
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        iconst_0 
        ireturn 
.end method


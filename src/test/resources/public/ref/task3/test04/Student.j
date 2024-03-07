.source test04.jova
.class public Student
.super Person

.field public matr_num I

.method public <init>()V
        aload_0
        invokespecial Person/<init>()V
        return
.end method

.method public printInfo()I
.limit stack 2
.limit locals 1
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "Student:\n"
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        iconst_0 
        pop 
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "Name: "
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        getstatic java/lang/System/out Ljava/io/PrintStream;
        aload_0 
        getfield Student/name Ljava/lang/String;
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "\n"
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "Matr.Num.: "
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        getstatic java/lang/System/out Ljava/io/PrintStream;
        aload_0 
        getfield Student/matr_num I
        invokevirtual java/io/PrintStream/print(I)V
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "\n"
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        iconst_0 
        ireturn 
.end method


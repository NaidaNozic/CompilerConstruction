.source test04.jova
.class public Test
.super java/lang/Object

.method public <init>()V
        aload_0
        invokespecial java/lang/Object/<init>()V
        return
.end method

.method public static main([Ljava/lang/String;)V
.limit stack 2
.limit locals 3
        new Student
        dup 
        invokespecial Student/<init>()V
        astore 1
        aload 1
        ldc "SpongeBob"
        putfield Student/name Ljava/lang/String;
        aload 1
        ldc 50191
        putfield Student/matr_num I
        new Employee
        dup 
        invokespecial Employee/<init>()V
        astore 2
        aload 2
        ldc "Squidward"
        putfield Employee/name Ljava/lang/String;
        aload 2
        ldc "The Krusty Krab"
        putfield Employee/office Ljava/lang/String;
        aload 1
        invokevirtual Student/printInfo()I
        pop 
        aload 2
        invokevirtual Employee/printInfo()I
        pop
        return 
.end method


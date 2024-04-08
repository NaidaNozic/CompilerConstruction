.source test02.jova
.class public MMan
.super java/lang/Object

.method public <init>()V
        aload_0
        invokespecial java/lang/Object/<init>()V
        return
.end method

.method public static main([Ljava/lang/String;)V
.limit stack 2
.limit locals 3
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "Set it to W for WUMBO!\n"
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        iconst_0 
        pop 
        iconst_1 
        istore 2
        iload 2
        ifeq ELSE0
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "MAGNIFY!\n"
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        iconst_0 
        pop 
        iconst_4 
        istore 1
        goto ENDIF0
ELSE0:
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "EEEVILLL!!!\n"
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        iconst_0 
        pop 
        iconst_2 
        istore 1
ENDIF0:
        return 
.end method


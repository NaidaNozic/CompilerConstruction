.source test03.jova
.class public Squid
.super java/lang/Object

.method public <init>()V
        aload_0
        invokespecial java/lang/Object/<init>()V
        return
.end method

.method public static main([Ljava/lang/String;)V
.limit stack 2
.limit locals 2
        bipush 10
        istore 1
WHILE0:
        iload 1
        iconst_0 
        if_icmpgt GREATER0
        iconst_0 
        goto LESSER0
GREATER0:
        iconst_1 
LESSER0:
        ifeq ENDWHILE0
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "Have you finished those errands?\n"
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        iconst_0 
        pop 
        iload 1
        iconst_1 
        isub 
        istore 1
        goto WHILE0
ENDWHILE0:
        return 
.end method


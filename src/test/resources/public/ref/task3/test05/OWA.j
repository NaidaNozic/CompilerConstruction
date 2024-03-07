.source test05.jova
.class public OWA
.super java/lang/Object

.method public <init>()V
        aload_0
        invokespecial java/lang/Object/<init>()V
        return
.end method

.method public static main([Ljava/lang/String;)V
.limit stack 3
.limit locals 4
        new java/util/Scanner
        dup
        getstatic java/lang/System/in Ljava/io/InputStream;
        invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
        astore 3
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "Welcome to the One-Way Adventure! What is your name? "
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        iconst_0 
        pop
        aload 3
        invokevirtual java/util/Scanner/nextLine()Ljava/lang/String;
        astore 2
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "Hello "
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        getstatic java/lang/System/out Ljava/io/PrintStream;
        aload 2
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc ". Which way do you want to go? (1-3) "
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        aload 3
        invokevirtual java/util/Scanner/nextInt()I
        istore 1
        iload 1
        iconst_1 
        if_icmpeq EQUAL0
        iconst_0 
        goto NOTEQUAL0
EQUAL0:
        iconst_1 
NOTEQUAL0:
        ifne ISTRUE0
        iload 1
        iconst_2 
        if_icmpeq EQUAL1
        iconst_0 
        goto NOTEQUAL1
EQUAL1:
        iconst_1 
NOTEQUAL1:
        ifne ISTRUE0
        iconst_0 
        goto ISFALSE0
ISTRUE0:
        iconst_1 
ISFALSE0:
        ifeq ELSE0
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "You fall into a hole.\n"
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        getstatic java/lang/System/out Ljava/io/PrintStream;
        aload 2
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc " dies.\nGame Over!\n"
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        goto ENDIF0
ELSE0:
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc "You defeat a goblin and rescue the princess. The princess turns into a wolf and eats you. Oh snap!\n"
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        iconst_0 
        pop 
        getstatic java/lang/System/out Ljava/io/PrintStream;
        aload 2
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
        getstatic java/lang/System/out Ljava/io/PrintStream;
        ldc " dies.\nGame Over!\n"
        invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
ENDIF0:
        return 
.end method


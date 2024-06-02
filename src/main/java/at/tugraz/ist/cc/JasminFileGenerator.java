package at.tugraz.ist.cc;

import at.tugraz.ist.cc.codegenvisitors.CodeGenStorage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JasminFileGenerator {

    private static String fileContent = "";


    public static void writeContent(String content) {
        fileContent += (content + "\n");
    }


    public static void makeFile() {
        try {
            FileWriter file = new FileWriter(new File("src/output", CodeGenStorage.getClassID() + ".j"), false);
            file.write(fileContent);
            file.close();
        } catch (Exception e) {
            System.out.println("File not written");
        }


    }


    public static void reset() {
        fileContent = "";
    }

}

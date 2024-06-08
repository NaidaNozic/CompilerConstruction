package at.tugraz.ist.cc;

import at.tugraz.ist.cc.codegenvisitors.CodeGenStorage;

import java.io.File;
import java.io.FileWriter;

public class JasminFileGenerator {

    private static String fileContent = "";


    public static void writeContent(String content) {
        fileContent += (content + "\n");
    }

    public static void makeFile(String outPath) {
        try {
            File outputFile = new File(outPath, CodeGenStorage.getClassID() + ".j");
            FileWriter fileWriter = new FileWriter(outputFile, false);
            fileWriter.write(fileContent);
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("File not written");
        }
    }

    public static void reset() {
        fileContent = "";
    }

}

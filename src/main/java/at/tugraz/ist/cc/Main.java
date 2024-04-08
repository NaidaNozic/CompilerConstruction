package at.tugraz.ist.cc;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author cc24
 *
 * The jovac Main class
 */
public class Main {

    public static void main(String[] args) {
        if (args.length == 0 || args[0].equals("-help")) {
            printHelp();
        } else if (args.length == 1) {
            var jovac = new Jovac();
            var out_dir = "out";
            jovac.task3(args[0], out_dir);
            if (jovac.getLexicalErrors().isEmpty() && jovac.getSyntaxErrors().isEmpty()
                    && jovac.getSemanticErrors().isEmpty())
                executeJasmin(out_dir);
            else
                System.exit(-1);
        } else if (args.length < 4) {
            switch (args[0]) {
                case "task1" -> {
                    new Jovac().task1(args[1]);
                }
                case "task2" -> {
                    new Jovac().task2(args[1]);
                }
                case "task3" -> {
                    new Jovac().task3(args[2], args[1]);
                }
                default -> System.out.println("Unknown task name: " + args[0]);
            }
        } else {
            printHelp();
            System.exit(0);
        }
    }

    private static void printHelp() {
        System.out.println("USAGE: jovac [<task>] [<outpath>] <file>");
        System.out.println("<task>: task1|task2|task3");
        System.out.println("<output_path>: path to output folder (for task3 only)]");
        System.out.println("<file>: path to the input file (*.jova)");
    }

    private static void executeJasmin(String out_dir) {
        System.out.println("Running Jasmin...");
        try {
            var command = new java.util.ArrayList<>(List.of("java", "-jar", "jasmin.jar", "-d", out_dir));
            command.addAll(Arrays.stream(Objects.requireNonNull(new File(out_dir).listFiles())).filter(f -> f.isFile()
                    && f.getName().endsWith(".j")).map(f -> out_dir + File.separator + f.getName()).toList());
            var bob = new ProcessBuilder(command).redirectErrorStream(true);
            System.out.println(new String(bob.start().getInputStream().readAllBytes())) ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

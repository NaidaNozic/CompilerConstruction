package at.tugraz.ist.cc;


import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Task3PublicTest {
    public static final String path_cg = Path.of("src", "test", "resources", "public", "input", "task3") + File.separator;
    public static final String outdir = "out" + File.separator;

    private static void executeJasmin(String out_dir) {
        System.out.println("Running Jasmin...");
        try {
            var command = new java.util.ArrayList<>(List.of("java", "-jar", "libs" + File.separator + "jasmin.jar", "-d", out_dir));
            command.addAll(Arrays.stream(Objects.requireNonNull(new File(out_dir).listFiles())).filter(f -> f.isFile()
                    && f.getName().endsWith(".j")).map(f -> out_dir + File.separator + f.getName()).toList());
            var bob = new ProcessBuilder(command).redirectErrorStream(true);
            System.out.println(new String(bob.start().getInputStream().readAllBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static Stream<Arguments> codeGenTests() {
        String path = path_cg;
        return Stream.of(
                Arguments.of(path + "test01.jova"),
                Arguments.of(path + "test02.jova"),
                Arguments.of(path + "test03.jova"),
                Arguments.of(path + "test04.jova"),
                Arguments.of(path + "test05.jova"),
                Arguments.of(path + "test06.jova"),
                Arguments.of(path + "test07.jova"),
                Arguments.of(path + "test08.jova")
        );
    }


    @ParameterizedTest
    @MethodSource("codeGenTests")
    @Timeout(2)
    public void testCodeGen(String file_path) {
        String filename = Path.of(file_path).getFileName().toString();
        System.out.println("Running test " + filename);

        Jovac jovac = new Jovac();
        int lastDotIndex = filename.lastIndexOf('.');
        String targetDir = lastDotIndex == -1 ? filename : filename.substring(0, lastDotIndex);

        jovac.task3(file_path, outdir+targetDir);

        assertTrue(jovac.getLexicalErrors().isEmpty());
        assertTrue(jovac.getSyntaxErrors().isEmpty());
        assertTrue(jovac.getSemanticErrors().isEmpty());

        executeJasmin(outdir+targetDir);
    }
}
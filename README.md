Framework tested with OpenJDK21 + gradle 8.5<br>

*) Use 'gradlew compileJava' or 'gradlew compileTestJava' to compile your project<br>
*) Use 'gradlew test' to run JUnit tests<br>
*) Use 'gradlew clean' to delete contents of 'build' directory<br>

========================================================================================================================
<h4>Running the project</h4>

The following commands are available for use: <br>
```
 .\gradlew clean 
 .\gradlew compileJava
 .\gradlew fatJar
 .\gradlew compileTestJava
 .\gradlew test
 ```

Running task1:<br>
```
 .\gradlew clean 
 .\gradlew compileJava
 .\gradlew fatJar
 java -jar "...\jovac.jar" task1 example.jova
```

Running task2:<br>
```
 .\gradlew clean 
 .\gradlew compileJava
 .\gradlew fatJar
 java -jar "...\jovac.jar" task1 example.jova
```

Running task3:<br>
```
.\gradlew clean 
 .\gradlew compileJava
 .\gradlew fatJar
 java -jar "...\jovac.jar" task3 "...\output" "...\example.jova"
 java -jar ...\jasmin.jar -d "...\output" "...\output\Class.j"
```

Running the .class<br>
```
java Class
```
or if the Class is located in some folder:<br>
```
java -cp "...\output" Class
```

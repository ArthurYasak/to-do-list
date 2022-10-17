package main.java.com.company;

import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        // maybe create second file
        System.out.println("Hello!");
        System.out.println("Type your business:");

        Scanner sc = new Scanner(System.in);
        Path path = Paths.get("path.txt");
        String str = sc.nextLine() + "\n";
        byte[] boolStr = str.getBytes();
        Files.write(path, boolStr, StandardOpenOption.APPEND);

        // (LAST VERSION!) main part of program
        /*
        File output = new File("output.txt");
        File copyOutput = new File("copyOutput.txt");
        File newOutput = new File("newOutput.txt");
        boolean create = newOutput.createNewFile();
        if (create) {
            System.out.println("File newOutput created");
        } else {
            System.out.println("File newOutput NOT created");
        }
        boolean delete = newOutput.delete();
        if (delete) {
            System.out.println("File newOutput deleted");
        } else {
            System.out.println("File newOutput NOT deleted");
        }

         */

        // тестовый файл   (CREATED, RENAME, DELETED)
        /*
        System.out.println();
        File fileTest = new File("fileTest.txt");
        boolean created = fileTest.createNewFile();
        if(created) {
            System.out.println("File has been created");
        } else {
            System.out.println("File has NOT been created");
        }
        FileWriter testWriter;
        PrintWriter testPrintWriter;
        // testWriter = new FileWriter(fileTest);
        // testPrintWriter = new PrintWriter(testWriter);
        System.out.println(str);
        // testPrintWriter.println("str");
        // переименуем тестовый файл

        File newFileTest = new File("newFileTest.txt");
        boolean renamed = fileTest.renameTo(newFileTest);
        if (renamed) {
            System.out.println("File has been renamed");
        } else {
            System.out.println("File has NOT been renamed");
        }
        // удалим тестовый файл
        boolean deleted = newFileTest.delete();
        if(deleted)
            System.out.println("File has been deleted");
        else
            System.out.println("File has NOT been deleted");
        System.out.println();

         */


        // work with Path
        /*
        System.out.println("Work with PATH");
        Path path = Paths.get("path.txt");
        Path newPath = Paths.get("newPath.txt");
        // path.resolveSibling(newPath);
        try {
            Files.createFile(path);
            // Files.createFile(newPath);
            Files.copy(path, newPath);
            System.out.println("Files PATH has been created");
        }
        catch (IOException e) {
            System.out.println("Operation PATH FAILED");
            // Files.delete(path);
            // Files.delete(newPath);
        }
        byte[] byteStr = str.getBytes();
        Files.write(path, byteStr, StandardOpenOption.APPEND);
        System.out.println();



        Path oldFile = Paths.get("oldFile.txt");
        Path newFile = Paths.get("newFile.txt");
        try {
            Files.createFile(oldFile);
            Files.move(oldFile, Paths.get("newFile.txt"));
            System.out.println("oldFile successfully renamed to newFile");
        }
        catch (IOException e){
            System.out.println("rename operation FAILED");
            Files.delete(oldFile);
            Files.delete(newFile);
        }
        // Files.move(copyOutput, copyOutput.resolveSibling("output.txt"));
        System.out.println();

         */




        // WRITER ((LAST VERSION!) main part of program)
        /*
        FileWriter writer = new FileWriter(output, true);
        FileWriter copyWriter = new FileWriter(copyOutput);
        FileWriter newWriter = new FileWriter(newOutput);
        writer.write(str + "\n");
        writer.flush();
        writer.close();

         */


        // deleteBusiness(1);
        deleteBusiness(2);

        deleteEmpty();

        // do a method "printBusiness"
        System.out.println("Your to-do list: ");

        List<String> linesToOutput = Files.readAllLines(path);
        for (String line : linesToOutput) {
            System.out.println(line);
        }

        // удалим третий элемент из списка дел
        // Path newPath = Paths.get("newPath.txt");



        // OUTPUT to-do-list
        /*
        FileReader reader = new FileReader(copyOutput);
        BufferedReader buffReader = new BufferedReader(reader);
        while (buffReader.ready()) {
            System.out.println(buffReader.readLine());
        }

         */

        /*
        writer = new PrintWriter(new FileWriter("output.txt"));
        int current = 0;
        String line;
        while ((line = buffReader.readLine()) != null) {
            if (current != index) {
                writer.println(line);
            }
            current++;
        }
        */
        /*
        int c;
        while (reader.read() != -1) {
            System.out.print(reader.read());
        }
        */

    }

    public static void deleteBusiness(int index) throws IOException {
        Path path = Paths.get("path.txt");
        Path newPath = Paths.get("newPath.txt");
        Files.createFile(newPath);
        List<String> linesToRewrite = Files.readAllLines(path);
        for (int i = 0; i < linesToRewrite.size(); i++) {
            if (i != (index - 1)) {
                byte[] byteLine = (linesToRewrite.get(i) + '\n').getBytes();
                Files.write(newPath, byteLine, StandardOpenOption.APPEND);
            } else {
                String nextDoor = "\n";
                byte[] byteLine = nextDoor.getBytes();
                Files.write(newPath, byteLine, StandardOpenOption.APPEND);
            }
        }
        String nextDoor = "\n";
        byte[] byteLine = nextDoor.getBytes();
        Files.write(newPath, byteLine, StandardOpenOption.APPEND);
        if (Files.exists(path)) {
            Files.delete(path);
        }
        Files.copy(newPath, path);
        if (Files.exists(newPath)) {
            Files.delete(newPath);
        }

        /*

        Files.delete(path);

        BufferedReader reader = null;
        PrintWriter writer = null;

        File output = new File("output.txt");
        File copyOutput = new File("copyOutput.txt");
        FileReader fReader = new FileReader(output);
        reader = new BufferedReader(fReader);
        FileWriter fWriter = new FileWriter(copyOutput);
        writer = new PrintWriter(fWriter);

        int current = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            if (current != index) {
                writer.print(line + "\n");
            }
            current++;
        }
        */

        // try to rename (false)
        /*
        File newOutput = new File("newOutput.txt");
        FileWriter newWriter = new FileWriter(newOutput);
        FileReader newReader = new FileReader(newOutput);
        newWriter.close();
        newReader.close();
        boolean rename = copyOutput.renameTo(newOutput);
        System.out.println(rename);


        reader.close();
        writer.close();

         */
    }

    public static void deleteEmpty() throws IOException {
        Path path = Paths.get("path.txt");
        Path newPath = Paths.get("newPath.txt");
        Files.createFile(newPath);
        List<String> linesToRewrite = Files.readAllLines(path);
        for (String lineToCheck : linesToRewrite) {
            if (!(lineToCheck.isEmpty())) {
                String nextDoor = "\n";
                lineToCheck += nextDoor;
                byte[] byteLine = lineToCheck.getBytes();
                Files.write(newPath, byteLine, StandardOpenOption.APPEND);
            }
        }
        if (Files.exists(path)) {
            Files.delete(path);
        }
        Files.copy(newPath, path);
        if (Files.exists(newPath)) {
            Files.delete(newPath);
        }
    }
}



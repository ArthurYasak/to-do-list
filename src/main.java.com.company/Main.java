package main.java.com.company;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("Hello!");
        System.out.println("Type your business: ");

        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();

        File output = new File("output.txt");
        FileWriter writer = new FileWriter(output, true);
        writer.write(str + "\n");
        writer.flush();
        writer.close();

        System.out.println("Your to-do list: ");
        FileReader reader = new FileReader("output.txt");
        BufferedReader buffReader = new BufferedReader(reader);
        while (buffReader.ready()) {
            System.out.println(buffReader.readLine());
        }
        /*
        int c;
        while (reader.read() != -1) {
            System.out.print(reader.read());
        }
        */
    }
}

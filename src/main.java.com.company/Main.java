package main.java.com.company;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello");
        System.out.println("Type your business: ");
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        File output = new File("output.txt");
        FileWriter writer = new FileWriter(output, true);
        writer.write(str + "\n");
        writer.flush();
        writer.close();
        FileReader reader = new FileReader("output.txt");
        System.out.println("Your to-do list");
        int c;
        while((c=reader.read())!=-1){
            System.out.print((char)c);
        }

    }
}

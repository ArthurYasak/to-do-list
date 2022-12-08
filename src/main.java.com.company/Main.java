package main.java.com.company;

import java.io.IOException;
import java.io.EOFException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Calendar;
import java.util.Date;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Scanner sc = new Scanner(System.in);

        while (true) {    // user commands input (realize later)

            break;
        }

        ListsControl.addList();

        // *maybe* add this realization to ToDoList.addBusiness()
        System.out.println("Available lists:");
        for (ToDoList list : ListsControl.toDoLists) {
            System.out.println(list.getName());
        }
        System.out.println("Write ToDoList name to add business");
        String listToSearch = sc.nextLine();
        ToDoList currentList = ListsControl.searchList(listToSearch);
        if (currentList != null) {
            Path path = Paths.get(currentList.getName() + ".txt");
            System.out.println("How many businesses want you add");
            int n = sc.nextInt();
            sc.nextLine();
            for (int i = 0; i < n; i++) {
                currentList.addBusiness(path);
            }

        } else {
            System.out.println("List with this name does not exist");
        }

        System.out.println("Write ToDoList for print its business.");
        String listToPrint = sc.nextLine();
        currentList = ListsControl.searchList(listToPrint);
        if (currentList != null) {
            ToDoList.printList(currentList);
        } else {
            System.out.println("List with this name does not exist");
        }

        ListsControl.printLists();

    }

    // for deleteBusiness
    /*
        System.out.println("Type your business to delete (or press 0):");
        int delBusinessNum = 0;
        if (sc.hasNextInt()) {
            delBusinessNum = sc.nextInt();
        } else {
            System.out.println("Введено не число");
        }

         */

    // methods for class ToDoList
    /*

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
        if (Files.exists(newPath)) {
            Files.copy(newPath, path);
        }
        if (Files.exists(newPath)) {
            Files.delete(newPath);
        }
    }

     */


}

class ListsControl {
    private int amount; // for amount of added lists (now is not use)
    private String listName;
    static ArrayList<ToDoList> toDoLists = new ArrayList<>();
    static void addList() throws IOException, ClassNotFoundException {
        String toDoListsObj = "toDoListsObj.ser";
        Path forSave = Paths.get(toDoListsObj);

        // SERIALIZATION
        FileOutputStream outputStream;
        ObjectOutputStream objectOutputStream;
        if (Files.exists(forSave)) {
            // Stream to write data to file (connection stream)
            outputStream = new FileOutputStream(toDoListsObj, true);

            // Stream to convert ToDoList object to bytes (chain (цепной) stream) WITHOUT WRITING HEADER
            objectOutputStream = new AppendingObjectOutputStream(outputStream);
        } else {
            // Stream to write data to file (connection stream)
            outputStream = new FileOutputStream(toDoListsObj);

            // Stream to convert ToDoList object to bytes (chain (цепной) stream)
            objectOutputStream = new ObjectOutputStream(outputStream);
        }

        // DESERIALIZATION
        // Stream to connect to file and to read data from this file (connection stream)
        FileInputStream inputStream = new FileInputStream(toDoListsObj);

        // Stream to convert bytes to ToDoList object (chain (цепной) stream)
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        // filling the ArrayList from file
        boolean flagForCheck = true;
        while (inputStream.available() != 0) {
            ToDoList listForCheck = new ToDoList();
            try {
                listForCheck = (ToDoList) objectInputStream.readObject();
            } catch (EOFException e) {
                System.out.println("End of file reached"); // print when reset() available
            }
            Path listCheck = Paths.get(listForCheck.getName() + ".txt");
            if (Files.exists(listCheck)) {
                toDoLists.add(listForCheck);
            } else {
                flagForCheck = false;
            }
        }

        // checking if .txt files with ListsControl is not exist anymore
        if (!flagForCheck) {
            FileOutputStream rewriteStream = new FileOutputStream(toDoListsObj, true);
            ObjectOutputStream rewriteObjectStream = new AppendingObjectOutputStream(rewriteStream);
            for (ToDoList thisToDoList: toDoLists) {
                objectOutputStream.writeObject(thisToDoList);
            }
        }

        Scanner sc = new Scanner(System.in);
        ToDoList toDoList = new ToDoList();

        System.out.println("Enter the name of list.");
        String listName = sc.nextLine();
        if (listName.isEmpty()) {
            System.out.println("List name field is empty");
            return;
        }
        toDoList.setName(listName);
        Path path = Paths.get(toDoList.getName() + ".txt");
        if (!(Files.exists(path))) {


            System.out.println("Enter the description of list.");
            toDoList.setDescription(sc.nextLine());
            Files.createFile(path);
            objectOutputStream.writeObject(toDoList); // Save ToDoList object to file
            toDoLists.add(toDoList);
            // outputStream.close();
            // objectOutputStream.close();
            // System.out.println(inputStream.available());
            System.out.println("List was successfully added.");


            System.out.println("Do you want to add business in ToDoList " + toDoList.getName());
            System.out.println("Press 1 (Yes) or 0 (No)");
            if (sc.nextLine().equals("1")) {
                System.out.println("How many businesses you want to add?");
                int n = sc.nextInt();
                sc.nextLine();
                for (int i = 0; i < n; i++) {
                    toDoList.addBusiness(path);
                }
            }

        } else {
            System.out.println("List already exists.");
        }
    }
    
    static void printLists() {
        System.out.print("Available " + toDoLists.size());
        System.out.println((toDoLists.size() == 1)? " list:" : " lists:");
        for (ToDoList toDoList: toDoLists) {
            System.out.println(toDoList.getName());
            System.out.println("Date: " + toDoList.getDate());
            System.out.println("Description: " + toDoList.getDescription());
            System.out.println();
        }
    }

    public static ToDoList searchList(String listName) {
        for (ToDoList toDoListForCheck : toDoLists) {
            if (toDoListForCheck.getName().equals(listName)) {
                return toDoListForCheck;
            }
        }
        return null;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
    public String getListName() {
        return this.listName;
    }
}

class ToDoList implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private Calendar calendar = Calendar.getInstance();
    private Date date = calendar.getTime();
    private String description;
    static ArrayList<Business> busList = new ArrayList<>();
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {
        return this.date;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return this.description;
    }

    void addBusiness(Path path) throws IOException {

        // String businessObj = "businessObj.ser"; // want to try to use Data Base

        System.out.println("Print your business");
        Scanner sc = new Scanner(System.in);
        Business business = new Business();
        business.setName(sc.nextLine());
        System.out.println("Set importance for " + business.getName() + "\n" +
                "HIGH    -  press 1 \n" +
                "MEDIUM  -  press 2 \n" +
                "LOW     -  press 3 \n" +
                "or just press \"Enter\" to set default importance: MEDIUM");

        if (business.setImportance(sc.nextLine())) {
            System.out.println("For business " + business.getName() + " set " + business.getImportance() + " importance");
        } else {
            System.out.println("For business " + business.getName() + " stayed " + business.getImportance() + " importance");
        }

        String str = business.getName() + "\n";
        byte[] byteStr = str.getBytes();
        Files.write(path, byteStr, StandardOpenOption.APPEND);
        System.out.println("Business added!");
    }
    public static void deleteBusiness(int index) throws IOException {
        Path path = Paths.get("path.txt");
        Path newPath = Paths.get("newPath.txt");
        if (!(Files.exists(newPath))) {
            Files.createFile(newPath);
        }
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
        if (Files.exists(newPath)) {
            Files.copy(newPath, path);
        }
        if (Files.exists(newPath)) {
            Files.delete(newPath);
        }
    }


    static void printList(ToDoList listToPrint) throws IOException {
        System.out.printf("Businesses in %s:", listToPrint.getName());
        Path path = Paths.get(listToPrint.getName()+ ".txt");
        List<String> linesToOutput = Files.readAllLines(path);
        for (String line : linesToOutput) {
            System.out.println(line);
        }
    }
}



class Business {
    private String name;
    private Importance importance = Importance.MEDIUM;

    /**
     * Set importance for the Deal
     *
     * @param importance
     * @return result of Set importance (true or false)
     */
    public boolean setImportance(String importance) {
        switch (importance) {
            case "1":
                this.importance = Importance.HIGH;
                return true;
            case "2":
                this.importance = Importance.MEDIUM;
                return true;
            case "3":
                this.importance = Importance.LOW;
                return true;
            default:
                return false;
        }
    }

    public Importance getImportance() {
        return importance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

enum Importance {
    HIGH,
    MEDIUM,
    LOW
}

class AppendingObjectOutputStream extends ObjectOutputStream {
    public AppendingObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }
    @Override
    protected void writeStreamHeader() throws IOException {
        // reset(); // when active EOFException throws
    }
}
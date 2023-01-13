package main.java.com.company;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.NoSuchMethodException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Collections;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Blob;
import java.sql.ResultSet;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;


public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // System.out.println("Downloading lists...");
        ListsControl.download();
        ListsControl.printAllBusinesses();
        ListsControl.printListsInfo();
        ListsControl.addList();
        ListsControl.deleteList();
        ListsControl.addBusiness();
        ListsControl.deleteBusiness();
        ListsControl.listsDateSort();
        ListsControl.printAllBusinesses();


        // ListsControl.printLists();

        // add business to list which user want

        // delete business in list which user want

        /*
        System.out.println("In which list do you want print business?");
        name = sc.nextLine();
        listIndex = ListsControl.searchList(name);
        if (listIndex != -1) {
            ListsControl.toDoLists.get(listIndex).printList();
        } else {
            System.out.printf("List with name %s doesn't exist\n", name);
        }

         */

        // System.out.println("Lists saved");

        /*
        while (true) {    // user commands input (realize later (or not))
            System.out.println("What want you do?");
            String command = sc.nextLine();
            if (command.equals("Exit")) {
                break;
            } else {
                if (command.equals("Add")) {
                    System.out.println("What you want to add?");
                    String addCommand = sc.nextLine();
                    if (addCommand.equals("New List")) {

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

                    } else {
                        System.out.println("Wrong command");
                    }
                } else {
                    System.out.println("Wrong command");
                }
            }
        }

         */
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
    private int amount; // for amount of added lists (now is not in use)
    static ArrayList<ToDoList> toDoLists = new ArrayList<>();
    static void addList() {
        ToDoList list = new ToDoList();
        Scanner sc = new Scanner(System.in);

        // System.out.println(list);


        System.out.println("Do you want to add list?");
        String answer = sc.nextLine();
        if (answer.equals("yes")) {
            System.out.println("Enter the list name");
            String name = sc.nextLine();
            for (ToDoList checkList : toDoLists) {
                String checkName = checkList.getName();
                if (checkName.equals(name)) {
                    System.out.printf("List with name %s already exists\n", name);
                    return;
                }
            }
            list.setName(name);
            System.out.println("Enter the list description");
            list.setDescription(sc.nextLine());

            // write list to toDoLists ArrayList
            toDoLists.add(list);
            saveToBase(list);
        }
        if (answer.isEmpty()) {
            System.out.println("Field \"answer\" is empty");
        }
    }
    static void deleteList() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Do you want to delete list");
        String answer = sc.nextLine();
        if (answer.equals("yes")) {
            printListsNames();
            System.out.println("Enter the list number to delete");
            if (!sc.hasNextInt()) {
                System.out.println("Not a number");
                return;
            }
            int delNumber = sc.nextInt() - 1;
            if (delNumber < 0 || delNumber > toDoLists.size() - 1) {
                System.out.println("No list with such number");
                return;
            }
            ToDoList list = toDoLists.get(delNumber);
            deleteFromBase(list);
            toDoLists.remove(delNumber);
        }
    }
    static void addBusiness() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Do you want to add business?");
        if (sc.nextLine().equals("yes")) {
            printListsNames();
            System.out.println("To which list do you want to add business?");
            String name = sc.nextLine();
            ToDoList list = ListsControl.searchList(name);
            // System.out.println(list);
            if (list != null) {
                list.addBusiness();
                rewriteInBase(list);
                // ListsControl.addList(ListsControl.toDoLists.get(listIndex));
            } else {
                System.out.printf("List with name %s doesn't exist\n", name);
            }
        }
    }
    static void deleteBusiness() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Do you want to delete business?");
        if (sc.nextLine().equals("yes")) {
            printListsNames();
            System.out.println("In which list do you want to delete business?");
            String name = sc.nextLine();
            ToDoList list = ListsControl.searchList(name);
            // System.out.println(list);
            if (list != null) {
                if (list.deleteBusiness()) {
                    rewriteInBase(list);
                }
            } else {
                System.out.printf("List with name %s doesn't exists\n", name);
            }
        }
    }
    static void download() {
        // extract lists from data base

        // read Blob object from data base
        // System.out.println("Reading Blob object from dataBase");
        try (Connection conn = MyConnection.getConnection()) {
            // System.out.println("Creating statement...");
            Statement statement = conn.createStatement();


            // statement.execute("TRUNCATE TABLE lists");

            // statement.execute("ALTER TABLE lists ADD CONSTRAINT uniq_name UNIQUE (name)");

            String sqlToDoListBase = "USE ToDoListBase";
            String sqlExists = "SELECT EXISTS (SELECT * FROM lists)";
            String sqlSelect = "SELECT * FROM lists";
            // System.out.println("Choosing toDoListBase...");
            statement.execute(sqlToDoListBase);
            // System.out.println("Checking table (empty or not)");
            if (! statement.execute(sqlExists)) {
                System.out.println("Table is empty");
                return;
            }
            // System.out.println("Creating resultSet...");
            ResultSet resultSet = statement.executeQuery(sqlSelect);
            // System.out.println("while ResultSet.next()...");
            while (resultSet.next()) {
                // Blob blobObj = conn.createBlob();
                Blob blobObj = resultSet.getBlob("list");

                // convert Blob object to byte[] array
                byte[] byteObj;
                byteObj = blobObj.getBytes(1, (int)blobObj.length());

                // convert byte[] array to ToDoList object
                ToDoList list = null;
                InputStream is = new ByteArrayInputStream(byteObj);
                try (ObjectInputStream ois = new ObjectInputStream(is)) {
                    while (is.available() != 0) {
                        list = (ToDoList)ois.readObject();
                    }
                    toDoLists.add(list);
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
            // System.out.println("Connection was successful!");
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }
    static void saveToBase(ToDoList list) {
        try (Connection conn = MyConnection.getConnection()) {
            Statement statement = conn.createStatement();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] byteObj = null;
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(list);
                byteObj = baos.toByteArray();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            // convert byte[] array to Blob object
            Blob blobObj = conn.createBlob();
            blobObj.setBytes(1, byteObj);

            // write Blob object to Data Base
            String sqlAddList = "INSERT lists (name, description, list) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlAddList);
            preparedStatement.setString(1, list.getName());
            preparedStatement.setString(2, list.getDescription());
            preparedStatement.setBlob(3, blobObj);
            preparedStatement.execute();
        } catch (MysqlDataTruncation dtEx) {
            System.out.println(dtEx.getMessage());
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }
    static void rewriteInBase(ToDoList list) {
        try (Connection conn = MyConnection.getConnection()) {
            Statement statement = conn.createStatement();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] byteObj = null;
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(list);
                byteObj = baos.toByteArray();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            // convert byte[] array to Blob object
            Blob blobObj = conn.createBlob();
            blobObj.setBytes(1, byteObj);

            // delete list from Data Base
            String sqlDropList = "DELETE FROM lists WHERE (name = '" + list.getName() + "')";
            System.out.println(sqlDropList);
            System.out.println(statement.executeUpdate(sqlDropList));

            // write Blob object to Data Base
            String sqlAddList = "INSERT lists (name, description, list) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlAddList);
            preparedStatement.setString(1, list.getName());
            preparedStatement.setString(2, list.getDescription());
            preparedStatement.setBlob(3, blobObj);
            preparedStatement.execute();
        } catch (MysqlDataTruncation dtEx) {
            System.out.println(dtEx.getMessage());
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }
    static void deleteFromBase(ToDoList list) {
        try (Connection conn = MyConnection.getConnection()) {
            Statement statement = conn.createStatement();

            String sqlDropList = "DELETE FROM lists WHERE (name = '" + list.getName() + "')";
            System.out.println(sqlDropList);
            System.out.println(statement.executeUpdate(sqlDropList));
        } catch (MysqlDataTruncation dtEx) {
            System.out.println(dtEx.getMessage());
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }

    static void listsDateSort() {
        System.out.println("Lists sorted by date:");
        Collections.sort(toDoLists);
        printListsNames();
    }
    /*
    static void save() {
        // System.out.println("Start saving");
        try (Connection conn = MyConnection.getConnection()) {
            // System.out.println("Creating statement...");
            Statement statement = conn.createStatement();
            // statement.execute("ANALYZE TABLE lists");
            // statement.execute("REPAIR TABLE lists");
            // System.out.println("truncating table...");

            // statement.execute("DROP TABLE lists");
            // statement.execute("CREATE TABLE lists (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(20) UNIQUE, description VARCHAR(100), list BLOB)");

            statement.execute("TRUNCATE TABLE lists");

            // System.out.println("Converting to byte array");
            for (ToDoList list : toDoLists) {
                System.out.println(list);
                // convert ToDoList object to byte[] array
                // System.out.println("Working with list " + list.getName());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] byteObj = null;
                try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                    oos.writeObject(list);
                    byteObj = baos.toByteArray();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

                // convert byte[] array to Blob object
                // System.out.println("Convert byte[] array to Blob object");
                Blob blobObj = conn.createBlob();
                blobObj.setBytes(1, byteObj);

                // write Blob object to Data Base
                // System.out.println("Write Blob object to Data Base");
                String sqlAddList = "INSERT lists (name, description, list) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sqlAddList);
                preparedStatement.setString(1, list.getName());
                preparedStatement.setString(2, list.getDescription());
                preparedStatement.setBlob(3, blobObj);
                // System.out.println("proceed...");
                preparedStatement.execute();
                // System.out.println("Ended");
            }
        } catch (MysqlDataTruncation dtEx) {
            System.out.println(dtEx.getMessage());
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }

     */
    public static ToDoList searchList(String listName) {
        /*
        for (int i = 0; i < toDoLists.size(); i++) {

            if (toDoLists.get(i).getName().equals(listName)) {
                return i;
            }
        }
        return -1;
         */
        for (ToDoList toDoListForCheck : toDoLists) {
            String name = toDoListForCheck.getName();
            if (toDoListForCheck.getName() == null) {
                return null;
            }
            if (name.equals(listName)) {
                return toDoListForCheck;
            }
        }
        return null;

    }
    static void printListsNames() {
        System.out.println("Available lists:");
        for (int i = 0; i < toDoLists.size(); i++) {
            ToDoList list = toDoLists.get(i);
            System.out.println(i+1 + ". " + list.getName());
        }
    }
    static void printListsInfo() {

        System.out.print("Available info for " + toDoLists.size());
        System.out.println((toDoLists.size() == 1)? " list:" : " lists:");
        for (ToDoList toDoList: toDoLists) {
            System.out.println(toDoList.getName());
            System.out.println("Date: " + toDoList.getDate());
            System.out.println("Description: " + toDoList.getDescription());
            // System.out.println("BusList: " + toDoList.busList.get(0));
            System.out.println();
        }
    }
    static void printAllBusinesses() {
        System.out.println("Print businesses in all lists");
        for (ToDoList list : ListsControl.toDoLists) {
            // System.out.println(list);
            list.printThisList();
        }
    }

}

class ToDoList implements Serializable, Comparable<ToDoList> {
    private static final long serialVersionUID = 1L;
    private String name;
    private Calendar calendar = Calendar.getInstance();
    private Date date = calendar.getTime();
    private String description;
    ArrayList<Business> busList = new ArrayList<>();
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
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

    void addBusiness() {

        // String businessObj = "businessObj.ser"; // want to try to use Data Base

        /*
        if (busList == null) {
            busList = new ArrayList<>();
        }

         */
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
        busList.add(business);
        // String str = business.getName() + "\n";
        // byte[] byteStr = str.getBytes();
        // Files.write(path, byteStr, StandardOpenOption.APPEND);
        // System.out.println("Business added!");
    }
    public boolean deleteBusiness() {
        Scanner sc = new Scanner(System.in);
        printThisList();
        System.out.println("Enter list number to delete");
        if (sc.hasNextInt()) {
            int delNum = sc.nextInt();
            if (delNum - 1 < busList.size() && delNum - 1 >= 0) {
                busList.remove(delNum - 1);
                return true;
            } else {
                System.out.println("No business with such number");
                return false;
            }
        } else {
            System.out.println("Not a number");
            return false;
        }
        // work with files (old version)
        /*
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

         */
    }


    void printThisList() {
        /*
        if (busList == null) {
            busList = new ArrayList<>();
        }

         */
        System.out.printf("Businesses in %s:\n", this.name);
        for (int i = 0; i < this.busList.size(); i++) {
            System.out.println(i + 1 + ". " + busList.get(i).getName() +
                    ". Importance: " + busList.get(i).getImportance());
        }
        /*
        Path path = Paths.get(listToPrint.getName()+ ".txt");
        List<String> linesToOutput = Files.readAllLines(path);
        for (String line : linesToOutput) {
            System.out.println(line);
        }

         */
    }
    @Override
    public int compareTo(ToDoList list) {
        Date thisDate = this.getDate();
        long thisTime = thisDate.getTime();
        Date listDate = list.getDate();
        long listTime = listDate.getTime();
        int difference = (int) (thisTime - listTime);
        return difference;
    }
}



class Business implements Serializable {
    public static final long serialVersionUID = 1L;
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

// class for connection to data base MySQL
class MyConnection {
    private static String url;
    private static String user;
    private static String password;
    private static void downloadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            // System.out.println("Driver downloaded");
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException | ClassNotFoundException ex ) {
            ex.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException, IOException {
        downloadDriver();
        Properties props = new Properties();
        Path path = Paths.get("ToDoListBase.properties");
        try (InputStream is = Files.newInputStream(path)) {
            props.load(is);
        }
        url = props.getProperty("url");
        user = props.getProperty("user");
        password = props.getProperty("password");
        return DriverManager.getConnection(url, user, password);
    }
    public static void initDataBase() {
        String sqlToDoListBase = "USE todolistbase";
        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS lists " +
                "(id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(20), description VARCHAR(100), list BLOB)";
        try (Connection conn = MyConnection.getConnection()) {
            Statement statement = conn.createStatement();
            statement.execute(sqlToDoListBase);
            statement.execute(sqlCreateTable);
        } catch (SQLException | IOException initEx) {
            System.out.println(initEx.getMessage());
            System.out.println("Data base initialisation was unsuccessfully");
        }

    }

}
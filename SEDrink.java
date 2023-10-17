/*************************************************
 *  Program : SE Drink #9
 *  Student ID : 65160326
 *  Student Name : Natnawat Panisarasirikul
 *  Date : 12 OCT 2023
 *  Description : Vending Machine
 *  Note : Drink menu and User order
 *************************************************/

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class SEDrink {
    
    static File machineFile = new File("./machine.txt");
    static File adminFile = new File("./login.txt");
    static File menuFile = new File("./Menu.txt");
    static File orderFile = new File("./Order.txt");

    static LinkedList<Object[]> adminDataSet = new LinkedList<>();
    static LinkedList<Object[]> machineDataSet = new LinkedList<>();
    static LinkedList<Object[]> menuDataSet = new LinkedList<>();
    static LinkedList<Object[]> orderDataSet = new LinkedList<>();

    /* CHECK SHIPPING TYPE
     * LATITUDE LONGITUDE   TYPE
     *     -        -       SHIPS
     *     +        +       PLANE
     *     +        -       TRUCK
     *     -        +       TRUCK
     */
    public static String checkShippingType(double latitude, double longitude) {
        if (latitude < 0.0 && longitude < 0.0) {
            return "Ships";
        } else if (latitude > 0.0 && longitude > 0.0) {
            return "Planes";
        } else {
            return "Trucks";
        }
    }

    /* HASHING PASSWORD
     * 
     */
    public static String hashPassword(String password) {
        String newPassword = "";
        for (int i = 0; i < 18; i++) {
            Random r = new Random();
            char c = (char) (r.nextInt(26) + 'A');
            switch (i) {
                case 3:
                    newPassword += password.charAt(0);
                    break;
                case 4:
                    newPassword += password.charAt(1);
                    break;
                case 8:
                    newPassword += '1';
                    break;
                case 11:
                    newPassword += password.charAt(2);
                    break;
                case 12:
                    newPassword += password.charAt(3);
                    break;
                case 13:
                    newPassword += password.charAt(4);
                    break;
                case 14:
                    newPassword += password.charAt(5);
                    break;
                default:
                    newPassword += c;
                    break;
            }
        }
        
        return newPassword;
    }
    
    /* UPDATE MACHINE DATASET
     * 0 => ID              STRING
     * 1 => CITY            STRING
     * 2 => LATITUDE        DOUBLE
     * 3 => LONGITUDE       DOUBLE
     * 4 => ACCOUNT NUMBER  STRING
     * 5 => BALANCE         DOUBLE
     * 6 => ID?             STRING
     */
    public static void updateMachine() throws IOException {
        Scanner reader = new Scanner(machineFile);

        machineDataSet.clear();

        while (reader.hasNextLine()) { // Init machineDataSet
            Object[] rawData = reader.nextLine().split("\t");
            Object[] data = {
                rawData[0], //0 => ID
                rawData[1], //1 => CITY
                Double.parseDouble(rawData[2].toString().split(", ")[0]), //2 => LATITUDE
                Double.parseDouble(rawData[2].toString().split(", ")[1]), //3 => LONGITUDE
                rawData[3], //4 => ACC NO.
                Double.parseDouble(rawData[4].toString().replaceAll("[$,]", "")), //5 => BALANCE
                rawData[5]
            };

            machineDataSet.add(data);
        }
        reader.close();
    }

    /* UPDATE MENU DATASET
     * 0 => ID              STRING
     * 1 => NAME            STRING
     * 2 => PRICE           STRING
     * 3 => REC.AGES        STRING
     * 4 => REC.GENDER      STRING
     * 5 => TYPE            STRING
     * 6 => DESCRIPTION     STRING
     */
    public static void updateMenu() throws IOException {
        Scanner reader = new Scanner(menuFile);

        menuDataSet.clear();

        while (reader.hasNextLine()) { // Init menuDataSet
            Object[] rawData = reader.nextLine().split("\t");

            Object[] data =  {
                rawData[0], //0 => ID
                rawData[1], //1 => NAME
                rawData[2], //2 => PRICE
                rawData[3], //3 => REC.AGES
                rawData[4], //4 => REC.GENDER
                rawData[5],//5 => TYPE
                rawData[6]// 6 => DESCRIPTION
            };
            
            menuDataSet.add(data);
        }
        reader.close();
    }
    
    /* UPDATE ADMIN DATASET
     * 0 => ID              STRING
     * 1 => FIRST NAME      STRING
     * 2 => LAST NAME       DOUBLE
     * 3 => EMAIL           DOUBLE
     * 4 => PASSWORD HASH   STRING
     * 5 => TELEPHONE       DOUBLE
     */
    public static void updateAdmin() throws IOException {
        Scanner reader = new Scanner(adminFile);

        adminDataSet.clear();

        while (reader.hasNextLine()) { // Init adminDataSet
            Object[] rawData = reader.nextLine().split("\t");

            Object[] data =  {
                rawData[0], //0 => ID
                (((String)rawData[1]).split(" "))[0], //1 => FIRSTNAME
                (((String)rawData[1]).split(" "))[1], //2 => LASTNAME
                rawData[2], //3 => EMAIL
                rawData[3], //4 => PASSWORD
                rawData[4] //5 => TEL.
            };
            
            adminDataSet.add(data);
        }
        reader.close();
    }
    
    /* UPDATE ORDER DATASET
     * 0 => ID              STRING
     * 1 => MENU ID         STRING
     * 2 => MACHINE ID      STRING
     * 3 => CUSTOMER TEL.   STRING
     * 4 => DRINK PIN       STRING
     * 5 => STATUS          STRING
     * 6 => USE DATE        STRING
     */
    public static void updateOrder() throws IOException {
        Scanner reader = new Scanner(orderFile);

        orderDataSet.clear();

        while (reader.hasNextLine()) { // Init orderDataSet
            Object[] rawData = reader.nextLine().split("\t");
            Object[] data =  {
                rawData[0], //0 => ID
                rawData[1], //1 => MENU ID
                rawData[2], //2 => MACHINE ID
                rawData[3], //3 => CUSTOMER TEL.
                rawData[4], //4 => DRINK PIN
                rawData[5], //5 => STATUS
                rawData[6] //6 => USE DATE
            };
            
            orderDataSet.add(data);
        }
        reader.close();
    }
    
    /* ADD DATA TO FILE
     * 
     */
    public static void addToFile(Object[] data, File file, String format) throws IOException {
        FileWriter fileWriter = new FileWriter(file, true);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        String outputValue = String.format("\n"+format, data);
        printWriter.print(outputValue);

        printWriter.close();
    }

    public static void rewriteMachineDataSet() throws IOException {
        FileWriter edit_fileWriter = new FileWriter(machineFile);
        PrintWriter printWriter = new PrintWriter(edit_fileWriter);
        for (int i = 0; i < machineDataSet.size(); i++) {
            Object[] edit_data = machineDataSet.get(i);
            DecimalFormat format = new DecimalFormat("#,###.00");
            String outputValue = String.format(
                "%s\t%s\t%.10f, %.10f\t%s\t%s\t%s" + (i == machineDataSet.size()-1 ? "" : "\n"),
                edit_data[0],
                edit_data[1],
                Double.parseDouble(edit_data[2].toString()),
                Double.parseDouble(edit_data[3].toString()),
                edit_data[4],
                "$"+format.format(Double.parseDouble(edit_data[5].toString())),
                edit_data[6]
            );
            
            printWriter.printf(outputValue);
            
        }
        printWriter.close();
    }

    public static void rewriteOrderDataSet() throws IOException {
        FileWriter edit_fileWriter = new FileWriter(orderFile);
        PrintWriter printWriter = new PrintWriter(edit_fileWriter);
        for (int i = 0; i < orderDataSet.size(); i++) {
            Object[] edit_data = orderDataSet.get(i);
            String outputValue = String.format(
                "%s\t%s\t%s\t%s\t%s\t%s\t%s" + (i == orderDataSet.size()-1 ? "" : "\n"),
                edit_data[0],
                edit_data[1],
                edit_data[2],
                edit_data[3],
                edit_data[4],
                edit_data[5],
                edit_data[6]
            );
            
            printWriter.printf(outputValue);
            
        }
        printWriter.close();
    }
    
    /* show data from machine data
     * 
     */
    public static void showMachineData(){
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
        System.out.printf("ID\t\tCity\t\tPosition\t\tShippingType\tAccount Number\t\tBalance($)\n");
        System.out.println("--------------------------------------------------------------------------------------------------------------------");

        for (int i = 0; i < machineDataSet.size(); i++) {
            Object[] data = machineDataSet.get(i);

            DecimalFormat format = new DecimalFormat("#,###.##");
            String balance = (format.format(Double.parseDouble(data[5].toString())));

            System.out.printf(
                "%-6s\t%-15s\t%.2f, %.2f\t\t%s\t\t%s\t%s\n", 
                data[0], 
                data[1], 
                data[2], 
                data[3], 
                checkShippingType((double)data[2], (double)data[3]), 
                data[4].toString().substring(0, 12)+"xxxx", 
                balance.substring(0, balance.indexOf(",")) + balance.substring(balance.indexOf(",")).replaceAll("[0123456789]", "x")
                );
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
    }
    
    public static void showMachineData_OnlyIDName(){
        System.out.println("-------------------------");
        System.out.printf("ID\t\tCity\n");
        System.out.println("-------------------------");

        for (int i = 0; i < machineDataSet.size(); i++) {
            Object[] data = machineDataSet.get(i);

            System.out.printf(
                "%-6s\t%s\n", 
                data[0].toString().substring(3,5) + data[0].toString().substring(8,9), 
                data[1]
                );
        }
        System.out.println("-------------------------");
    }

    /* show data from menu data
     * 
     */
    public static void showMenu(){
        System.out.println("--------------------------------------");
        System.out.printf("ID\tMenu\t\t\tPrice\n");
        System.out.println("--------------------------------------");

        for (int i = 0; i < menuDataSet.size(); i++) {
            Object[] data = menuDataSet.get(i);

            System.out.printf("%-3s\t%-20s\t%s\n",
            data[0],data[1],data[2]
                );
        }
        System.out.println("--------------------------------------");
    }

    public static void sort(LinkedList<Object[]> dataSet, int dataIndex, boolean isASC, boolean isValue) {
        
        if (isValue) { // sort value
            if (isASC) { // ASC // BUBBLE
                boolean swapped = false;
                for (int i = 0; i < dataSet.size()-1; i++) {
                    for (int j = 0; j < dataSet.size()-i-1; j++) {
                        Object[] data = dataSet.get(j);
                        Object[] data_next = dataSet.get(j+1);

                        System.out.println(data[dataIndex].toString());

                        if (Double.parseDouble(data[dataIndex].toString()) > Double.parseDouble(data_next[dataIndex].toString()) ) {
                            Object[] temp = data;
                            dataSet.set(j, data_next);
                            dataSet.set(j+1, temp);
                            swapped = true;
                        }
                        
                        if (!swapped) break;
                    }
                }
            } else { // DESC // SHELL
                int gap = dataSet.size()/2;
                while (gap > 0) {
                    for (int index = gap; index < dataSet.size(); index++) {
                        int temp = Integer.parseInt(dataSet.get(index)[dataIndex].toString());
                        int j = index;
                        while (j >= gap && Integer.parseInt(dataSet.get(j-gap)[dataIndex].toString()) > temp) {
                            dataSet.set(j,dataSet.get(j-gap));
                            j -= gap;
                        }
                        gap /= 2;
                    }
                }
            }
            
        } else { // sort Alphabet
            if (isASC) { // ASC // INSERTION 
                for (int i = 1; i < dataSet.size(); i++) {
                    for (int j = i-1; j < dataSet.size(); j++) {
                        Object[] data = dataSet.get(i);
                        Object[] data_next = dataSet.get(j);

                        if (compareTo(data_next[dataIndex], data[dataIndex]) < 0) {
                            
                        }
                    }
                }
            } else { // DESC // SELECTION
                
            }
        }
    }

    public static Object[] search(LinkedList<Object[]> dataSet, int dataIndex, Object value) {
        for (int i = 0; i < dataSet.size(); i++) {
            Object[] data = dataSet.get(i);
            if (data[dataIndex].toString().equals(value.toString())) {
                return data;
            }
        }

        return null;
    }
    
    public static void mostPopularDrink(String forWho, int rank) throws Exception {
        LinkedList<Object[]> rankDataSet = new LinkedList<>();
        

        sort(orderDataSet, 6, false, false);

        for (int i = 0; i < orderDataSet.size(); i++) {
            Object[] orderData = orderDataSet.get(i);

            for (int j = 0; j < menuDataSet.size(); j++) {
                Object[] menuData = menuDataSet.get(j);

                if (!menuData[4].toString().equals(forWho) || !menuData[0].toString().equals(orderData[1].toString())) {
                    continue;
                }

                boolean haveValue = false;
                for (int k = 0; k < rankDataSet.size(); k++) {
                    Object[] rankData = rankDataSet.get(k);
                    if (orderData[1].toString().equals(rankData[0].toString())) {
                        Object[] temp = {
                            rankData[0],
                            rankData[1],
                            rankData[2],
                            Integer.parseInt(rankData[3].toString()) + 1
                        };

                        rankDataSet.set(k, temp);
                        haveValue = true;
                        break;
                    }   
                }
                
                if (!haveValue) {
                    Object[] temp = {
                        menuData[0],
                        menuData[1],
                        orderData[6],
                        1
                    }; 

                    rankDataSet.add(temp);
                }
                break;
            }
        }

        System.out.printf(
            "--------------------\n"+
            "Top 3 For-%s\n" +
            "--------------------",
            (forWho == "M"? "Men" : (forWho == "F"? "Women" : "All"))
        );

        sort(rankDataSet, 3, false, true);
        for (int i = 0; i < rankDataSet.size() && i < rank; i++) {
            Object[] data = rankDataSet.get(i);
            System.out.printf(
                "\n# %d : %s %s",
                i+1, 
                data[0],
                data[1] 
            );
        }
    }
    
    public static int compareTo(Object data, Object data_next) {
        for (int i = 0; i < data.toString().length() && i < data_next.toString().length(); i++) {
            if (data.toString().charAt(i) > data_next.toString().charAt(i)) {
                return 1;
            }
            if (data.toString().charAt(i) < data_next.toString().charAt(i)) {
                return -1;
            }
        }

        return 0;
    }
    
    /* |====================================|
     * |                MAIN                |
     * |====================================| */
    public static void main(String[] args) throws Exception {
   
        updateAdmin();
        updateMachine();
        updateMenu();
        updateOrder();

        Scanner input = new Scanner(System.in);

        int wrongPass = 0;

        Object[] userInfo = new Object[4];
        String menu = "";

        do{
            System.out.print(
                "-------------------\n"+
                "SE BUU Drink\n"+
                "-------------------\n"+
                "1. Ordering your drink\n"+
                "2. PIN Check\n"+
                "3. Most popular drink\n" +
                "4. Virtual machine\n" +
                "5. Login\n"+
                "6. Exit\n"+
                "-------------------\n"+
                "Enter Number : "
            );
            menu = input.next();
            System.out.println("-------------------");
            switch(menu){
                case "1":
                sort(menuDataSet, 2, true, true);
                showMenu();
                System.out.print("Enter menu ID : ");
                String menuID = input.next();
                System.out.println("-----------------------");
                boolean haveID = false;
                for (int i = 0; i < menuDataSet.size(); i++) {
                    Object[] data = menuDataSet.get(i);
                    if(data[0].equals(menuID)){
                        haveID = true;
                        break;
                    }
                }
                if(!haveID){
                    break;
                }
                System.out.print("Enter Tel. : ");
                String telephone = input.next();
                System.out.println("-----------------------");
                if(telephone.length() != 10){
                    System.out.println("Your telephone is error");
                    break;
                }
                Random r = new Random();
                char c1 = (char)(r.nextInt(26) + 'A');
                char c2 = (char)(r.nextInt(26) + 'A');
                String pin = String.valueOf(r.nextInt(1000,9999))+c1+c2;
            
                System.out.println("Your PIN is : " + pin);
                System.out.println("-----------------------");
                Object[] dataOrder = {
                    Integer.parseInt(orderDataSet.get(orderDataSet.size()-1)[0].toString())+1,
                    menuID,
                    "-",
                    telephone.substring(0, 3)+"-"+telephone.substring(3,6)+"-"+telephone.substring(6),
                    pin,
                    "0",
                    "-"
                };
                orderDataSet.add(dataOrder);
                addToFile(dataOrder, orderFile, "%s\t%s\t%s\t%s\t%s\t%s\t%s");
                System.out.println("Your order has been successfully ordered.");
                break;
                case "2":
                    System.out.println(
                        "-------------------\n" +
                        "     PIN Check\n" +
                        "-------------------"
                    );
                    System.out.print("Enter PIN : ");
                    String pinCheck = input.next();
                    System.out.println("-------------------");
                    boolean havePIN = false;
                    Object [] dataPIN = null;

                    for (int i = 0; i < orderDataSet.size(); i++) {
                        dataPIN = orderDataSet.get(i);
                        if(dataPIN[4].equals(pinCheck)){
                            havePIN = true;
                            break;
                        }
                    }
                    if(!havePIN){
                        System.out.println("Invalid PIN.");
                        System.out.println("-------------------");
                        break;
                    }
                    String menuName = "";
                    for (int i = 0; i < menuDataSet.size(); i++) {
                        Object[] dataMenu = menuDataSet.get(i);
                        if(dataMenu[0].equals(dataPIN[1])){
                            menuName = dataMenu[1].toString();
                            break;
                        }
                    }
                    System.out.println("Menu : " + menuName);
                    System.out.println("Status : " + (dataPIN[5].toString().equals("1") ? "Used" : "Not yet used"));
                    System.out.println("-------------------");

                    break;
                

                case "3":
                    do {
                        System.out.println(
                            "\n-------------------\n" +
                            "Most popular drink\n" +
                            "-------------------\n" +
                            "1. For-Men\n" + //
                            "2. For-Women\n" + //
                            "3. For-All\n" + //
                            "4. Exit\n" +
                            "-------------------"
                        );
                        System.out.print("Enter Number : ");
                        menu = input.next();

                        switch (menu) {
                            case "1":
                                mostPopularDrink("M", 3);
                                break;
                            case "2":
                                mostPopularDrink("F", 3);
                                break;
                            case "3":
                                mostPopularDrink("A" ,3);
                                break;
                            case "4":
                                break;
                        }
                    } while (!menu.equals("4"));
                    break;
                case "4":
                    showMachineData_OnlyIDName();
                    System.out.print("-------------------------\n" + //
                            "Enter Machine ID : ");
                    String machineID = input.next();
                    System.out.println("-------------------------");

                    Object[] machineData = null;
                    int machineDataIndex = -1;
                    for (int i = 0; i < machineDataSet.size(); i++) {
                        Object[] data = machineDataSet.get(i);

                        if ((data[0].toString().substring(3, 5) + data[0].toString().substring(8,9)).equals(machineID)) {
                            machineData = data;
                            machineDataIndex = i;
                            break;
                        }
                    }

                    if (machineDataIndex == -1) {
                        System.out.println("Invalid");
                        break;
                    } 

                    System.out.println("-------------------------");
                    System.out.printf(
                        "Machine ID : %s (%s)\n",
                        machineData[0].toString().substring(3, 5) + machineData[0].toString().substring(8,9),
                        machineData[1].toString()
                    );
                    System.out.println("-------------------------");
                    boolean ordered = false;
                    do {
                        System.out.println("1. Use your PIN to get a drink.");
                        System.out.println("2. Exit");
                        System.out.println("-------------------------");
                        System.out.print("Enter Number : ");
                        menu = input.next();
                        System.out.println("-------------------------");

                        switch (menu) {
                            case "1": // Use your PIN to get a drink
                                System.out.print("Enter your PIN : ");
                                String PIN = input.next();
                                System.out.println("-------------------------");

                                Object[] orderData = search(orderDataSet, 4, PIN);

                                if (orderData == null) {
                                    System.out.println("Invalid");
                                    break;
                                }

                                if (orderData[5].toString().equals("1")) {
                                    System.out.println("PIN is Already Used!!");
                                    break;
                                }

                                Object[] menuData = search(menuDataSet, 0, orderData[1]);

                                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                                Object[] temp_order = {
                                    orderData[0],
                                    orderData[1],
                                    machineData[0].toString(),
                                    orderData[3],
                                    orderData[4],
                                    "1",
                                    dateFormat.format(LocalDateTime.now())
                                };
                                orderDataSet.set(orderDataSet.indexOf(orderData), temp_order);

                                double price = Double.parseDouble(menuData[2].toString()) / 36.96;

                                Object[] temp_machine = {
                                    machineData[0],
                                    machineData[1],
                                    machineData[2],
                                    machineData[3],
                                    machineData[4],
                                    Double.parseDouble(machineData[5].toString()) + price,
                                    machineData[6]
                                };
                                machineDataSet.set(machineDataIndex, temp_machine);

                                rewriteOrderDataSet();
                                rewriteMachineDataSet();

                                System.out.printf(
                                    "Your %s is currently in the process of being prepared, please wait a moment.\n",
                                    menuData[1].toString()
                                );
                                ordered = true;

                                break;
                            case "2": // exit
                                break;
                        }
                    } while (!menu.equals("2") && !ordered);
                    break;
                case "5":
                    do {
                        System.out.print(
                            "-------------------\n" +
                            "       Login\n" +
                            "Username : "
                        );
                        String username = input.next();
                        System.out.print("Password : ");
                        String password = input.next();
                        System.out.println("-------------------");

                        for (int i = 0; i < adminDataSet.size(); i++) {
                            Object[] data = adminDataSet.get(i);

                            String deHash = ((String)data[4]).substring(3,5) + ((String)data[4]).substring(11,15);

                            if (((String)username).equals(data[3]) && ((String)password).equals(deHash)) {
                                if (((String)data[4]).charAt(8) == '0') {
                                    System.out.println("The users account has expired");
                                    return;
                                }
                                userInfo[0] = ((String)data[1]).charAt(0);
                                userInfo[1] = data[2];
                                userInfo[2] = data[3];
                                userInfo[3] = data[5];
                                wrongPass = 3;
                                break;
                            } else if (i == adminDataSet.size()-1) {
                                wrongPass++;
                                System.out.printf(
                                    "The username or password is incorrect. (%d)\n", 
                                    wrongPass
                                );
                            }

                            if (wrongPass == 3) {
                                System.out.println("Thank you for using the service.");
                                return;
                            }
                        }
                    } while (wrongPass < 3);

                menu = "";

                do {
                    System.out.printf(
                        "Welcome : %s. %s\n" +
                        "Email : %s\n" +
                        "Tel. : %sxxxx\n" +
                        "-------------------\n" +
                        "        Menu\n" +
                        "-------------------\n" +
                        "1. Machine details\n" +
                        "2. Add user\n" +
                        "3. Edit user\n" +
                        "4. Exit\n" +
                        "-------------------\n" +
                        "Enter Number : ",
                        userInfo[0], userInfo[1],userInfo[2], ((String)userInfo[3]).substring(0, 8)
                    );
                    menu = input.next();
                    System.out.println("-------------------");

                    switch (menu) {
                        case "1": // show machine data
                            do {
                                System.out.println("-------------------");
                                System.out.println("        Menu");
                                System.out.println("-------------------");
                                System.out.println("1. Sorting by balance (DESC)");
                                System.out.println("2. Sorting by city (ASC)");
                                System.out.println("3. Return to main menu");
                                System.out.println("-------------------");
                                System.out.print("Enter Number (1-3) : ");
                                menu = input.next();
                                System.out.println("-------------------");

                                switch(menu) {
                                    case "1": // Sorting by [5] balance (DESC)
                                        sort(machineDataSet, 5, false, true);
                                        showMachineData();
                                        break;
                                    case "2": // Sorting by [1] city (ASC)
                                        sort(machineDataSet, 1, true, false);
                                        showMachineData();
                                        break;
                                    case "3": // return to main menu
                                        break;
                                }
                            } while (!menu.equals("3"));
                            break;
                        case "2": // add admin user
                            String[] rawData = new String[6];
                            System.out.print("Enter first name :");
                            rawData[0] = input.next();
                            System.out.print("Enter Last name :");
                            rawData[1] = input.next();
                            System.out.print("Enter Email :");
                            rawData[2] = input.next();
                            System.out.print("Enter Password :");
                            rawData[3] = input.next(); 
                            System.out.print("Confirm Password :");
                            rawData[4] = input.next();
                            System.out.print("Enter Telephone number :");
                            rawData[5] = input.next();

                            boolean isSuccess = !(
                                rawData[0].length() < 2 || 
                                rawData[1].length() < 2 || 
                                !rawData[2].contains("@") || 
                                rawData[3].length() != 6 || 
                                !rawData[4].equalsIgnoreCase(rawData[3]) || 
                                rawData[5].length() != 10
                            );

                            if(!isSuccess){
                                System.out.println("The user added failed.");
                            }else{
                                int id = (Integer.valueOf((adminDataSet.get(adminDataSet.size()-1)[0]).toString()))+1;
                                String[] data = {
                                    String.valueOf(id),
                                    rawData[0], //name
                                    rawData[1], //last name
                                    rawData[2], //email
                                    hashPassword(rawData[3]), //pass
                                    String.join("-", rawData[5].substring(0, 3), rawData[5].substring(3, 6), rawData[5].substring(6)) //tel.
                                };

                                addToFile(data, adminFile, "%s\t%s %s\t%s\t%s\t%s");
                                adminDataSet.add(data);
                                System.out.println("The user added successfully!!!");
                            }
                            break;

                            
                        case "3": // edit admin user
                            System.out.print("Enter member ID : ");
                            String inputId = input.next();

                            Object[] data = null;
                            int dataIndex = -1;
                            for (int i = 0; i < adminDataSet.size(); i++) {
                                data = adminDataSet.get(i);

                                if (inputId.equals(data[0].toString())) {
                                    dataIndex = i;
                                    break;
                                }
                            }

                            if (dataIndex == -1) {
                                System.out.println("Invalid Member ID.!!!!");
                                break;
                            }
                            
                            System.out.println("Valid Member ID.");
                            System.out.println("-------------------");

                            do {
                                System.out.println("1. Edit user status");
                                System.out.println("2. Reset password");
                                System.out.println("3. Return to main menu");
                                System.out.println("-------------------");
                                System.out.print("Enter Number (1-3) : ");
                                menu = input.next();
                                System.out.println("-------------------");

                                switch (menu) {
                                    case "1":
                                        System.out.println("1. Active");
                                        System.out.println("2. Non-active");
                                        System.out.println("-------------------");
                                        System.out.print("Enter Number (1-2) : ");
                                        int statusSelect = input.nextInt();

                                        data[4] = data[4].toString().substring(0,8) +  (statusSelect == 1 ? '1' : '0') + data[4].toString().substring(9);

                                        System.out.println("This user status has been successfully edit.");
                                        break;
                                    case "2":
                                        System.out.println("Are you sure about resetting this password? ");
                                        System.out.println("-------------------");
                                        System.out.print("Enter Number (Y/N) : ");
                                        char resetPass_confirm = input.next().charAt(0);
                                        if (resetPass_confirm == 'N') {
                                            break;
                                        }

                                        r = new Random();
                                        String newPassword = String.valueOf((int)r.nextInt(000000,999999));


                                        data[4] = data[4].toString().substring(0,3) + newPassword.substring(0,2) + data[4].toString().substring(5,11) + newPassword.substring(2) + data[4].toString().substring(15);

                                        System.out.printf("This password has been successfully reset. =>  New password is \"%s\"\n", newPassword);
                                        break;
                                    case "3": // return to main menu
                                        break;
                                }
                                adminDataSet.set(dataIndex, data);

                                FileWriter edit_fileWriter = new FileWriter("./login.txt");
                                PrintWriter printWriter = new PrintWriter(edit_fileWriter);
                                for (int i = 0; i < adminDataSet.size(); i++) {
                                    Object[] edit_data = adminDataSet.get(i);
                                    String outputValue = String.format("%s\t%s %s\t%s\t%s\t%s\n",edit_data[0],edit_data[1],edit_data[2],edit_data[3],edit_data[4],edit_data[5]);
                                    
                                    printWriter.printf(outputValue);
                                    
                                }
                                printWriter.close();
                            } while (!menu.equals("3"));

                            
                        case "4": // Exit Service
                        System.out.println("Thank you for using the service");
                        break;
                }
            } while (!menu.equals("4"));
        break;
            case "6":
                break;
            
         }
        }while(!menu.equals("6"));

        input.close();
    }
}


import java.io.*;
import java.util.Scanner;

public class ArrayVersion {
    private static final int[] QueueCapacity = {2, 3, 5};
    private static final int MaxSize_Queue = 5;
    private static final int MaxCashiers = 3;
    private static final int BurgerLimit = 50;
    private static final String DataFilePath = "Foodies Fav Food Center.txt";
    private static boolean shouldexit = false;
    private static boolean[][] queues = new boolean[MaxCashiers][MaxSize_Queue]; //Array for the queue.
    private static String[][] queueNames = new String[MaxCashiers][MaxSize_Queue]; //Array to store customer names in each queue.
    private static int[] queueSize = {0, 0, 0}; //Array to store the size of each queue
    private static int burgersInStock = BurgerLimit; //Number of burgers in stock

    private static Scanner scanner = new Scanner(System.in);



    public static void main(String[] args) {
        initializing_Queue();
        mainMenu();
    }

    //Initializing the queues
    private static void initializing_Queue() {
        for (int i = 0; i < MaxCashiers; i++) {
            queues[i] = new boolean[QueueCapacity[i]];
            queueNames[i] = new String[QueueCapacity[i]];
        }
    }

    // Displays the menu options and handles user input
    private static void mainMenu() {
        System.out.println();
        System.out.println("                        Foodies Fave Food Center                       ");
        System.out.println();
        System.out.println("-----------------------------Menu Options-----------------------------");
        System.out.println("100 or VFQ: View all Queues");
        System.out.println("101 or VEQ: View all Empty Queues");
        System.out.println("102 or ACQ: Add customer to a Queue");
        System.out.println("103 or RCQ: Remove a customer from a Queue (From a specific location)");
        System.out.println("104 or PCQ: Remove a served customer");
        System.out.println("105 or VCS: View Customers Sorted in alphabetical order");
        System.out.println("106 or SPD: Store Program Data into file");
        System.out.println("107 or LPD: Load Program Data from file");
        System.out.println("108 or STK: View Remaining burgers Stock");
        System.out.println("109 or AFS: Add burgers to Stock");
        System.out.println("999 or EXT: Exit the Program");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println();
        System.out.println("Enter your choice:");
        String option = scanner.next().toUpperCase();
        scanner.nextLine();


        // Handle user input based on the chosen option
        switch (option) {
            case "100","VFQ" -> View_All_Queues();
            case "101","VEQ" -> View_All_Empty_Queues();
            case "102","ACQ" -> Add_Customer_To_Queue();
            case "103","RCQ" -> Remove_Customer_From_Queue();
            case "104","PCQ" -> Remove_Served_Customer();
            case "105","VCS" -> View_Customer_Sorted();
            case "106","SPD" -> Store_Program_Data();
            case "107","LPD" -> Load_Program_Data();
            case "108","STK" -> View_Remaining_Burger_Stock();
            case "109","AFS" -> Add_Burger_To_Stock();
            case "999","EXT" -> {
                System.out.println("Exiting the program...");
                shouldexit = true;
            }
            default -> System.out.println("Invalid choice.Please try again.");
        }
        System.out.println();

        if(!shouldexit) {
            mainMenu();
        }
    }


    private static void View_All_Queues(){
        // Function to display all the queues in the center.
        System.out.println("*****************");
        System.out.println("* Cashiers *");
        System.out.println("*****************");

        int maxQueueCapacity = getMaxQueueCapacity();

        for (int j = 0; j < maxQueueCapacity; j++) {
            for (int i = 0; i < MaxCashiers; i++) {
                if (j < QueueCapacity[i]) {
                    if (queues[i][j]) {
                        // "0" represented when the customer is present
                        System.out.print("O ");
                    } else {
                        // "1" represented when the Queue is empty
                        System.out.print("X ");
                    }
                } else {
                    // Empty spaces for queues with lower capacity
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }


    private static int getMaxQueueCapacity() {
        int maxCapacity = 0;
        for (int i = 0; i < MaxCashiers; i++) {
            if (QueueCapacity[i] > maxCapacity) {
                maxCapacity = QueueCapacity[i];
            }
        }
        return maxCapacity;

    }


    private static void View_All_Empty_Queues(){
        // The function to display all the empty queues
        // If there are no empty queues it will display it
        int emptyQueueCount = 0;
        for (int i = 0; i < MaxCashiers; i++) {
            if (queueSize[i] == 0) {
                System.out.println("Cashier" + (i + 1) + ": Queue is empty");
                emptyQueueCount++;
            }
        }
        if(emptyQueueCount==0){
            System.out.println("NO EMPTY QUEUES...");
        }
    }


    private static void Add_Customer_To_Queue(){
        // Function to add customers to specific cashier queue. when if the queue is not filled with the slots.
        System.out.println("Enter the cashier number (1-" + MaxCashiers + "):");
        int cashierNumber = scanner.nextInt();
        scanner.nextLine();

        if (cashierNumber < 1 || cashierNumber > MaxCashiers) {
            System.out.println("Invalid cashier number.");
            return;
        }

        int queueIndex = cashierNumber - 1;
        if (queueSize[queueIndex] == QueueCapacity[queueIndex]) {
            System.out.println("Queue " + cashierNumber + " is already full.");
            return;
        }

        System.out.println("Enter the customer name:");
        String customerName = scanner.nextLine();

        // Add the customer to the queue
        boolean isQueueEmpty = (queueSize[queueIndex] == 0);
        queues[queueIndex][queueSize[queueIndex]] = true;
        queueNames[queueIndex][queueSize[queueIndex]] = customerName;
        queueSize[queueIndex]++;

        System.out.println("Customer " + customerName + " added to queue " + cashierNumber);

        System.out.println();

        if (burgersInStock <= 10) {
            System.out.println("Low stock");
        }

        if (isQueueEmpty) {
            // Perform additional actions when the queue was initially empty
            System.out.println("Processing queue " + cashierNumber + ":");

            for (int i = 0; i < queueSize[queueIndex]; i++) {
                String processedCustomerName = queueNames[queueIndex][i];
                System.out.println("Processing customer: " + processedCustomerName);
                // Additional processing logic for each customer in the queue
            }

            System.out.println("Queue " + cashierNumber + " processed.");
        }
    }


    private static void Remove_Customer_From_Queue(){
        // Function to remove a customer from a specific location of the queue at a given position.
        System.out.println("Enter the cashier number (1-" + MaxCashiers + "):");
        int cashierNumber = scanner.nextInt();
        scanner.nextLine();

        if (cashierNumber < 1 || cashierNumber > MaxCashiers) {
            System.out.println("Invalid cashier number.");
            return;
        }

        int queueIndex = cashierNumber - 1;
        if (queueSize[queueIndex] == 0) {
            System.out.println("Queue " + cashierNumber + " is empty.");
            return;
        }

        System.out.println("Enter the position of the customer to remove (1-" + queueSize[queueIndex] + "):");
        int position = scanner.nextInt();
        scanner.nextLine();

        if (position < 1 || position > queueSize[queueIndex]) {
            System.out.println("Invalid position.");
            return;
        }

        String customerName = queueNames[queueIndex][position - 1];

        // Remove the customer from the queue
        boolean isQueueEmpty = (queueSize[queueIndex] == 1);
        for (int i = position - 1; i < queueSize[queueIndex] - 1; i++) {
            queues[queueIndex][i] = queues[queueIndex][i + 1];
            queueNames[queueIndex][i] = queueNames[queueIndex][i + 1];
        }

        queues[queueIndex][queueSize[queueIndex] - 1] = false;
        queueNames[queueIndex][queueSize[queueIndex] - 1] = null;
        queueSize[queueIndex]--;

        System.out.println("Customer " + customerName + " removed from queue " + cashierNumber);

        if (isQueueEmpty) {
            System.out.println("Queue " + cashierNumber + " is now empty.");
        }
    }


    private static void Remove_Served_Customer(){
        // Function to remove the first customer of a queue which is served with burgers.
        System.out.println("Enter the cashier number (1-" + MaxCashiers + "):");
        int cashierNumber = scanner.nextInt();
        scanner.nextLine();

        if (cashierNumber < 1 || cashierNumber > MaxCashiers) {
            System.out.println("Invalid cashier number.");
            return;
        }

        int queueIndex = cashierNumber - 1;
        if (queueSize[queueIndex] == 0) {
            System.out.println("Queue " + cashierNumber + " is empty.");
            return;
        }

        String servedCustomer = queueNames[queueIndex][0];

        // Remove the served customer from the queue
        boolean isQueueEmpty = (queueSize[queueIndex] == 1);
        for (int i = 0; i < queueSize[queueIndex] - 1; i++) {
            queues[queueIndex][i] = queues[queueIndex][i + 1];
            queueNames[queueIndex][i] = queueNames[queueIndex][i + 1];
        }

        queues[queueIndex][queueSize[queueIndex] - 1] = false;
        queueNames[queueIndex][queueSize[queueIndex] - 1] = null;
        queueSize[queueIndex]--;
        burgersInStock -= 5;

        System.out.println("Served customer " + servedCustomer + " removed from queue " + cashierNumber);

        if (isQueueEmpty) {
            System.out.println("Queue " + cashierNumber + " is now empty.");
        }
    }


    private static void View_Customer_Sorted(){
        // Function to sort the names of the customers from all the queues in alphabetical order
        String[] customers = new String[10];
        int customerCount = 0;

        // Collect all customer names from the queues
        for (int i = 0; i < MaxCashiers; i++) {
            for (int j = 0; j < QueueCapacity[i]; j++) {
                if (queues[i][j]) {
                    customers[customerCount] = queueNames[i][j];
                    customerCount++;
                }
            }
        }

        // Sort the customers array in alphabetical order
        for (int i = 0; i < customerCount - 1; i++) {
            for (int j = 0; j < customerCount - i - 1; j++) {
                if (customers[j].compareTo(customers[j + 1]) > 0) {
                    String temp = customers[j];
                    customers[j] = customers[j + 1];
                    customers[j + 1] = temp;
                }
            }
        }

        System.out.println("Customers sorted in alphabetical order:");
        for (int i = 0; i < customerCount; i++) {
            System.out.println(customers[i]);
        }
    }


    private static void Store_Program_Data(){
        // Function to sore the data in the queues and the stock of the remaining burgers.
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(DataFilePath))) {
            printWriter.println("Burgers in stock: " + burgersInStock); // Printing the remaining stock of burgers.

            for (int i = 0; i < MaxCashiers; i++) {
                printWriter.println("Queue " + (i + 1) + ":");
                for (int j = 0; j < queueSize[i]; j++) {
                    printWriter.println(queueNames[i][j]);
                }
            }
            printWriter.println();

            System.out.println("Program data stored successfully.");
        } catch (IOException e) {
            System.out.println("Error storing program data: " + e.getMessage());
        }
    }


    private static void Load_Program_Data(){
        //Function to load the previous data from the saved file and display in the console.
        try (Scanner scanner = new Scanner(new File(DataFilePath))) {
            while (scanner.hasNextLine()) {
                String programData = scanner.nextLine();
                System.out.println(programData);
            }

            System.out.println("Program data loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Error loading program data: " + e.getMessage());
        }
    }


    private static void View_Remaining_Burger_Stock(){
        // Function to the remaining amount of burgers in the stock.
        System.out.println("Remaining burgers in stock : "+ burgersInStock);
    }


    private static void Add_Burger_To_Stock(){
        // Function to add the new stock of burgers to the stock.
        System.out.println("Enter the number of burgers to be added to the stock:");
        int burgersToAdd = scanner.nextInt();
        scanner.nextLine();

        if (burgersToAdd < 0) {
            System.out.println("Invalid number of burgers.");
            return;
        }

        burgersInStock += burgersToAdd;
        System.out.println(burgersToAdd + " Burgers added to stock. Total burgers in stock: " + burgersInStock);
    }


}

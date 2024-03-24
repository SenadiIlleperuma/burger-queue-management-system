import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    static final int[] QueueCapacity = new int[]{2, 3, 5};
    static final int MaxCashiers = 3;
    static final int BurgerLimit = 50;
    private static int currQueueIndex = 0;
    static final String DataFilePath = "Foodies Fav Food Center.txt";
    private static boolean shouldexit = false;
    static List<FoodQueue> queues = new ArrayList();
    static List<Customer> waitingQueue = new ArrayList();
    static int burgersInStock = 50;
    static Scanner scanner;



    public static void main(String[] args) {
        initializing_Queue();
        mainMenu();
    }
    //Initializing the queues
    private static void initializing_Queue() {
        for(int i = 0; i < 3; ++i) {
            queues.add(new FoodQueue(QueueCapacity[i]));
        }

    }
    //Displays the menu options and handles user input

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
        System.out.println("110 or IFQ: Print income of each queue");
        System.out.println("999 or EXT: Exit the Program");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println();
        System.out.println("Enter your choice:");

        //Handle user input based on the chosen option
        switch (scanner.nextLine().toUpperCase()) {
            case "100":
            case "VFQ":
                View_All_Queues();
                break;
            case "101":
            case "VEQ":
                View_All_Empty_Queues();
                break;
            case "102":
            case "ACQ":
                Add_Customer_To_Queue();
                break;
            case "103":
            case "RCQ":
                Remove_Customer_From_Queue();
                break;
            case "104":
            case "PCQ":
                Remove_Served_Customer();
                break;
            case "105":
            case "VCS":
                View_Customer_Sorted();
                break;
            case "106":
            case "SPD":
                Store_Program_Data();
                break;
            case "107":
            case "LPD":
                Load_Program_Data();
                break;
            case "108":
            case "STK":
                View_Remaining_Burger_Stock();
                break;
            case "109":
            case "AFS":
                Add_Burger_To_Stock();
                break;
            case "110":
            case "IFQ":
                Print_Income_Of_Each_Queue();
                break;
            case "999":
            case "EXT":
                System.out.println("Exiting the program...");
                shouldexit = true;
                break;
            default:
                System.out.println("Invalid choice.Please try again.");
        }

        System.out.println();
        if (!shouldexit) {
            mainMenu();
        }

    }

    private static void View_All_Queues() {
        FoodQueue.displayQueueVisual(queues);
    }

    private static void View_All_Empty_Queues() {
        boolean emptyQueuesFound = false;

        for(int i = 0; i < queues.size(); ++i) {
            FoodQueue queue = (FoodQueue)queues.get(i);
            if (queue.isEmpty()) {
                System.out.println("Cashier " + (i + 1) + ": Queue is empty");
                emptyQueuesFound = true;
            }
        }

        if (!emptyQueuesFound) {
            System.out.println("No empty queues found.");
        }

    }

    private static void Add_Customer_To_Queue() {
        FoodQueue minQueue = findQueueWithMinimumLength(queues);
        if (minQueue != null && !minQueue.isFull()) {
            System.out.println("Enter the customer's first name:");
            String firstName = scanner.nextLine();
            System.out.println("Enter the customer's last name:");
            String lastName = scanner.nextLine();
            System.out.println("Enter the number of burgers required:");
            int burgersRequired = scanner.nextInt();
            scanner.nextLine();
            Customer customer = new Customer(firstName, lastName, burgersRequired);
            minQueue.enqueue(customer);
            burgersInStock -= burgersRequired;
            System.out.println("Customer " + firstName + " " + lastName + " added to queue " + (queues.indexOf(minQueue) + 1));
            if (burgersInStock <= 10) {
                System.out.println("Low stock");
            }

            currQueueIndex = (currQueueIndex + 1) % queues.size();
        } else {
            System.out.println("All queues are full. Customers are being added to the waiting list.");
            addCustomerToWaitingList();
        }
    }

    public static FoodQueue findQueueWithMinimumLength(List<FoodQueue> queues) {
        FoodQueue minQueue = null;
        int minLength = Integer.MAX_VALUE;

        for(int i = 0; i < queues.size(); ++i) {
            int queueIndex = (currQueueIndex + i) % queues.size();
            FoodQueue queue = (FoodQueue)queues.get(queueIndex);
            int queueLength = queue.getQueueLength();
            if (queueLength < minLength && !queue.isFull()) {
                minLength = queueLength;
                minQueue = queue;
            }
        }

        return minQueue;
    }

    private static void addCustomerToWaitingList() {
        System.out.println("Enter the customer's first name:");
        String firstName = scanner.nextLine();
        System.out.println("Enter the customer's last name:");
        String lastName = scanner.nextLine();
        System.out.println("Enter the number of burgers required:");
        int burgersRequired = scanner.nextInt();
        scanner.nextLine();
        Customer customer = new Customer(firstName, lastName, burgersRequired);
        waitingQueue.add(customer);
        System.out.println("Customer " + firstName + " " + lastName + " added to the waiting list.");
    }

    private static void Remove_Customer_From_Queue() {
        System.out.println("Enter the cashier number (1-3):");
        int cashierNumber = scanner.nextInt();
        scanner.nextLine();
        if (cashierNumber >= 1 && cashierNumber <= 3) {
            int queueIndex = cashierNumber - 1;
            if (((FoodQueue)queues.get(queueIndex)).isEmpty()) {
                System.out.println("Queue " + cashierNumber + " is empty.");
            } else {
                Customer servedCustomer = ((FoodQueue)queues.get(queueIndex)).dequeue();
                if (servedCustomer != null) {
                    PrintStream var10000 = System.out;
                    String var10001 = servedCustomer.getFullName();
                    var10000.println("Served customer " + var10001 + " removed from queue " + cashierNumber);
                    burgersInStock -= servedCustomer.getBurgersRequired();
                    if (!waitingQueue.isEmpty()) {
                        Customer nextCustomer = (Customer)waitingQueue.remove(0);
                        ((FoodQueue)queues.get(queueIndex)).enqueue(nextCustomer);
                        var10000 = System.out;
                        var10001 = nextCustomer.getFullName();
                        var10000.println("Next customer " + var10001 + " added to queue " + cashierNumber);
                    }
                } else {
                    System.out.println("Failed to remove served customer from queue " + cashierNumber);
                }

            }
        } else {
            System.out.println("Invalid cashier number.");
        }
    }

    private static void Remove_Served_Customer() {
        System.out.println("Enter the cashier number (1-3):");
        int cashierNumber = scanner.nextInt();
        scanner.nextLine();
        if (cashierNumber >= 1 && cashierNumber <= 3) {
            int queueIndex = cashierNumber - 1;
            if (((FoodQueue)queues.get(queueIndex)).isEmpty()) {
                System.out.println("Queue " + cashierNumber + " is empty.");
            } else {
                int position = scanner.nextInt();
                scanner.nextLine();
                if (position >= 1 && position <= ((FoodQueue)queues.get(queueIndex)).getCustomers().size()) {
                    boolean removed = ((FoodQueue)queues.get(queueIndex)).dequeue(position);
                    if (removed) {
                        System.out.println("Customer removed from queue " + cashierNumber);
                        if (!waitingQueue.isEmpty()) {
                            Customer nextCustomer = (Customer)waitingQueue.remove(0);
                            ((FoodQueue)queues.get(queueIndex)).enqueue(nextCustomer);
                            PrintStream var10000 = System.out;
                            String var10001 = nextCustomer.getFullName();
                            var10000.println("Next customer " + var10001 + " added to queue " + cashierNumber);
                        }
                    } else {
                        System.out.println("Failed to remove customer from queue " + cashierNumber);
                    }

                } else {
                    System.out.println("Invalid position.");
                }
            }
        } else {
            System.out.println("Invalid cashier number.");
        }
    }

    private static void View_Customer_Sorted() {
        Set<Customer> uniqueCustomers = new HashSet();
        Iterator var1 = queues.iterator();

        while(var1.hasNext()) {
            FoodQueue queue = (FoodQueue)var1.next();
            uniqueCustomers.addAll(queue.getCustomers());
        }

        List<Customer> sortedCustomers = new ArrayList(uniqueCustomers);
        Collections.sort(sortedCustomers, Comparator.comparing(Customer::getFullName));
        System.out.println("Customers sorted in alphabetical order:");
        Iterator var5 = sortedCustomers.iterator();

        while(var5.hasNext()) {
            Customer customer = (Customer)var5.next();
            System.out.println(customer.getFullName());
        }

    }

    private static void Store_Program_Data() {
        try {
            FileWriter fileWriter = new FileWriter("Foodies Fav Food Center.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("Burgers in stock: " + burgersInStock);

            for(int i = 0; i < queues.size(); ++i) {
                FoodQueue queue = (FoodQueue)queues.get(i);
                printWriter.println("Queue " + (i + 1) + ": " + queue.getQueueString());
            }

            printWriter.println();
            printWriter.close();
            System.out.println("Program data stored successfully.");
        } catch (IOException var4) {
            System.out.println("Error storing program data: " + var4.getMessage());
        }

    }

    private static void Load_Program_Data() {
        try {
            File file = new File("Foodies Fav Food Center.txt");
            Scanner fileScanner = new Scanner(file);

            while(true) {
                while(fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    if (line.startsWith("Burgers in stock: ")) {
                        burgersInStock = Integer.parseInt(line.split(": ")[1]);
                    } else if (line.startsWith("Queue ")) {
                        int queueIndex = Integer.parseInt(line.substring(6, 7)) - 1;
                        FoodQueue queue = (FoodQueue)queues.get(queueIndex);
                        String customersData = line.substring(9);
                        if (!customersData.isEmpty()) {
                            String[] customers = customersData.split(";");
                            String[] var7 = customers;
                            int var8 = customers.length;

                            for(int var9 = 0; var9 < var8; ++var9) {
                                String customer = var7[var9];
                                String[] customerDetails = customer.split(",");
                                String fullName = customerDetails[0];
                                int burgersRequired = Integer.parseInt(customerDetails[1]);
                                String[] nameParts = fullName.split(" ");
                                String firstName = nameParts[0];
                                String lastName = nameParts[1];
                                Customer newCustomer = new Customer(firstName, lastName, burgersRequired);
                                queue.enqueue(newCustomer);
                            }
                        }
                    }
                }

                fileScanner.close();
                System.out.println("Program data loaded successfully.");
                break;
            }
        } catch (FileNotFoundException var18) {
            System.out.println("Error loading program data: " + var18.getMessage());
        }

    }

    private static void View_Remaining_Burger_Stock() {
        System.out.println("Remaining burgers in stock: " + burgersInStock);
    }

    private static void Add_Burger_To_Stock() {
        System.out.println("Enter the number of burgers to add to stock:");
        int burgersToAdd = scanner.nextInt();
        scanner.nextLine();
        if (burgersToAdd < 0) {
            System.out.println("Invalid number of burgers. Cannot add a negative quantity.");
        } else {
            burgersInStock += burgersToAdd;
            System.out.println("" + burgersToAdd + " burgers added to stock. Total burgers in stock: " + burgersInStock);
        }
    }

    private static void Print_Income_Of_Each_Queue() {
        System.out.println("Income of each queue:");

        for(int i = 0; i < queues.size(); ++i) {
            FoodQueue queue = (FoodQueue)queues.get(i);
            int queueIncome = queue.getCustomers().stream().mapToInt(Customer::getBurgersRequired).sum() * 650;
            System.out.println("Queue " + (i + 1) + ": " + queueIncome);
        }

    }

    static {
        scanner = new Scanner(System.in);
    }
}

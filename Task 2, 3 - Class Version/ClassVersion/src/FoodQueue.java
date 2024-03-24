import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FoodQueue {
    private int capacity;
    private Queue<Customer> customers;
    private int size;

    public FoodQueue(int capacity) {
        this.capacity = capacity;
        this.customers = new LinkedList();
    }

    public int getSize() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.customers.isEmpty();
    }

    public boolean isFull() {
        return this.customers.size() == this.capacity;
    }

    public void enqueue(Customer customer) {
        this.customers.add(customer);
        PrintStream var10000 = System.out;
        String var10001 = customer.getFirstName();
        var10000.println("Customer " + var10001 + " " + customer.getLastName() + " added to the queue.");
    }

    public boolean dequeue(int position) {
        if (position >= 1 && position <= this.customers.size()) {
            int count = 1;

            for(Iterator<Customer> iterator = this.customers.iterator(); iterator.hasNext(); ++count) {
                iterator.next();
                if (count == position) {
                    iterator.remove();
                    return true;
                }
            }
        }

        return false;
    }

    public Customer dequeue() {
        return (Customer)this.customers.poll();
    }

    public String toString() {
        return this.customers.toString();
    }

    public String getQueueString() {
        StringBuilder sb = new StringBuilder();
        Iterator var2 = this.customers.iterator();

        while(var2.hasNext()) {
            Customer customer = (Customer)var2.next();
            sb.append(customer.getFullName()).append(",").append(customer.getBurgersRequired()).append(";");
        }

        return sb.toString();
    }

    public List<Customer> getCustomers() {
        return new ArrayList(this.customers);
    }

    public static void displayQueueVisual(List<FoodQueue> queues) {
        System.out.println("*****************");
        System.out.println("* Cashiers *");
        System.out.println("*****************");
        int maxQueueCapacity = getMaxQueueCapacity(queues);

        for(int j = 0; j < maxQueueCapacity; ++j) {
            for(int i = 0; i < 3; ++i) {
                if (j < Main.QueueCapacity[i]) {
                    FoodQueue queue = (FoodQueue)queues.get(i);
                    if (j < queue.customers.size()) {
                        System.out.print("O ");
                    } else {
                        System.out.print("X ");
                    }
                } else {
                    System.out.print("  ");
                }
            }

            System.out.println();
        }

    }

    private static int getMaxQueueCapacity(List<FoodQueue> queues) {
        int maxCapacity = 0;
        Iterator var2 = queues.iterator();

        while(var2.hasNext()) {
            FoodQueue queue = (FoodQueue)var2.next();
            if (queue.getCapacity() > maxCapacity) {
                maxCapacity = queue.getCapacity();
            }
        }

        return maxCapacity;
    }

    private int getCapacity() {
        return this.capacity;
    }

    public static FoodQueue findQueueWithMinimumLength(List<FoodQueue> queues) {
        FoodQueue minQueue = null;
        int minLength = Integer.MAX_VALUE;
        Iterator var3 = queues.iterator();

        while(var3.hasNext()) {
            FoodQueue queue = (FoodQueue)var3.next();
            int queueLength = queue.customers.size();
            if (queueLength < minLength) {
                minLength = queueLength;
                minQueue = queue;
            }
        }

        return minQueue;
    }

    public int getQueueLength() {
        return 0;
    }
}

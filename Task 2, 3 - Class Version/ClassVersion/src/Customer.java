public class Customer {
    String firstName;
    String lastName;
    int burgersRequired;

    public Customer(String firstName, String lastName, int burgersRequired) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.burgersRequired = burgersRequired;
    }



    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public int getBurgersRequired() {
        return this.burgersRequired;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public String getFullDetails() {
        return this.firstName + " " + this.lastName + " " + this.burgersRequired;
    }

    public int compareTo(Customer other) {
        return this.getFullName().compareTo(other.getFullName());
    }
}

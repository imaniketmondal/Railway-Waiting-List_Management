import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class railwayticket {

    private static final int TOTAL_SEATS = 3; 
    private static final String BOOKED_FILE = "booked_tickets.txt";
    private static final String WAITING_LIST_FILE_FCFS = "waiting_list-fcfs.txt";
    private static final String WAITING_LIST_FILE_AGE = "waiting_list-age.txt";
      private static final String GENERAL_WAITING_LIST_FILE = "general_waiting_list.txt";

    private static Map<String, Passenger> bookedSeats;
    private static LinkedList<Passenger> waitingListFcfs; 
    private static PriorityQueue<Passenger> waitingListAgePriority;
    private static LinkedList<Passenger> generalWaitingList;
    private static int nextSeatNumber = 1;

    public static void main(String[] args) {
        bookedSeats = loadBookedTickets();
        waitingListFcfs = loadWaitingListFcfs();
        waitingListAgePriority = loadWaitingListAgePriority();
        generalWaitingList = loadGeneralWaitingList();

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nTrain Ticket Booking System");
            System.out.println("1. Book Ticket");
            System.out.println("2. Cancel Ticket");
            System.out.println("3. Display Booked Tickets");
            System.out.println("4. Display Waiting Lists");
            System.out.println("5. Display General Waiting List");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    bookTicket();
                    break;
                case 2:
                    cancelTicket();
                    break;
                case 3:
                    displayBookedTickets();
                    break;
                case 4:
                    displayWaitingLists();
                    break;
                case 5:
                    displayGeneralWaitingList();
                    break;
                case 6:
                    System.out.println("Exiting the system...");
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 6);

        scanner.close();
        saveBookedTickets();
        saveWaitingListFcfs();
        saveWaitingListAgePriority();
        saveGeneralWaitingList();
    }

    private static void bookTicket() {
        Scanner scanner = new Scanner(System.in);

        if (bookedSeats.size() < TOTAL_SEATS) {
            System.out.print("Enter passenger name: ");
            String name = scanner.nextLine();
            System.out.print("Enter passenger age: ");
            int age = scanner.nextInt();
            System.out.print("Enter passenger gender (M/F): ");
            String gender = scanner.next().toUpperCase();

            bookedSeats.put(name, new Passenger(name, age, gender).setSeatNumber(nextSeatNumber++));
            System.out.println("Ticket booked successfully for " + name + ". Seat number: " + bookedSeats.get(name).getSeatNumber());
            saveBookedTickets();
        } else {
            System.out.println("wait");
            System.out.print("Enter passenger name: ");
            String name = scanner.nextLine();
            System.out.print("Enter passenger age: ");
            int age = scanner.nextInt();
            System.out.print("Enter passenger gender (M/F): ");
            String gender = scanner.next().toUpperCase();

            generalWaitingList.add(new Passenger(name, age, gender));
            saveGeneralWaitingList(); 

            if (age > 60) {
                waitingListAgePriority.add(new Passenger(name, age, gender));
            } else {
                waitingListFcfs.add(new Passenger(name, age, gender));
            }
            saveWaitingListFcfs();
            saveWaitingListAgePriority();
        }
    }

    private static void cancelTicket() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter passenger name to cancel: ");
        String name = scanner.nextLine();

        if (bookedSeats.containsKey(name)) {
            Passenger passenger = bookedSeats.remove(name);
                        System.out.println("Ticket canceled for " + name + ". Seat number: " + passenger.getSeatNumber());
             nextSeatNumber--;
              generalWaitingList.remove(new Passenger(name, passenger.getAge(), passenger.getGender()));
            saveGeneralWaitingList();

            Passenger nextPassenger = null;
            if (!waitingListAgePriority.isEmpty()) {
                nextPassenger = waitingListAgePriority.poll();
            } else if (!waitingListFcfs.isEmpty()) {
                nextPassenger = waitingListFcfs.poll();
            }

            if (nextPassenger != null) {
                bookedSeats.put(nextPassenger.getName(), nextPassenger.setSeatNumber(bookedSeats.size() + 1));
                System.out.println(nextPassenger.getName() + " from the waiting list has been assigned seat number " + nextPassenger.getSeatNumber());
            }

            saveBookedTickets();
            saveWaitingListFcfs();
            saveWaitingListAgePriority();
        } else {
            System.out.println(name + " does not have a booked ticket.");
        }
    }

    private static void displayBookedTickets() {
        if (!bookedSeats.isEmpty()) {
            System.out.println("\nBooked Tickets:");
            for (Passenger passenger : bookedSeats.values()) {
                System.out.println("Passenger: " + passenger.getName() + ", Age: " + passenger.getAge() + ", Gender: " + passenger.getGender() + ", Seat number: " + passenger.getSeatNumber());
            }
        } else {
            System.out.println("There are no booked tickets currently.");
        }
    }

    private static void displayWaitingLists() {
        System.out.println("\nWaiting Lists:");
        if (!waitingListAgePriority.isEmpty()) {
            System.out.println("  - Age Priority (over 60):");
            for (Passenger passenger : waitingListAgePriority) {
                System.out.println("    Passenger: " + passenger.getName() + ", Age: " + passenger.getAge() + ", Gender: " + passenger.getGender());
            }
        } else {
            System.out.println("    - Age Priority (over 60): Empty");
        }
        if (!waitingListFcfs.isEmpty()) {
            System.out.println("  - FCFS (under 60):");
            for (Passenger passenger : waitingListFcfs) {
                System.out.println("    Passenger: " + passenger.getName() + ", Age: " + passenger.getAge() + ", Gender: " + passenger.getGender());
            }
        } else {
            System.out.println("    - FCFS (under 60): Empty");
        }
    }

    private static Map<String, Passenger> loadBookedTickets() {
        Map<String, Passenger> loadedSeats = new HashMap<>();
        try (Scanner scanner = new Scanner(new File(BOOKED_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                loadedSeats.put(parts[0], new Passenger(parts[0], Integer.parseInt(parts[1]), parts[2]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Booked tickets file not found. Creating a new one.");
       
        }
        return loadedSeats;
    }

    private static void saveBookedTickets() {
        try (FileWriter writer = new FileWriter(BOOKED_FILE)) {
            for (Passenger passenger : bookedSeats.values()) {
                writer.write(passenger.getName() + "," + passenger.getSeatNumber() + "," + passenger.getGender() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static LinkedList<Passenger> loadWaitingListFcfs() {
        LinkedList<Passenger> loadedList = new LinkedList<>();
        try (Scanner scanner = new Scanner(new File(WAITING_LIST_FILE_FCFS))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                loadedList.add(new Passenger(parts[0], Integer.parseInt(parts[1]), parts[2]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("FCFS waiting list file not found. Creating a new one.");
           
        }
        return loadedList;
    }

    private static void saveWaitingListFcfs() {
        try (FileWriter writer = new FileWriter(WAITING_LIST_FILE_FCFS)) {
            for (Passenger passenger : waitingListFcfs) {
                writer.write(passenger.getName() + "," + passenger.getAge() + "," + passenger.getGender() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static PriorityQueue<Passenger> loadWaitingListAgePriority() {
        PriorityQueue<Passenger> loadedList = new PriorityQueue<>((p1, p2) -> Integer.compare(p2.getAge(), p1.getAge())); // Sorts by age descending (older first)
        try (Scanner scanner = new Scanner(new File(WAITING_LIST_FILE_AGE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                loadedList.add(new Passenger(parts[0], Integer.parseInt(parts[1]), parts[2]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Age priority waiting list file not found. Creating a new one.");}
       
        return loadedList;
    }

    private static void saveWaitingListAgePriority() {
        try (FileWriter writer = new FileWriter(WAITING_LIST_FILE_AGE)) {
            for (Passenger passenger : waitingListAgePriority) {
                writer.write(passenger.getName() + "," + passenger.getAge() + "," + passenger.getGender() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static LinkedList<Passenger> loadGeneralWaitingList() {
    LinkedList<Passenger> loadedList = new LinkedList<>();
    try (Scanner scanner = new Scanner(new File(GENERAL_WAITING_LIST_FILE))) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            loadedList.add(new Passenger(parts[0], Integer.parseInt(parts[1]), parts[2]));
        }
    } catch (FileNotFoundException e) {
        System.out.println("General waiting list file not found. Creating a new one.");}
   
    return loadedList;
}

private static void saveGeneralWaitingList() {
    try (FileWriter writer = new FileWriter(GENERAL_WAITING_LIST_FILE)) {
        for (Passenger passenger : generalWaitingList) {
            writer.write(passenger.getName() + "," + passenger.getAge() + "," + passenger.getGender() + "\n");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    private static void displayGeneralWaitingList() {
        System.out.println("\nGeneral Waiting List (Informational):");
        if (!generalWaitingList.isEmpty()) {
            for (Passenger passenger : generalWaitingList) {
                System.out.println("    Passenger: " + passenger.getName() + ", Age: " + passenger.getAge() + ", Gender: " + passenger.getGender());
            }
        } else {
            System.out.println("    - Empty");
        }
    }

}

class Passenger {
    private String name;
    private int age;
    private String gender;
    private int seatNumber;

    public Passenger(String name, int age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.seatNumber=-1;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public Passenger setSeatNumber(int seatNumber) {
         if (seatNumber <= 0) {
            throw new IllegalArgumentException("Seat number must be positive.");
        }
        this.seatNumber = seatNumber;
        return this;
    }
}
            

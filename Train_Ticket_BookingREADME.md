# Train Ticket Booking System README

This is a simple Train Ticket Booking System implemented in Java. It allows users to book tickets, cancel tickets, and manage waiting lists for a train with a limited number of seats.

## Features

- **Booking Tickets**: Users can book tickets by providing their name, age, and gender. If seats are available, the ticket is booked; otherwise, the user is added to the waiting list based on age priority (for passengers over 60) or first-come-first-serve basis (for passengers under 60).
  
- **Canceling Tickets**: Users can cancel their booked tickets, which automatically assigns the next passenger on the waiting list a seat if available.

- **Displaying Booked Tickets**: Users can view the currently booked tickets, including passenger details and seat numbers.

- **Displaying Waiting Lists**: Users can view the waiting lists, both based on age priority and first-come-first-serve basis.

- **Displaying General Waiting List**: Users can view the general waiting list, which includes passengers who couldn't get a seat due to unavailability.

## File Structure

- `railwayticket.java`: The main Java file containing the implementation of the Train Ticket Booking System.
  
- `Passenger.java`: A helper class representing a passenger with attributes such as name, age, gender, and seat number.

- `booked_tickets.txt`: A text file storing information about currently booked tickets.

- `waiting_list-fcfs.txt`: A text file storing information about passengers on the first-come-first-serve waiting list.

- `waiting_list-age.txt`: A text file storing information about passengers on the age priority waiting list.

- `general_waiting_list.txt`: A text file storing information about passengers on the general waiting list.

## Usage

1. **Compile**: Compile the `railwayticket.java` file using a Java compiler.

2. **Run**: Run the compiled Java program (`railwayticket.class`).

3. **Follow Instructions**: Follow the instructions printed on the console to perform actions such as booking tickets, canceling tickets, and viewing passenger information.

## Note

- Make sure to have all necessary input files (`booked_tickets.txt`, `waiting_list-fcfs.txt`, `waiting_list-age.txt`, `general_waiting_list.txt`) in the same directory as the Java files.

- Ensure proper file permissions for reading from and writing to the text files.

## Contributors

-Aniket Mondal, 
Siddharth Arya,
Prashant Kunwar

## License

This project is licensed under the MIT License. Feel free to use, modify, and distribute it as per the terms of the license.

---
Feel free to add any additional sections or details as needed for your specific project!

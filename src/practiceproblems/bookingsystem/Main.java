package src.practiceproblems.bookingsystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Setup Movies
        Movie movie1 = new Movie();
        movie1.id = "M1";
        movie1.title = "Interstellar";

        Movie movie2 = new Movie();
        movie2.id = "M2";
        movie2.title = "The Dark Knight";

        // 2. Setup Showtimes
        Showtime s1 = new Showtime("S1", movie1, LocalDateTime.now().plusDays(1));
        Showtime s2 = new Showtime("S2", movie2, LocalDateTime.now().plusHours(5));

        // 3. Setup Theatre
        List<Showtime> showtimes = new ArrayList<>(Arrays.asList(s1, s2));
        Theatre theatre1 = new Theatre("T1", showtimes);

        // 4. Initialize Booking System
        List<Theatre> theatres = new ArrayList<>(List.of(theatre1));
        BookingSystem system = new BookingSystem(theatres);

        System.out.println("--- Starting Tests ---");

        // TEST 1: Successful Booking
        String confirmation1 = "";
        try {
            System.out.println("Test 1: Booking A1, A2 for Interstellar...");
            confirmation1 = system.book("S1", Arrays.asList("A1", "A2"));
            System.out.println("Success! Confirmation: " + confirmation1);
        } catch (Exception e) {
            System.err.println("Unexpected failure: " + e.getMessage());
        }

        // TEST 2: Double Booking (Should fail)
        try {
            System.out.println("\nTest 2: Attempting to book A2, A3 (A2 already taken)...");
            system.book("S1", Arrays.asList("A2", "A3"));
        } catch (InvalidRequestException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }

        // TEST 3: Search Movie
        System.out.println("\nTest 3: Searching for 'Dark'...");
        List<Showtime> results = system.getShowtimesByMovie("Dark");
        results.forEach(s -> System.out.println("Found: " + s.getMovie().getTitle() + " at " + s.getShowtime()));

        // TEST 4: Cancel and Re-book
        try {
            System.out.println("\nTest 4: Canceling reservation " + confirmation1);
            system.cancel(confirmation1);
            System.out.println("Cancellation successful. Re-booking A2...");
            String confirmation2 = system.book("S1", Arrays.asList("A2"));
            System.out.println("Success! New Confirmation: " + confirmation2);
        } catch (Exception e) {
            System.err.println("Cancellation test failed: " + e.getMessage());
        }

        System.out.println("\n--- All Tests Completed ---");
    }
}
package src.practiceproblems.bookingsystem;

import java.util.List;
import java.util.UUID;


public class Reservation {
    String confirmationId;
    List<String> seats;
    Showtime showtime;

    public Reservation(List<String> seats, Showtime showtime){
        this.confirmationId = UUID.randomUUID().toString();
        this.seats = seats;
        this.showtime = showtime;
    }

    public String getConfirmationId() {
        return confirmationId;
    }

    public List<String> getSeats() {
        return seats;
    }

    public Showtime getShowtime() {
        return showtime;
    }
}

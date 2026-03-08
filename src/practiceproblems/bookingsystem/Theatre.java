package src.practiceproblems.bookingsystem;

import java.util.List;

public class Theatre {
    String id;
    List<Showtime> showtimes;

    public Theatre(String id, List<Showtime> showtimes){
        this.id = id;
        this.showtimes = showtimes;
    }

    public List<Showtime> getShowtimes() {
        return showtimes;
    }
}

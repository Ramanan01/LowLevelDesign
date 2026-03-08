package src.practiceproblems.bookingsystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingSystem {
    List<Theatre> theatres;
    Map<String, List<Showtime>> movieIdToShowtimesMap;
    Map<String, Movie> movieIdToMovieMap;
    Map<String, Reservation> reservationMap;
    Map<String, Showtime> showtimeIdMap;

    public BookingSystem(List<Theatre> theatres) {
        this.theatres = theatres;
        this.movieIdToShowtimesMap = new HashMap<>();
        this.movieIdToMovieMap = new HashMap<>();
        this.reservationMap = new HashMap<>();
        this.showtimeIdMap = new HashMap<>();

        for(Theatre theatre: theatres){
            for(Showtime showtime: theatre.getShowtimes()){
                showtimeIdMap.put(showtime.getId(), showtime);
                movieIdToShowtimesMap.computeIfAbsent(showtime.getMovie().getId(), (k) -> new ArrayList<>());
                movieIdToShowtimesMap.get(showtime.getMovie().getId()).add(showtime);
                movieIdToMovieMap.put(showtime.getMovie().getId(), showtime.getMovie());
            }
        }
    }

    public List<Showtime> getShowtimesByMovie(String searchKey){
        if(searchKey == null || searchKey.isEmpty()){
            throw new InvalidRequestException("Search key is invalid");
        }

        List<Showtime> matchingShowtimes = new ArrayList<>();

        for(Map.Entry<String, Movie> entry: movieIdToMovieMap.entrySet()){
            if(entry.getValue().getTitle().contains(searchKey)){
                for(Showtime showtime: movieIdToShowtimesMap.get(entry.getKey())){
                    if(showtime.getShowtime().isAfter(LocalDateTime.now())){
                        matchingShowtimes.add(showtime);
                    }
                }
            }
        }

        return matchingShowtimes;
    }

    public List<Showtime> getShowtimesByTheatre(Theatre theatre){
        if(theatre == null){
            return new ArrayList<>();
        }

        List<Showtime> showtimesAtTheatre = new ArrayList<>();

        for(Showtime showtime: theatre.getShowtimes()){
            if(showtime.getShowtime().isAfter(LocalDateTime.now())){
                showtimesAtTheatre.add(showtime);
            }
        }

        return  showtimesAtTheatre;
    }

    public String book(String showtimeId, List<String> seats){

        if(!showtimeIdMap.containsKey(showtimeId)){
            throw new InvalidRequestException("Invalid showtime");
        }

        Showtime showtime = showtimeIdMap.get(showtimeId);
        Reservation reservation = showtime.book(seats);

        reservationMap.put(reservation.getConfirmationId(), reservation);

        return reservation.getConfirmationId();
    }

    public boolean cancel(String confirmationId){
        if(confirmationId==null || confirmationId.isEmpty()){
            throw new InvalidRequestException("Invalid confirmation id");
        }

        if(!reservationMap.containsKey(confirmationId)){
            throw new InvalidRequestException("Cannot find reservation");
        }

        Showtime showtime = reservationMap.get(confirmationId).getShowtime();
        showtime.cancel(confirmationId);

        reservationMap.remove(confirmationId);

        return true;
    }
}

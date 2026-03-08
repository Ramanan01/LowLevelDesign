package src.practiceproblems.bookingsystem;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Showtime {
    String id;
    Movie movie;
    LocalDateTime showtime;
    Map<String, Reservation> reservationMap;

    public Showtime(String id, Movie movie, LocalDateTime showtime){
        this.id = id;
        this.movie = movie;
        this.showtime = showtime;
        this.reservationMap = new HashMap<>();
    }

    public Reservation book(List<String> seats){
        if(seats == null || seats.isEmpty()){
            throw new InvalidRequestException("Seats are empty");
        }
        synchronized (this){
            for(String seat: seats){
                if(checkValidSeat(seat)) {
                    for (Reservation reservation : reservationMap.values()) {
                        if (reservation.getSeats().contains(seat)) {
                            throw new InvalidRequestException("Seat already booked");
                        }
                    }
                }
                else{
                    throw new InvalidRequestException("Invalid seat");
                }
            }

            Reservation reservation = new Reservation(seats, this);
            reservationMap.put(reservation.getConfirmationId(), reservation);
            return reservation;
        }
    }

    public void cancel(String confirmationId){
        if(confirmationId==null || confirmationId.isEmpty()){
            throw new InvalidRequestException("Invalid confirmation Id");
        }

        synchronized (this){
            if(!reservationMap.containsKey(confirmationId)){
                throw new InvalidRequestException("Invalid confirmation Id");
            }


            reservationMap.remove(confirmationId);
        }

    }

    private boolean checkValidSeat(String seat){
        seat = seat.toLowerCase();
        if(seat.length() < 2){
            return false;
        }

        if(!(seat.charAt(0)>='a' && seat.charAt(0)<='z')){
            return false;
        }

        String seatNo = "";

        for(int i=1;i<seat.length();i++){
            if(!(seat.charAt(i)>='0' && seat.charAt(i)<='9')){
                return false;
            }
            seatNo += seat.charAt(i);
        }

        return Integer.parseInt(seatNo) <= 20 && Integer.parseInt(seatNo)>=1;
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDateTime getShowtime() {
        return showtime;
    }

    public String getId() {
        return id;
    }
}

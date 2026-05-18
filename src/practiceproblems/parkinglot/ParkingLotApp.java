package src.practiceproblems.parkinglot;

import java.util.*;
import java.util.concurrent.locks.*;

enum SpotType {
    CAR,
    TRUCK,
    BIKE
}

enum VehicleType {
    CAR,
    TRUCK,
    BIKE
}

class NoSpotAvailableException extends  Exception {
    NoSpotAvailableException() {
        super("No available and compatible spot");
    }
}

class ParkingSpot {
    private final String spotId;
    private final SpotType spotType;

    public ParkingSpot(String spotId, SpotType spotType) {
        this.spotId = spotId;
        this.spotType = spotType;
    }

    public String getSpotId() {
        return spotId;
    }

    public SpotType getSpotType() {
        return spotType;
    }
}

class Ticket {
    private final String ticketId;
    private final String spotId;
    private final long entryTimeMillis;

    public Ticket(String spotId) {
        this.ticketId = UUID.randomUUID().toString();
        this.spotId = spotId;
        this.entryTimeMillis = System.currentTimeMillis();
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getSpotId() {
        return spotId;
    }

    public long getEntryTimeMillis() {
        return entryTimeMillis;
    }
}

class ParkingLot {
    List<ParkingSpot> spots;
    Map<String, Ticket> activeTickets;
    Set<String> occupiedSpots;
    int hourlyRate;
    ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public ParkingLot(List<ParkingSpot> spots, int hourlyRate) {
        this.spots = spots;
        activeTickets = new HashMap<>();
        occupiedSpots = new HashSet<>();
        this.hourlyRate = hourlyRate;
    }

    public Ticket enter(VehicleType vehicleType) throws NoSpotAvailableException {
        if(vehicleType == null) {
            throw new IllegalArgumentException("Invalid vehicle type");
        }

        while(true) {
            ParkingSpot spot = findAvailableSpot(vehicleType);

            if (spot == null) {
                throw new NoSpotAvailableException();
            }

            rwLock.writeLock().lock();
            try{
                if(!occupiedSpots.contains(spot.getSpotId())) {
                    occupiedSpots.add(spot.getSpotId());
                    Ticket ticket = new Ticket(spot.getSpotId());
                    activeTickets.put(ticket.getTicketId(), ticket);
                    return ticket;
                }
            }
            finally{
                rwLock.writeLock().unlock();
            }
        }
    }

    public int exit(String ticketId) {
        if(ticketId == null || ticketId.isEmpty()) {
            throw new IllegalArgumentException("Ticket id is empty");
        }

        rwLock.writeLock().lock();

        try{
            Ticket ticket = activeTickets.get(ticketId);
            if(ticket == null) {
                throw new IllegalArgumentException("Invalid ticket ID");
            }
            int fee = calculateFee(ticket);
            occupiedSpots.remove(ticket.getSpotId());
            activeTickets.remove(ticketId);
            return fee;
        }
        finally{
            rwLock.writeLock().unlock();
        }
    }

    private int calculateFee(Ticket ticket) {
        long startTime = ticket.getEntryTimeMillis();
        long endTime = System.currentTimeMillis();

        long hours = (endTime-startTime)/(1000 * 60 * 60);
        if((endTime-startTime)%(1000 * 60 * 60)!=0) {
            hours++;
        }

        return (int) hours * hourlyRate;
    }

    private ParkingSpot findAvailableSpot(VehicleType vehicleType){
        rwLock.readLock().lock();
        try {
            SpotType spotType = getSpotTypeForVehicle(vehicleType);
            if (spotType == null) {
                throw new IllegalArgumentException("Incompatible vehicle type");
            }

            for (ParkingSpot spot : spots) {
                if (spotType.equals(spot.getSpotType()) && !occupiedSpots.contains(spot.getSpotId())) {
                    return spot;
                }
            }

            return null;
        }
        finally {
            rwLock.readLock().unlock();
        }
    }

    public SpotType getSpotTypeForVehicle(VehicleType vehicleType) {
        switch(vehicleType) {
            case CAR: return SpotType.CAR;
            case TRUCK: return SpotType.TRUCK;
            case BIKE: return SpotType.BIKE;
            default: return null;
        }
    }
}

public class ParkingLotApp {
}

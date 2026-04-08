package src.practiceproblems.MeetingRooms;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class Meeting {
    private final String id;
    private final int startTime;
    private final int endTime;
    private final String roomId;

    public Meeting(String id, int startTime, int endTime, String roomId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomId = roomId;
    }

    public String getId() {
        return id;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public String getRoomId() {
        return roomId;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Meeting meeting) {
            return this.getStartTime() == meeting.getStartTime() && this.getEndTime() == meeting.getEndTime() && Objects.equals(meeting.getId(), this.getId()) && Objects.equals(meeting.getRoomId(), this.getRoomId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.roomId, this.startTime, this.endTime);
    }
}

class Room {
    private final String roomId;
    private final TreeSet<Meeting> meetings;

    public Room(String roomId) {
        this.roomId = roomId;
        this.meetings = new TreeSet<>((a, b) -> {
            int comp = Integer.compare(a.getStartTime(), b.getStartTime());
            if (comp != 0) {
                return comp;
            }
            return a.getId().compareTo(b.getId());
        });
    }

    public synchronized boolean book(Meeting newMeeting) {
        Meeting floor = meetings.floor(newMeeting);

        if(floor != null && floor.getEndTime() > newMeeting.getStartTime()){
            return false;
        }

        Meeting ceil = meetings.ceiling(newMeeting);

        if(ceil != null && ceil.getStartTime() < newMeeting.getEndTime()) {
            return false;
        }

        meetings.add(newMeeting);

        return true;
    }

    public synchronized boolean cancel(Meeting meeting){
        return meetings.remove(meeting);
    }
}

class ReservationSystem {
    private final Map<String, Room> rooms;
    private final Map<String, Meeting> meetings;

    public ReservationSystem(List<String> roomIds){
        meetings = new ConcurrentHashMap<>();
        rooms = new ConcurrentHashMap<>();

        for(String roomId: roomIds){
            rooms.computeIfAbsent(roomId, Room::new);
        }
    }

    public String bookRoom(String roomId, int startTime, int endTime){
        if(!rooms.containsKey(roomId)){
            throw new IllegalArgumentException("Invalid room");
        }

        if(endTime<=startTime){
            throw new IllegalArgumentException("End time has to be greater than start time");
        }

        Meeting newMeeting = new Meeting(UUID.randomUUID().toString(), startTime, endTime, roomId);

        if(rooms.get(roomId).book(newMeeting)){
            meetings.put(newMeeting.getId(), newMeeting);
            return newMeeting.getId();
        }
        System.out.println("Booking failed: Room " + roomId + " is busy.");
        return null;
    }

    public void cancelMeeting(String meetingId) {
        if(!meetings.containsKey(meetingId)){
            throw new IllegalArgumentException("Invalid meeting id");
        }

        Meeting meeting = meetings.get(meetingId);
        String roomId = meeting.getRoomId();

        if(!rooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Invalid room");
        }

        Room room = rooms.get(roomId);
        room.cancel(meeting);
        meetings.remove(meetingId);
    }
}

public class MeetingRoomsApplication {
    public static void main(String[] args) {
        // Initialize the system with a few rooms
        List<String> roomNames = Arrays.asList("Boardroom", "WarRoom", "ZenDen");
        ReservationSystem system = new ReservationSystem(roomNames);

        System.out.println("--- Starting Booking Tests ---");

        // 1. Successful Booking
        String m1 = system.bookRoom("Boardroom", 10, 12);
        printStatus("Booking m1 (10-12) in Boardroom", m1 != null);

        // 2. Overlap Booking (Starts during m1)
        String m2 = system.bookRoom("Boardroom", 11, 13);
        printStatus("Booking m2 (11-13) in Boardroom (Should fail)", m2 == null);

        // 3. Edge Case: Start time exactly at another's end time (Should succeed)
        String m3 = system.bookRoom("Boardroom", 12, 13);
        printStatus("Booking m3 (12-13) in Boardroom (Should succeed)", m3 != null);

        // 4. Booking in a different room (Should succeed)
        String m4 = system.bookRoom("WarRoom", 10, 12);
        printStatus("Booking m4 (10-12) in WarRoom", m4 != null);

        System.out.println("\n--- Starting Cancellation Tests ---");

        // 5. Cancel m1 and re-book in that slot
        system.cancelMeeting(m1);
        System.out.println("Canceled m1.");

        String m5 = system.bookRoom("Boardroom", 10, 11);
        printStatus("Booking m5 (10-11) in Boardroom after canceling m1", m5 != null);

        // 6. Invalid Room Error Check
        try {
            system.bookRoom("Kitchen", 9, 10);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }
    }

    private static void printStatus(String message, boolean success) {
        String status = success ? "[SUCCESS]" : "[FAILED]";
        System.out.println(status + " " + message);
    }
}

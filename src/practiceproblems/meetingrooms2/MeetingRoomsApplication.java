package src.practiceproblems.meetingrooms2;

import java.util.*;
import java.util.concurrent.*;

class Meeting {
    public final int startTime;
    public final int endTime;
    public final String meetingId;
    public final String roomId;

    Meeting(int startTime, int endTime, String meetingId, String roomId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.meetingId = meetingId;
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if(!(o instanceof Meeting)){
            return false;
        }

        Meeting meeting = (Meeting) o;

        return meetingId.equals(meeting.meetingId);

    }

    @Override
    public int hashCode() {
        return Objects.hash(meetingId);
    }
}

class Room {
    private final TreeSet<Meeting> meetings;
    private final String roomId;

    public Room(String roomId) {
        this.roomId = roomId;
        meetings = new TreeSet<>((a, b) -> {
            return Integer.compare(a.startTime, b.startTime);
        });
    }

    public synchronized boolean addMeeting(Meeting newMeeting) {
        if(newMeeting.endTime<=newMeeting.startTime) {
            throw new IllegalArgumentException("End time should be greater than start time");
        }

        Meeting floor = meetings.floor(newMeeting);

        if(floor != null && floor.endTime > newMeeting.startTime) {
            return false;
        }

        Meeting ceil = meetings.ceiling(newMeeting);

        if(ceil != null && ceil.startTime < newMeeting.endTime) {
            return false;
        }

        meetings.add(newMeeting);

        return true;
    }

    public synchronized void cancelMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }
}

class MeetingRooms {
    Map<String, Room> rooms;
    Map<String, Meeting> meetings;

    public MeetingRooms(String[] roomIds) {
        rooms = new ConcurrentHashMap<>();
        meetings = new ConcurrentHashMap<>();

        for(String roomId: roomIds) {
            rooms.put(roomId, new Room(roomId));
        }
    }

    public void book(String roomId, int startTime, int endTime) {
        if(roomId==null || roomId.isEmpty()) {
            throw new IllegalArgumentException("Invalid roomId");
        }

        if(startTime<0) {
            throw new IllegalArgumentException("Time cannot be negative");
        }

        if(!rooms.containsKey(roomId)){
            throw new IllegalArgumentException("Room not valid");
        }

        Meeting newMeeting = new Meeting(startTime, endTime, UUID.randomUUID().toString(), roomId);

        Room room = rooms.get(roomId);

        if(room.addMeeting(newMeeting)) {
            System.out.println("Meeting added successfully with ID " + newMeeting.meetingId);
            meetings.put(newMeeting.meetingId, newMeeting);
        }
        else{
            System.out.println("Unable to accommodate successfully");
        }

    }

    public void cancelMeeting(String meetingId) {
        if(meetingId==null || meetingId.isEmpty()) {
            throw new IllegalArgumentException("Invalid meeting id");
        }

        Meeting meeting = meetings.remove(meetingId);

        if(meeting==null) {
            throw new IllegalArgumentException("Invalid meeting ID");
        }

        Room room = rooms.get(meeting.roomId);
        room.cancelMeeting(meeting);
    }

}

public class MeetingRoomsApplication {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String[] roomIds = {"room1", "room2", "room3"};
        MeetingRooms meetingRooms = new MeetingRooms(roomIds);

        System.out.println("Meeting Room System Started");
        System.out.println("Commands:");
        System.out.println("book <roomId> <startTime> <endTime>");
        System.out.println("cancel <meetingId>");
        System.out.println("exit");

        while(true) {
            try {
                System.out.print("> ");

                String input = sc.nextLine().trim();

                if(input.isEmpty()) {
                    continue;
                }

                String[] parts = input.split("\\s+");

                String command = parts[0];

                switch(command.toLowerCase()) {

                    case "book":
                        if(parts.length != 4) {
                            System.out.println(
                                    "Usage: book <roomId> <startTime> <endTime>"
                            );
                            break;
                        }

                        String roomId = parts[1];
                        int start = Integer.parseInt(parts[2]);
                        int end = Integer.parseInt(parts[3]);

                        meetingRooms.book(roomId, start, end);
                        break;

                    case "cancel":
                        if(parts.length != 2) {
                            System.out.println(
                                    "Usage: cancel <meetingId>"
                            );
                            break;
                        }

                        meetingRooms.cancelMeeting(parts[1]);
                        System.out.println("Meeting cancelled");
                        break;

                    case "exit":
                        System.out.println("Exiting...");
                        sc.close();
                        return;

                    default:
                        System.out.println(
                                "Unknown command"
                        );
                }

            } catch(Exception e) {
                System.out.println(
                        "Error: " + e.getMessage()
                );
            }
        }
    }
}
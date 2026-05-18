package src.practiceproblems.railwaystationapp;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class TimeInterval {
    LocalTime startTime;
    LocalTime endTime;

    public TimeInterval(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}

class Train {
    TimeInterval timeInterval;
    String trainId;
    String trainName;

    public Train(TimeInterval timeInterval, String trainName) {
        this.timeInterval = timeInterval;
        this.trainName = trainName;
        this.trainId = UUID.randomUUID().toString();
    }

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public String getTrainId() {
        return trainId;
    }

    public String getTrainName() {
        return trainName;
    }
}

class Platform {
    private static int platformCount = 0;
    int platformNo;
    TreeSet<Train> trains;

    public Platform() {
        platformNo = ++platformCount;
        trains = new TreeSet<>(Comparator.comparing((Train t) -> t.getTimeInterval().getStartTime()).thenComparing(Train::getTrainId));
    }

    public boolean addTrainIfNotClashing(Train train) {
        if(train == null) {
            throw new IllegalArgumentException("Train is null");
        }

        Train floor = trains.floor(train);

        if(floor!=null && floor.getTimeInterval().getEndTime().isAfter(train.getTimeInterval().getStartTime())){
            return false;
        }

        Train ceiling = trains.ceiling(train);

        if(ceiling!=null && ceiling.getTimeInterval().getStartTime().isBefore(train.getTimeInterval().getEndTime())){
            return false;
        }

        trains.add(train);

        return true;
    }

    public Train getTrainAtTime(LocalTime time) {
        if(time == null) {
            throw new IllegalArgumentException("Invalid time");
        }

        TimeInterval timeInterval = new TimeInterval(time, time);
        Train train = new Train(timeInterval, "dummyName");

        Train candidate = trains.floor(train);

        if(candidate!=null && candidate.getTimeInterval().getEndTime().isAfter(train.getTimeInterval().getStartTime())){
            return candidate;
        }

        return null;
    }

    public static Integer getPlatformCount() {
        return platformCount;
    }

    public int getPlatformNo() {
        return platformNo;
    }
}

class RailwayStation {
    private final List<Platform> platforms;

    public RailwayStation() {
        platforms = new ArrayList<>();
    }

    public void addPlatform() {
        Platform platform = new Platform();
        platforms.add(platform);
    }

    public int addTrain(String start, String end, String trainName) {
        if(start == null || start.isEmpty() || end==null || end.isEmpty()) {
            throw new IllegalArgumentException("Invalid start and end time");
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("H:mm");
        LocalTime startTime = LocalTime.parse(start, dateTimeFormatter);
        LocalTime endTime = LocalTime.parse(end, dateTimeFormatter);

        if(startTime.isAfter(endTime)) {
            throw new IllegalArgumentException(
                    "Invalid interval"
            );
        }

        TimeInterval newInterval = new TimeInterval(startTime, endTime);
        Train newTrain = new Train(newInterval, trainName);
        for(Platform platform: platforms) {
            if(platform.addTrainIfNotClashing(newTrain)) {
                return platform.getPlatformNo();
            }
        }

        return -1;
    }

    public Train getTrainAtPlatform(
            int platformNo,
            String time){

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("H:mm");
        LocalTime queryTime =
                LocalTime.parse(time, dateTimeFormatter);

        for(Platform platform :
                platforms){

            if(platform.getPlatformNo()
                    == platformNo){

                return platform
                        .getTrainAtTime(
                                queryTime
                        );
            }
        }

        return null;
    }
}


public class RailwayStationApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        RailwayStation station =
                new RailwayStation();

        System.out.println("""
                Commands:
                addPlatform
                addTrain <start(HH:mm)> <end(HH:mm)> <trainName>
                getTrain <platformNo> <time(HH:mm)>
                exit
                """);

        while(true){

            System.out.print("> ");

            String input =
                    sc.nextLine().trim();

            String[] parts =
                    input.split("\\s+");

            if(parts.length==0){
                continue;
            }

            String command =
                    parts[0].toLowerCase();

            try{

                switch(command){

                    case "addplatform":

                        station.addPlatform();

                        System.out.println(
                                "Platform added"
                        );
                        break;


                    case "addtrain":

                        if(parts.length<4){

                            System.out.println(
                                    "Usage: addTrain start end trainName"
                            );

                            break;
                        }

                        int assignedPlatform =
                                station.addTrain(
                                        parts[1],
                                        parts[2],
                                        parts[3]
                                );

                        if(assignedPlatform==-1){

                            System.out.println(
                                    "No platform available"
                            );
                        }
                        else{

                            System.out.println(
                                    "Train assigned to platform "
                                            + assignedPlatform
                            );
                        }

                        break;


                    case "gettrain":

                        if(parts.length<3){

                            System.out.println(
                                    "Usage: getTrain platformNo time"
                            );

                            break;
                        }

                        int platformNo =
                                Integer.parseInt(
                                        parts[1]
                                );

                        Train train =
                                station
                                        .getTrainAtPlatform(
                                                platformNo,
                                                parts[2]
                                        );

                        if(train==null){

                            System.out.println(
                                    "No train at this time"
                            );
                        }
                        else{

                            System.out.println(
                                    "Train : "
                                            +
                                            train.getTrainName()
                            );
                        }

                        break;


                    case "exit":

                        sc.close();

                        return;


                    default:

                        System.out.println(
                                "Invalid command"
                        );
                }

            }
            catch(Exception e){

                System.out.println(
                        "Error : "
                                + e.getMessage()
                );
            }
        }
    }
}
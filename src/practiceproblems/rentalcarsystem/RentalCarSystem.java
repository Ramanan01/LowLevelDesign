package src.practiceproblems.rentalcarsystem;
import java.util.*;
import java.time.*;
import java.time.temporal.*;
import java.time.format.DateTimeFormatter;

class Car{
    private final String licensePlate;
    private final int costPerDay;
    private final int freeKmsPerDay;
    private final int costPerKm;

    public Car(String licensePlate, int costPerDay, int freeKmsPerDay, int costPerKm) {
        this.licensePlate = licensePlate;
        this.costPerDay = costPerDay;
        this.freeKmsPerDay = freeKmsPerDay;
        this.costPerKm = costPerKm;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getCostPerDay() {
        return costPerDay;
    }

    public int getFreeKmsPerDay() {
        return freeKmsPerDay;
    }

    public int getCostPerKm() {
        return costPerKm;
    }

}

class Booking {
    private final String orderId;
    private final String carLicensePlate;
    private final LocalDate fromDate;
    private LocalDate tillDate;
    private int initialOdometerReading;
    private int finalOdometerReading;

    public Booking(String orderId, String carLicensePlate, LocalDate fromDate, LocalDate tillDate) {
        this.orderId = orderId;
        this.carLicensePlate = carLicensePlate;

        this.fromDate = fromDate;
        this.tillDate = tillDate;
    }

    public String getCarLicensePlate() {
        return carLicensePlate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getTillDate() {
        return tillDate;
    }

    public void setTillDate(LocalDate tillDate) {
        this.tillDate = tillDate;
    }

    public void setInitialOdometerReading(int initialOdometerReading) {
        this.initialOdometerReading = initialOdometerReading;
    }

    public void setFinalOdometerReading(int finalOdometerReading) {
        this.finalOdometerReading = finalOdometerReading;
    }

    public int calculateFinalCostAndReturn(LocalDate endDate, Car car) {
        LocalDate finalDate = this.tillDate.isAfter(endDate) ? tillDate : endDate;
        int days = (int) ChronoUnit.DAYS.between(fromDate, finalDate) + 1;
        int totalKms = finalOdometerReading - initialOdometerReading;
        int freeKms = days * car.getFreeKmsPerDay();
        int extraKms = Math.max(totalKms - freeKms, 0);

        int totalCost = 0;
        totalCost += extraKms * car.getCostPerKm();
        totalCost += days * car.getCostPerDay();

        return totalCost;
    }
}

class CarRentalService {
    private final Map<String, Car> cars;
    private final Map<String, Booking> orders;
    private final Map<String, TreeSet<Booking>> bookingsPerCar;
    private final DateTimeFormatter formatter;

    public CarRentalService() {
        cars = new HashMap<>();
        bookingsPerCar = new HashMap<>();
        orders = new HashMap<>();
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    public void addCar(String licensePlate, int costPerDay, int freeKmsPerDay, int costPerKm) {
        if(licensePlate==null || licensePlate.isEmpty()) {
            throw new IllegalArgumentException("License plate is invalid");
        }

        if(cars.containsKey(licensePlate)){
            return;
        }

        Car car = new Car(licensePlate, costPerDay, freeKmsPerDay, costPerKm);
        cars.put(licensePlate, car);
    }

    public boolean bookCar(String orderId, String carLicensePlate, String fromDate, String tillDate) {
        if(carLicensePlate==null || carLicensePlate.isEmpty()) {
            throw new IllegalArgumentException("License plate is invalid");
        }

        if(orderId==null || orderId.isEmpty()) {
            throw new IllegalArgumentException("OrderId is invalid");
        }

        if(orders.containsKey(orderId)) {
            return false;
        }

        if(!cars.containsKey(carLicensePlate)) {
            return false;
        }

        LocalDate fromDateNew = LocalDate.parse(fromDate, formatter);
        LocalDate tillDateNew = LocalDate.parse(tillDate, formatter);

        if(tillDateNew.isBefore(fromDateNew)){
            throw new IllegalArgumentException("Till date has to be after start date");
        }

        bookingsPerCar.putIfAbsent(carLicensePlate, new TreeSet<>((a, b) -> {
            if(a.getFromDate().isBefore(b.getFromDate())){
                return -1;
            }
            else if(a.getFromDate().isAfter(b.getFromDate())){
                return 1;
            }
            else{
                return 0;
            }
        }));

        Booking newBooking = new Booking(orderId, carLicensePlate, fromDateNew, tillDateNew);

        Booking ceiling = bookingsPerCar.get(carLicensePlate).ceiling(newBooking);
        if(ceiling!=null && !ceiling.getFromDate().isAfter(newBooking.getTillDate())) {
            return false;
        }

        Booking floor = bookingsPerCar.get(carLicensePlate).floor(newBooking);
        if(floor!=null && !floor.getTillDate().isBefore(newBooking.getFromDate())) {
            return false;
        }

        bookingsPerCar.get(carLicensePlate).add(newBooking);
        orders.put(orderId, newBooking);
        return true;
    }

    public void startTrip(String orderId, int odometerReading) {
        if(orderId==null || orderId.isEmpty()) {
            throw new IllegalArgumentException("OrderId is invalid");
        }

        if(!orders.containsKey(orderId)) {
            return;
        }
        orders.get(orderId).setInitialOdometerReading(odometerReading);
    }

    public int endTrip(String orderId, int finalOdometerReading, String endDate) {
        if(orderId==null || orderId.isEmpty()) {
            throw new IllegalArgumentException("OrderId is invalid");
        }

        if(!orders.containsKey(orderId)) {
            return -1;
        }
        Booking booking = orders.get(orderId);
        booking.setFinalOdometerReading(finalOdometerReading);
        Car car = cars.get(booking.getCarLicensePlate());
        LocalDate endDateNew = LocalDate.parse(endDate, formatter);
        int cost = booking.calculateFinalCostAndReturn(endDateNew, car);
        bookingsPerCar.get(car.getLicensePlate()).remove(booking);
        booking.setTillDate(endDateNew);
        bookingsPerCar.get(car.getLicensePlate()).add(booking);
        return cost;
    }
}

public class RentalCarSystem {
}

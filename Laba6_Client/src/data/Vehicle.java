package data;
import Client.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Class of elements which is contained in collection
 */
public class Vehicle implements Comparable<Vehicle>, Serializable {
    private int id;
    private String name;
    private Coordinates coordinates;
    private LocalDate creationDate;
    private float enginePower;
    private int capacity;
    private VehicleType vehicleType;
    private FuelType fuelType;
    private boolean inFile;
    private  float x;
    private  double y;
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    @Override
    public int compareTo(Vehicle vehicle) {
        if (enginePower > vehicle.getEnginePower())
            return 1;
        if (enginePower < vehicle.getEnginePower())
            return -1;
        return name.compareTo(vehicle.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return id == vehicle.id &&
                Float.compare(vehicle.enginePower, enginePower) == 0 &&
                capacity == vehicle.capacity &&
                Objects.equals(name, vehicle.name) &&
                Objects.equals(coordinates, vehicle.coordinates) &&
                Objects.equals(creationDate, vehicle.creationDate) &&
                vehicleType == vehicle.vehicleType &&
                fuelType == vehicle.fuelType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, enginePower, capacity, vehicleType, fuelType);
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vehicle(String name, Coordinates coordinates, float enginePower, int capacity, VehicleType type, FuelType fuelType) {
        creationDate = LocalDate.now();
        this.name = name;
        this.coordinates = coordinates;
        this.enginePower = enginePower;
        this.capacity = capacity;
        this.vehicleType = type;
        this.fuelType = fuelType;
        this.x = coordinates.getX();
        this.y = coordinates.getY();
        inFile = false;
    }
    public Vehicle(int id, String name, Coordinates coordinates, float enginePower, int capacity, VehicleType vehicleType, FuelType fuelType) {
        this.id = id;
        creationDate = LocalDate.now();
        this.name = name;
        this.coordinates = coordinates;
        this.enginePower = enginePower;
        this.capacity = capacity;
        this.vehicleType = vehicleType;
        this.fuelType = fuelType;
        this.x = coordinates.getX();
        this.y = coordinates.getY();
        inFile = false;
    }

    public Vehicle(int id, String name, Coordinates coordinates, LocalDate creationDate,
                   float enginePower, int capacity, VehicleType vehicleType, FuelType fuelType) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.enginePower = enginePower;
        this.capacity = capacity;
        this.vehicleType = vehicleType;
        this.fuelType = fuelType;
        this.x = coordinates.getX();
        this.y = coordinates.getY();
        inFile = true;
    }

    public boolean isInFile() {
        return inFile;
    }

    public void setInFile(boolean inFile) {
        this.inFile = inFile;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public float getEnginePower() {
        return enginePower;
    }

    public int getCapacity() {
        return capacity;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    @Override
    public String toString() {
        return "Vehicle " +
                "id:" + id +
                ", name: " + name +
                ", coordinates: " + coordinates.toString() +
                ", creationDate: " + creationDate +
                ", enginePower: " + enginePower +
                ", capacity: " + capacity +
                ", vehicleType: " + vehicleType +
                ", fuelType: " + fuelType;
    }
    public Vehicle setAll(Vehicle v){
        name = v.getName();
        coordinates = v.getCoordinates();
        creationDate = v.getCreationDate();
        enginePower = v.getEnginePower();
        capacity = v.getCapacity();
        vehicleType = v.getVehicleType();
        fuelType = v.getFuelType();
        inFile = v.isInFile();
        x = v.getCoordinates().getX();
        y = v.getCoordinates().getY();
        return this;
    }

    public float getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

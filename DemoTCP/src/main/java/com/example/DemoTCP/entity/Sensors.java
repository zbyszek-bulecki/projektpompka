package com.example.DemoTCP.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Sensors {
    @Id
    @GeneratedValue
    private int id;
    private String address;
    private float voltage;
    private int soil;
    private float light;
    private float temp;
    private float pressure;
    private int water;
    private LocalDateTime date;

    public Sensors() {
        this.date = LocalDateTime.now();
    }

    public Sensors(String address, float voltage, int soil, float light, float temp, float pressure, int water) {
        this.address = address;
        this.voltage = voltage;
        this.soil = soil;
        this.light = light;
        this.temp = temp;
        this.pressure = pressure;
        this.water = water;
        this.date = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getVoltage() {
        return voltage;
    }

    public void setVoltage(float voltage) {
        this.voltage = voltage;
    }

    public int getSoil() {
        return soil;
    }

    public void setSoil(int soil) {
        this.soil = soil;
        this.date = LocalDateTime.now();
    }

    public float getLight() {
        return light;
    }

    public void setLight(float light) {
        this.light = light;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensors sensors = (Sensors) o;
        return id == sensors.id && Float.compare(sensors.voltage, voltage) == 0 && soil == sensors.soil && Float.compare(sensors.light, light) == 0 && Float.compare(sensors.temp, temp) == 0 && Float.compare(sensors.pressure, pressure) == 0 && water == sensors.water && Objects.equals(address, sensors.address) && Objects.equals(date, sensors.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, voltage, soil, light, temp, pressure, water, date);
    }

    @Override
    public String toString() {
        return "Sensors{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", voltage=" + voltage +
                ", soil=" + soil +
                ", light=" + light +
                ", temp=" + temp +
                ", pressure=" + pressure +
                ", water=" + water +
                ", date=" + date +
                '}';
    }
}

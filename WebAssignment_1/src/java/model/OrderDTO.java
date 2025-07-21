/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

/**
 *
 * @author an0other
 */
public class OrderDTO {
    private int id;
    private String username;
    private double total_price;
    private String status;
    private LocalDateTime date;
    public OrderDTO() {
    }

    public OrderDTO(int id, String username, double total_price, String status, LocalDateTime date) {
        this.id = id;
        this.username = username;
        this.total_price = total_price;
        this.status = status;
        this.date = date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public double getTotal_price() {
        return total_price;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}

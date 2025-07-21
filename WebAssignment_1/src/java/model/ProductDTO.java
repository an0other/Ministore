/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;
import java.time.LocalDateTime;

/**
 *
 * @author an0other
 */
public class ProductDTO {
    private String id;
    private String name;
    private double price;
    private String img_url;
    private String category;
    private LocalDateTime date;
    private boolean status;
    private int quantity;
    public ProductDTO() {
    }

    public ProductDTO(String id, String name, double price, String img_url, String category, LocalDateTime date, boolean status, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.img_url = img_url;
        this.category = category;
        this.date = date;
        this.status = status;
        this.quantity = quantity;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getCategory() {
        return category;
    }


    public boolean isStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

}

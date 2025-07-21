/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author an0other
 */
public class OrderItemDTO {
    private int id;
    private String product_id;
    private int quantity;
    private double price;
    private int order_id;
    private String product_name;
    private String img;
    public OrderItemDTO() {
    }

    public OrderItemDTO(int id, String product_id, int quantity, double price, int order_id, String product_name) {
        this.id = id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.price = price;
        this.order_id = order_id;
        this.product_name = product_name;
    }

    
    public OrderItemDTO(int id, String product_id, int quantity, double price, int order_id) {
        this.id = id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.price = price;
        this.order_id = order_id;
    }

    public OrderItemDTO(int id, String product_id, int quantity, double price, int order_id, String product_name, String img) {
        this.id = id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.price = price;
        this.order_id = order_id;
        this.product_name = product_name;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
    
}

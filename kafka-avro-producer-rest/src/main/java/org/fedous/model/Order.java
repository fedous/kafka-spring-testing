package org.fedous.model;

import lombok.Data;

import java.util.List;
@Data
public class Order {

    private String id;
    private OrderType orderType;
    private String orderDate;
    private List<OrderItem> orderItem;
    private OrderStatus orderStatus;
    private boolean isOrderPaid;
    private double totalCost;
}

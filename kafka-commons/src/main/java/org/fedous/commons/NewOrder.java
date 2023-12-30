package org.fedous.commons;

import lombok.Data;

import java.util.List;

@Data
public class NewOrder {

    private String customerName;
    private List<Long> productIds;

}

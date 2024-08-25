package com.entity;

public enum DiscountType {
    FIRST_ORDER_DISCOUNT(5),
    VIP_DISCOUNT(10),
    NONE(0);

    private final Integer discountPercentage;

    DiscountType(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public double applyDiscount(double price) {
        double discountAmount = (price * this.discountPercentage) / 100;
        return price - discountAmount;
    }
}
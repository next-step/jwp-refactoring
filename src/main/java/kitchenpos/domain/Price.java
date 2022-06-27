package kitchenpos.domain;

import java.math.BigDecimal;

public class Price implements Comparable<Price> {
    public static Price ZERO = Price.valueOf(0);
    private BigDecimal price;

    public Price() {
    }

    public Price(BigDecimal price) {
        this.price = price;
    }

    public static Price valueOf(double price) {
        return new Price(BigDecimal.valueOf(price));
    }

    public Price multiply(Price o) {
        return new Price(this.price.multiply(o.price));
    }

    public Price add(Price o) {
        return new Price(this.price.add(o.price));
    }

    @Override
    public int compareTo(Price o) {
        return this.price.compareTo(o.price);
    }
}

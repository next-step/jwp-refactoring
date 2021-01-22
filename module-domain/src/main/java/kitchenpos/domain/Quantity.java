package kitchenpos.domain;

public class Quantity {
    private final long amount;

    public Quantity(long amount) {
        this.amount = amount;
    }

    public long longValue() {
        return amount;
    }

    public static Quantity of(long amount) {
        return new Quantity(amount);
    }
    public Money of(Money price) {
        return new Money(price.multiply(amount));
    }
}

package kitchenpos.common;

public class Price {

    public static final String PRICE_MINIMUM_EXCEPTION_MESSAGE = "0원 이하일 수 없습니다.";
    private int price;

    public Price(int price) {
        validate(price);
        this.price = price;
    }

    private void validate(int price) {
        if (price < 0) {
            throw new IllegalArgumentException(PRICE_MINIMUM_EXCEPTION_MESSAGE);
        }
        this.price = price;
    }
}

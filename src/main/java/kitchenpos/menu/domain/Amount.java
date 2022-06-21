package kitchenpos.menu.domain;

public class Amount {
    private int price;
    private int quantity;

    public Amount(int price, int quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public int calculateAmount() {
        return price * quantity;
    }
}

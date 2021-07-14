package kitchenpos.menu.domain;

import kitchenpos.generic.price.domain.Price;
import kitchenpos.generic.quantity.domain.Quantity;

public class MenuDetailOption {
    private Long productId;
    private String name;
    private Price price;
    private Quantity quantity;

    public MenuDetailOption(Long productId, String name, Price price, Quantity quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}

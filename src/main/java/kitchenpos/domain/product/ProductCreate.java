package kitchenpos.domain.product;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;

public class ProductCreate {
    private Name name;
    private Price price;

    public ProductCreate(String name, Price price) {
        this(new Name(name), price);
    }

    public ProductCreate(Name name, Price price) {
        this.name = name;
        this.price = price;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}

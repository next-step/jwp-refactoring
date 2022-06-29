package kitchenpos.fixture;

import kitchenpos.menu.domain.Name;
import kitchenpos.menu.domain.Price;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class TestProductFactory {

    public static Product create(String name, int price) {
        return create(null, name, price);
    }


    public static Product create(Long id, String name, int price) {
        return new Product(id, new Name(name), new Price(price));
    }
}

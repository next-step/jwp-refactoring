package kitchenpos.fixture;

import kitchenpos.embeddableEntity.Name;
import kitchenpos.embeddableEntity.Price;
import kitchenpos.product.domain.Product;

public class TestProductFactory {

    public static Product create(String name, int price) {
        return create(null, name, price);
    }


    public static Product create(Long id, String name, int price) {
        return new Product(id, new Name(name), new Price(price));
    }
}

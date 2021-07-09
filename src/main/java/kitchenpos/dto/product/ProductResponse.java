package kitchenpos.dto.product;

import kitchenpos.domain.product.Price;
import kitchenpos.domain.product.Product;

public class ProductResponse {

    private final Long id;
    private final String name;
    private final Price price;

    public ProductResponse(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName().getValue(), product.getPrice());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}

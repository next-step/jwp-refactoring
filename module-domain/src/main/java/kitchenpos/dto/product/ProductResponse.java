package kitchenpos.dto.product;

import kitchenpos.domain.product.Price;
import kitchenpos.domain.product.Product;

public class ProductResponse {
    private Long id;
    private String name;
    private long price;

    public ProductResponse() {}

    public ProductResponse(Long id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), mapToLongValue(product.getPrice()));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
    
    private static long mapToLongValue(Price price) {
        return price.getPrice().longValue();
    }
}

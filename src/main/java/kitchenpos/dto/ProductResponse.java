package kitchenpos.dto;

import kitchenpos.domain.ProductEntity;

public class ProductResponse {
    private Long id;
    private String name;
    private Long price;

    public ProductResponse(Long id, String name, long unitPrice) {
        this.id = id;
        this.name = name;
        this.price = unitPrice;
    }

    protected ProductResponse() {
    }

    public static ProductResponse of(ProductEntity product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getUnitPrice()
        );
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
}

package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

public class ProductDto {
    private Long id;
    private String name;
    private Long price;

    protected ProductDto() { }

    public ProductDto(String name, Long price) {
        this(null, name, price);
    }

    public ProductDto(Long id, String name, Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductDto of(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getPrice().getValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }
}

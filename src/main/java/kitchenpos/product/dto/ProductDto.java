package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

public class ProductDto {
    private Long id;
    private String name;
    private Integer price;

    public ProductDto() { }

    public ProductDto(Long id, String name, Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductDto of(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getPrice().intValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}

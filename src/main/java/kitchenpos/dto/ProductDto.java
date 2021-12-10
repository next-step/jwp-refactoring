package kitchenpos.dto;

import kitchenpos.domain.Price;

public class ProductDto {
    private Long id;
    private String name;
    private Price price;

    protected ProductDto() {
    }

    private ProductDto(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductDto of(Long id, String name, Price price) {
        return new ProductDto(id, name, price);
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Price getPrice() {
        return this.price;
    }
}

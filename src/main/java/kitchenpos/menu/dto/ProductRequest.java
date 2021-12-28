package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Product;

public class ProductRequest {
    private String name;
    private Long price;
    
    private ProductRequest() {
    }

    private ProductRequest(String name, Long price) {
        this.name = name;
        this.price = price;
    }
    
    public static ProductRequest of(String name, Long price) {
        return new ProductRequest(name, price);
    }
    
    public Product toProduct() {
        return Product.of(name, price);
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

}

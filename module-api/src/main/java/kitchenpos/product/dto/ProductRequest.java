package kitchenpos.product.dto;


import kitchenpos.moduledomain.product.Product;

public class ProductRequest {

    private Long id;
    private String name;
    private Long price;

    public ProductRequest(Long id, String name, Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
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

    public Product toProduct() {
        return Product.of(name, price);
    }
}

package kitchenpos.dto;

import kitchenpos.domain.Product;

public class ProductRequest {
    private String name;
    private int price;

    public ProductRequest() {

    }

    public ProductRequest(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product(name, price);
    }

    public static ProductRequest of(Product product) {
        return new ProductRequest(product.getName(), product.getPrice().intValue());
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}

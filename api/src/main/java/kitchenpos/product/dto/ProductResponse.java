package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

public class ProductResponse {
    private Long id;
    private String name;
    private int price;

    public ProductResponse() {}

    private ProductResponse(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Long id, String name, int price) {
        return new ProductResponse(id, name, price);
    }

    public static ProductResponse of(Product product) {
        String name = product.getName();
        return new ProductResponse(product.getId(), name, product.getPrice().amountToInt());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}

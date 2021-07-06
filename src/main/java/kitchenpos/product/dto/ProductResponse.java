package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.id(), product.name(), product.price());
    }

    public static List<ProductResponse> ofList(List<Product> products) {
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            productResponses.add(new ProductResponse(product.id(), product.name(), product.price()));
        }
        return productResponses;
    }

    public ProductResponse(Long id, String name, BigDecimal price) {
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

    public BigDecimal getPrice() {
        return price;
    }

}

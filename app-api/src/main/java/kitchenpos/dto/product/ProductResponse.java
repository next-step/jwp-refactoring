package kitchenpos.dto.product;

import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;

import static java.util.stream.Collectors.*;

import java.util.List;

public class ProductResponse {
    private Long id;
    private String name;
    private int price;

    public ProductResponse(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price.getPrice();
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public static List<ProductResponse> ofList(List<Product> products) {
        return products.stream()
                .map(ProductResponse::of)
                .collect(toList());
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

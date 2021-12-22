package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Product;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.*;

/**
 * packageName : kitchenpos.dto
 * fileName : ProductResponse
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public static List<ProductResponse> ofList(List<Product> products) {
        return products.stream().map(ProductResponse::of).collect(toList());
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

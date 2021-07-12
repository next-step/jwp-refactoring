package kitchenpos.dto.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.PriceResponse;

public class ProductResponse {

    private Long id;
    private String name;
    private PriceResponse price;

    public ProductResponse() {
    }

    @JsonCreator
    public ProductResponse(Long id, String name, PriceResponse price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName().getValue(), PriceResponse.of(product.getPrice().getValue()));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PriceResponse getPrice() {
        return price;
    }
}

package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Quantity;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class MenuProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long quantity;

    public static MenuProductResponse from(Product product, Quantity quantity) {
        MenuProductResponse response = new MenuProductResponse();

        response.id = product.getId();
        response.name = product.getName();
        response.price = product.getPrice().getValue();
        response.quantity = quantity.getValue();

        return response;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

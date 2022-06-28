package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;

import java.math.BigDecimal;
import java.util.List;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private List<MenuProductResponse> products;

    public static MenuResponse from(Menu menu) {
        MenuResponse response = new MenuResponse();

        response.id = menu.getId();
        response.name = menu.getName();
        response.price = menu.getPrice().getValue();
        response.products = menu.getMenuProducts().toProductResponses();

        return response;
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

    public List<MenuProductResponse> getProducts() {
        return products;
    }
}

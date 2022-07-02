package kitchenpos.service.menu.dto;

import kitchenpos.domain.menu.Menu;

import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {
    private Long id;
    private String name;
    private long price;
    private List<MenuProductResponse> products;

    public MenuResponse(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.products = menu.getMenuProducts().stream().map(MenuProductResponse::new).collect(Collectors.toList());
    }

    public MenuResponse() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public List<MenuProductResponse> getProducts() {
        return products;
    }
}

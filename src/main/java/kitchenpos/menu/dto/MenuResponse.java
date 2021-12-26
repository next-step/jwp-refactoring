package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private List<MenuProductResponse> products;

    private MenuResponse(Long id, String name, BigDecimal price, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.products = MenuProductResponse.ofList(menuProducts);
    }

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getMenuPrice(), menu.getMenuProducts());
    }

    public static List<MenuResponse> ofList(List<Menu> menus) {
        return menus.stream()
                .map(MenuResponse::of)
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return products;
    }
}

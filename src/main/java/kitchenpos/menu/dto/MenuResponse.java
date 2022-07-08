package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.global.domain.Price;
import kitchenpos.menu.domain.Menu;

public class MenuResponse {
    private long id;
    private String name;
    private BigDecimal price;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse() {
    }

    public MenuResponse(long id, String name, Price price,
                        List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price.value();
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuProducts().stream()
                        .map((MenuProductResponse::of))
                        .collect(Collectors.toList())
        );
    }



    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}

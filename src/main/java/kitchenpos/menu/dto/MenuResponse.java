package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Price;
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
                MenuProductResponse.from(menu.getMenuProducts()));
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

package kitchenpos.dto.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.product.Price;

public class MenuResponse {
    private Long id;
    private String name;
    private long price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse() {
    }

    public MenuResponse(Long id, String name, long price, Long menuGroupId, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu) {
        List<MenuProductResponse> menuProductResponses = mapToMenuProductResponse(menu);
        return new MenuResponse(menu.getId(),
                menu.getName(),
                mapToLongValue(menu.getPrice()),
                menu.getMenuGroupId(),
                menuProductResponses);
    }

    private static long mapToLongValue(Price price) {
        BigDecimal value = price.getPrice();
        return value.longValue();
    }

    private static List<MenuProductResponse> mapToMenuProductResponse(Menu menu) {
        return menu.getMenuProducts().stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}

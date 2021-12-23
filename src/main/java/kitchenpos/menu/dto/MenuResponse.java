package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProductResponses;

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
            menu.getMenuGroupId(), collectorMenuProductResponses(menu));
    }

    public static List<MenuResponse> toList(List<Menu> menus) {
        return menus.stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    }

    public MenuResponse() {
    }

    public MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId,
        List<MenuProductResponse> menuProductResponse) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductResponses = menuProductResponse;
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }

    private static List<MenuProductResponse> collectorMenuProductResponses(Menu menu) {
        return menu.getMenuProducts()
            .stream()
            .map(MenuProductResponse::of)
            .collect(Collectors.toList());
    }
}

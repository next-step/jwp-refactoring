package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import kitchenpos.menu.domain.Menu;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProductResponses = new ArrayList<>();

    protected MenuResponse() {
    }

    private MenuResponse(Long id, String name, BigDecimal price, List<MenuProductResponse> menuProductResponses, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuProductResponses = menuProductResponses;
        this.menuGroupId = menuGroupId;
    }

    public static MenuResponse of(Menu menu) {
        List<MenuProductResponse> menuProductResponses = menu.getMenuProducts().toResponses();
        return new MenuResponse(menu.getId(), menu.getName().toString(), menu.getPrice().toBigDecimal(), menuProductResponses, menu.getMenuGroupId());
    }

    public static MenuResponse of(Long id, String name, BigDecimal price,List<MenuProductResponse> menuProductResponses , Long menuGroupId) {
        return new MenuResponse(id, name, price, menuProductResponses, menuGroupId);
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

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }
}

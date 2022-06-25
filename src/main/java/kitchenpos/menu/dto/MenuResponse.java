package kitchenpos.menu.dto;

import java.util.List;
import kitchenpos.menu.domain.MenuProductV2;

public class MenuResponse {
    private Long id;
    private String name;
    private Long price;
    private Long menuGroupId;
    private List<MenuProductV2> menuProducts;

    public MenuResponse(Long id, String name, Long price, Long menuGroupId,
                        List<MenuProductV2> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductV2> getMenuProducts() {
        return menuProducts;
    }
}

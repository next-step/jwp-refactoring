package kitchenpos.menu.dto;

import java.util.List;
import kitchenpos.menu.domain.Menu;

public class MenuRequest {
    private final Long id;
    private final String name;
    private final Integer price;
    private final Long menuGroupId;
    private final List<MenuProductRequest> menuProducts;

    public MenuRequest(Long id, String name, Integer price, Long menuGroupId,
                       List<MenuProductRequest> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toMenu(){
        return new Menu(id, name, price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

}

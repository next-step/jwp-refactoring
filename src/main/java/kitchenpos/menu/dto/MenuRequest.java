package kitchenpos.menu.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

public class MenuRequest {
    @NotEmpty
    private String name;

    @PositiveOrZero
    private Long price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {
    }

    public MenuRequest(String name, Long price, Long menuGroupId,
                       List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}

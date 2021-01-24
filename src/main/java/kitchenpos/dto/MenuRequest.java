package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuRequest {
    public static final int ZERO = 0;
    private String name;
    private Integer price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;
    ;

    public MenuRequest(String name, int price, long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public boolean notValidPrice() {
        return Objects.isNull(price) || price < ZERO;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public boolean priceIsGreaterThen(BigDecimal sum) {
        return price.compareTo(sum.intValue()) > ZERO;
    }

    public Menu toMenu() {
        return new Menu(name, new BigDecimal(price), menuGroupId, menuProducts);
    }
}

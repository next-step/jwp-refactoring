package kitchenpos.util.dto;

import java.util.List;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

public class SaveMenuDto {

    private List<MenuProduct> menuProducts;
    private MenuGroup menuGroup;
    private String menuName;
    private int price;

    public SaveMenuDto(List<MenuProduct> menuProducts, MenuGroup menuGroup, String menuName, int price) {
        this.menuProducts = menuProducts;
        this.menuGroup = menuGroup;
        this.menuName = menuName;
        this.price = price;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public String getMenuName() {
        return menuName;
    }

    public int getPrice() {
        return price;
    }

}

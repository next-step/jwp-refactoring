package kitchenpos.menu.domain;

import kitchenpos.menu.exceptions.InputMenuDataErrorCode;
import kitchenpos.menu.exceptions.InputMenuDataException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Menu {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;


    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        this(name, price, menuGroupId);
        this.id = id;
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(name, price, menuGroupId);
        this.id = id;
        this.menuProducts = menuProducts;
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        checkValidValue(price, menuGroupId);
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public static Menu of(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }

    private void checkValidValue(BigDecimal price, Long menuGroupId) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InputMenuDataException(InputMenuDataErrorCode.IT_CAN_NOT_INPUT_MENU_PRICE_LESS_THAN_ZERO);
        }

        if (menuGroupId == null) {
            throw new InputMenuDataException(InputMenuDataErrorCode.YOU_MUST_INPUT_MENU_GROUP_ID);
        }

        if (menuGroupId < 0) {
            throw new InputMenuDataException(InputMenuDataErrorCode.THE_MENU_GROUP_ID_IS_LESS_THAN_ZERO);
        }
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public void validSum(BigDecimal sumPrice) {
        if(this.getPrice().compareTo(sumPrice) > 0){
            throw new InputMenuDataException(InputMenuDataErrorCode.THE_SUM_OF_MENU_PRICE_IS_LESS_THAN_SUM_OF_PRODUCTS);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;
        Menu menu = (Menu) o;
        return Objects.equals(getId(), menu.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

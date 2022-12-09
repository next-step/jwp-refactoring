package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

public class Menu {
    public static final String MENU_GROUP_NOT_NULL_EXCEPTION_MESSAGE = "메뉴 그룹이 없을 수 없습니다.";
    public static final String PRICE_NOT_NULL_EXCEPTION_MESSAGE = "가격은 필수입니다.";
    public static final String PRICE_NEGATIVE_EXCEPTION_MESSAGE = "가격은 필수입니다.";
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        if (menuGroupId == null) {
            throw new IllegalArgumentException(MENU_GROUP_NOT_NULL_EXCEPTION_MESSAGE);
        }
        if (price == null) {
            throw new IllegalArgumentException(PRICE_NOT_NULL_EXCEPTION_MESSAGE);
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(PRICE_NEGATIVE_EXCEPTION_MESSAGE);
        }
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(long id, String name, BigDecimal price, long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}

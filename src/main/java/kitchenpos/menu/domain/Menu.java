package kitchenpos.menu.domain;

import kitchenpos.common.Message;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id",
            foreignKey = @ForeignKey(name = "fk_menu_menu_group"),
            nullable = false)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validatePrice(price);
        validateMenuProducts(menuProducts, price);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = new MenuProducts(menuProducts, this);
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validatePrice(price);
        validateMenuProducts(menuProducts, price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = new MenuProducts(menuProducts, this);
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException(Message.ERROR_MENU_PRICE_REQUIRED.showText());
        }
        if (price.compareTo(BigDecimal.ZERO) < 1) {
            throw new IllegalArgumentException(Message.ERROR_MENU_PRICE_SHOULD_BE_OVER_THAN_ZERO.showText());
        }
    }

    public void validateMenuProducts(List<MenuProduct> menuProducts, BigDecimal price) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getTotalPrice());
        }
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException(Message.ERROR_MENU_PRICE_CANNOT_BE_BIGGER_THAN_MENUPRODUCTS_TOTAL.showText());
        }
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(final MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = MenuProducts.of(menuProducts);
    }
}

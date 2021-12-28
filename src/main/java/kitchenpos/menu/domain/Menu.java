package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.exception.IllegalMenuPriceException;
import kitchenpos.menugroup.exception.NoMenuGroupException;
import kitchenpos.product.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Menu {
    public static final int MIN_MENU_PRICE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    @Column(nullable = false)
    private Price price;

    @ManyToOne
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    public Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        validateMenuGroup();
        validateMenuPrice();
    }

    private void validateMenuGroup() {
        if (this.menuGroup == null) {
            throw new NoMenuGroupException();
        }
    }

    private void validateMenuPrice() {
        if (price.compareTo(menuProducts.getSumPrice()) > MIN_MENU_PRICE) {
            throw new IllegalMenuPriceException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return this.price.toBigDecimal();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}

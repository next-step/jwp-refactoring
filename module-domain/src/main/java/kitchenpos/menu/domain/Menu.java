package kitchenpos.menu.domain;

import java.math.*;
import java.util.*;

import javax.persistence.*;
import kitchenpos.common.*;

@Entity
public class Menu {
    private static final String MENU_NAME = "메뉴이름";
    private static final String MENU_PRICE = "메뉴금액";
    private static final String MENU_GROUP = "메뉴그룹";


    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {

    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        validate(name, price, menuGroup);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }

    private void validate(String name, BigDecimal price, MenuGroup menuGroup) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new WrongValueException(MENU_NAME);
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new WrongValueException(MENU_PRICE);
        }

        if (Objects.isNull(menuGroup)) {
            throw new WrongValueException(MENU_GROUP);
        }
    }

    public void addMenuProduct(Product product, Long quantity) {
        menuProducts.add(this.id, product, quantity);
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}

package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {
    private static final int MIN_SIZE_PRODUCTS_IN_MENU = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Embedded
    private MenuPrice price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    public static Menu create(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validate(menuGroup, menuProducts);
        Menu menu = new Menu(name, price, menuGroup);
        menuProducts.forEach(menu::addMenuProduct);
        return menu;
    }

    private Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this.name = name;
        this.price = new MenuPrice(price);
        this.menuGroup = menuGroup;
    }

    protected Menu() {
    }

    private void addMenuProduct(MenuProduct menuProduct) {
        menuProduct.updateMenu(this);
        menuProducts.add(menuProduct);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    private static void validate(MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        if (menuGroup == null) {
            throw new IllegalArgumentException("메뉴를 등록하기 위해서는 메뉴그룹이 있어야합니다.");
        }
        if (menuProducts.size() < MIN_SIZE_PRODUCTS_IN_MENU) {
            throw new IllegalArgumentException("메뉴를 등록하기 위해서는 등록할 상품이 " + MIN_SIZE_PRODUCTS_IN_MENU + "개 이상 있아야합니다.");
        }
    }
}

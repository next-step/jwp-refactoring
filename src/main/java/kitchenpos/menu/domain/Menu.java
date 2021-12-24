package kitchenpos.menu.domain;

import kitchenpos.menu.application.exception.InvalidPrice;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    private Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validatePrice(price, menuProducts);
        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;
        addProducts(menuProducts);
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroup, menuProducts);
    }

    private void validatePrice(BigDecimal price, List<MenuProduct> menuProducts) {
        Price sum = menuProducts.stream()
                .map(MenuProduct::calculate)
                .reduce(Price::sum)
                .orElseGet(Price::zero);

        if (!sum.isExpensiveThan(price)) {
            throw new InvalidPrice("메뉴 가격은 상품 가격의 합보다 적어야 합니다.");
        }
    }

    private void addProducts(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(this));
    }

    public void setMenuGroup(MenuGroup menuGroup) {
        if (this.menuGroup != null) {
            this.menuGroup.getMenus().remove(this);
        }
        this.menuGroup = menuGroup;
        menuGroup.getMenus().add(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}

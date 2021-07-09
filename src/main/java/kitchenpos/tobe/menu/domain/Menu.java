package kitchenpos.tobe.menu.domain;

import kitchenpos.tobe.common.model.Price;
import kitchenpos.tobe.menugroup.domain.MenuGroup;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(Long menuId) {
        this.id = menuId;
    }

    private Menu(String name, BigDecimal price, MenuProducts menuProducts, MenuGroup menuGroup) {
        this(null, name, Price.of(price), menuProducts, menuGroup);
    }

    private Menu(Long id, String name, Price price, MenuProducts menuProducts, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuProducts = menuProducts;
        this.menuGroup = menuGroup;
        menuProducts.registerMenu(this);
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = Price.of(price);
        this.menuProducts = new MenuProducts();
        this.menuGroup = menuGroup;
    }

    public static Menu createWithMenuProduct(String name, BigDecimal price, MenuProducts menuProducts, MenuGroup menuGroup) {
        validateProduct(menuProducts, price);
        return new Menu(name, price, menuProducts, menuGroup);
    }

    public static Menu of(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(id, name, price, menuGroup);
    }

    private static void validateProduct(MenuProducts menuProducts, BigDecimal price) {
        menuProducts.validatePrice(price);
        menuProducts.validateIsEmpty();
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }
}

package kitchenpos.menu.domain;

import kitchenpos.common.model.Price;

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
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(name, price, new MenuProducts(), menuGroup);
    }

    public void registerMenuProduct(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
        menuProduct.registerMenu(this);
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

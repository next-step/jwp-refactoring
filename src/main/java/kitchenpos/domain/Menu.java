package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    @OneToOne(fetch = LAZY, orphanRemoval = true)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @OneToMany(fetch = LAZY, mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    // for jpa
    public Menu() {
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(null, name, price, menuGroup, menuProducts);
    }

    private Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
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

    public void changePrice(BigDecimal price) {
        this.price = price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }

    public void addMenuProducts(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
        menuProduct.setMenu(this);
    }

    public void clearMenuProducts() {
        this.menuProducts.clear();
    }
}

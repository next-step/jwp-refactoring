package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = MenuProducts.createEmpty();

    protected Menu() {}

    public Menu(Long id) {
        this.id = id;
    }

    private Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(name, price, menuGroupId, menuProducts);
        this.id = id;
    }

    private Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = Name.from(name);
        this.price = Price.from(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = MenuProducts.from(menuProducts);
    }

    public static Menu from(long id) {
        return new Menu(id);
    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId) {
        return new Menu(name, price, menuGroupId, new ArrayList<>());
    }

    public static Menu of(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }

    public void addMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
        menuProducts.forEach(menuProduct -> menuProduct.assignMenu(this));
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}

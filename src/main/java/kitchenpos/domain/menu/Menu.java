package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
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

    private Menu(String name, BigDecimal price, Long menuGroupId) {
        this.name = Name.from(name);
        this.price = Price.from(price);
        this.menuGroupId = menuGroupId;
    }

    private Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = Name.from(name);
        this.price = Price.from(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = MenuProducts.from(menuProducts);
    }

    private Menu(String name, BigDecimal price) {
        this.name = Name.from(name);
        this.price = Price.from(price);
    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId) {
        return new Menu(name, price, menuGroupId);
    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public static Menu of(String name, BigDecimal price) {
        return new Menu(name, price);
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
        return this.menuGroupId;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public void appendMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }
}

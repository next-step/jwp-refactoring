package kitchenpos.menu.domain;

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

@Entity
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private MenuName name;
    @Embedded
    private MenuPrice price;
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts = MenuProducts.createEmpty();

    protected Menu() {}

    private Menu(String name, BigDecimal price, Long menuGroupId) {
        this.name = MenuName.from(name);
        this.price = MenuPrice.from(price);
        this.menuGroupId = menuGroupId;
    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId) {
        return new Menu(name, price, menuGroupId);
    }

    public Long getId() {
        return id;
    }

    public String findName() {
        return name.getValue();
    }

    public BigDecimal findPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return this.menuGroupId;
    }

    public List<MenuProduct> findMenuProducts() {
        return menuProducts.getReadOnlyValues();
    }

    public void appendMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
        menuProduct.mappedByMenu(this);
    }

    public void appendAllMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
        menuProducts.forEach(menuProduct -> menuProduct.mappedByMenu(this));
    }
}

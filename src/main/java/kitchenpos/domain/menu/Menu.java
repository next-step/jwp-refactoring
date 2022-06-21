package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.exception.MenuPriceException;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts = MenuProducts.createEmpty();

    protected Menu() {}

    private Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this.name = Name.from(name);
        this.price = Price.from(price);
        this.menuGroup = menuGroup;
    }

    private Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.name = Name.from(name);
        this.price = Price.from(price);
        this.menuGroup = menuGroup;
        this.menuProducts = MenuProducts.from(menuProducts);
    }

    private Menu(String name, BigDecimal price) {
        this.name = Name.from(name);
        this.price = Price.from(price);
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroup, menuProducts);
    }

    public static Menu of(String name, BigDecimal price) {
        return new Menu(name, price);
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
        return this.menuGroup.getId();
    }

    public List<MenuProduct> findMenuProducts() {
        return menuProducts.getReadOnlyValues();
    }

    public void appendMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public void appendAllMenuProducts(List<MenuProduct> menuProducts) {
        validateMenuPrice(menuProducts);
        menuProducts.forEach(menuProduct -> menuProduct.mappedByMenu(this));
    }

    private void validateMenuPrice(List<MenuProduct> menuProducts) {
        BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::calculateTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (price.compareTo(sum) > 0) {
            throw new MenuPriceException(price, sum);
        }
    }
}

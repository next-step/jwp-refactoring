package kitchenpos.domain;

import kitchenpos.common.exceptions.MenuGroupRequiredException;
import kitchenpos.common.exceptions.MenuProductSumPriceException;
import kitchenpos.common.exceptions.NoRequiredInputPriceException;
import kitchenpos.common.exceptions.NoRequiredNameException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "name", nullable = false))
    private Name name;
    @Enumerated
    @AttributeOverride(name = "price", column = @Column(name = "price", nullable = false))
    private Price price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts = MenuProducts.empty();

    public Menu() {
    }

    private Menu(final Long id, final Name name, final Price price, final MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public static Menu of(final Long id, final Name name, final Price price, final MenuGroup menuGroup) {
        validate(name, price, menuGroup);
        return new Menu(id, name, price, menuGroup);
    }

    public static Menu of(final String name, final BigDecimal price, final MenuGroup menuGroup) {
        return of(null, Name.from(name), Price.from(price), menuGroup);
    }

    private static void validate(final Name name, final Price price, final MenuGroup menuGroup) {
        if (Objects.isNull(name)) {
            throw new NoRequiredNameException();
        }
        if (Objects.isNull(price)) {
            throw new NoRequiredInputPriceException();
        }
        if (Objects.isNull(menuGroup)) {
            throw new MenuGroupRequiredException();
        }
    }

    public void addMenuProducts(final List<MenuProduct> menuProductList) {
        if (this.menuProducts.isOverPrice(this.price)) {
            throw new MenuProductSumPriceException();
        }
        menuProductList.forEach(this::addMenuProduct);
    }

    private void addMenuProduct(final MenuProduct menuProduct) {
        menuProduct.decideMenu(this);
        this.menuProducts.add(menuProduct);
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

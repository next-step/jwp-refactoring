package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private final MenuProducts menuProducts = MenuProducts.ofEmpty();

    protected Menu() {
    }

    public Menu(Long id, String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(name, price, menuGroup);
        this.id = id;
        menuProducts.forEach(this.menuProducts::add);
    }

    public Menu(String name, Price price, MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
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
        return menuProducts.getMenuProducts();
    }

    public void addMenuProduct(final MenuProduct menuProduct) {
        menuProduct.assignMenu(this);
        this.menuProducts
                .add(menuProduct);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id)
                && Objects.equals(name, menu.name)
                && Objects.equals(price, menu.price)
                && Objects.equals(menuGroup, menu.menuGroup)
                && Objects.equals(menuProducts, menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroup, menuProducts);
    }
}

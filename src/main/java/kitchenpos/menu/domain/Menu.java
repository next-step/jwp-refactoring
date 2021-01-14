package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.*;
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
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    protected Menu(final String name, final Price price, final MenuGroup menuGroup, final MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        this.menuProducts.linkMenu(this);
    }

    public static Menu of(final String name, final Price price, final MenuGroup menuGroup, final MenuProducts menuProducts) {
        return new Menu(name, price, menuGroup, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public Menu setPrice(final Price price) {
        this.price = price;
        return this;
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }
//
//    public void setMenuGroupId(final Long menuGroupId) {
//    }
//
    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }
//
//    public void setMenuProducts(final List<MenuProduct> menuProducts) {
//        this.menuProducts.setMenuProducts(menuProducts);
//    }
}

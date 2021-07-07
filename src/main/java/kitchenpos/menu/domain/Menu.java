package kitchenpos.menu.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.common.domian.Price;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupResponse;

@Entity
@Table(name = "menu")
public class Menu {

    public Menu() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    private Menu(MenuGroup menuGroup, String name, ProductsQuantities productsQuantities) {
        this.menuGroup = menuGroup;
        this.name = name;
        this.price = productsQuantities.totalPrice();
        this.menuProducts = new MenuProducts(this, productsQuantities);
    }

    private Menu(Long id, MenuGroup menuGroup, String name, ProductsQuantities productsQuantities) {
        this.id = id;
        this.menuGroup = menuGroup;
        this.name = name;
        this.price = productsQuantities.totalPrice();
        this.menuProducts = new MenuProducts(this, productsQuantities);
    }

    public static Menu of(MenuGroup menuGroup, String name, ProductsQuantities productsQuantities) {
        return new Menu(menuGroup, name, productsQuantities);
    }

    public static Menu of(Long id, MenuGroup menuGroup, String name, ProductsQuantities productsQuantities) {
        return new Menu(id, menuGroup, name, productsQuantities);
    }

    public Long id() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public int priceToInt() {
        return price.amountToInt();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }
}

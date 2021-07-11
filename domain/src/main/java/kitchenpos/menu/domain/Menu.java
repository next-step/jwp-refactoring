package kitchenpos.menu.domain;

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
import kitchenpos.menugroup.domain.MenuGroup;

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

    private Menu(MenuGroup menuGroup, String name, ProductsQuantities productsQuantities) {
        this.menuGroup = menuGroup;
        this.name = name;
        this.price = productsQuantities.totalPrice();
    }

    public static Menu of(MenuGroup menuGroup, String name, ProductsQuantities productsQuantities) {
        return new Menu(menuGroup, name, productsQuantities);
    }

    public static Menu of(Long id, MenuGroup menuGroup, String name, ProductsQuantities productsQuantities) {
        Menu menu = new Menu(menuGroup, name, productsQuantities);
        menu.id = id;
        return menu;
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
}

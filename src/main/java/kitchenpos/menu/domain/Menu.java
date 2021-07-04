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
import kitchenpos.menu.dto.MenuResponse;
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

    @Embedded
    private MenuProducts menuProducts;

    private Menu(MenuGroup menuGroup, String name, Price price, MenuProducts menuProducts) {
        this.price = price;
        this.name = name;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu of(MenuGroup menuGroup, String name, Price price, MenuProducts menuProducts) {
        return new Menu(menuGroup, name, price, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
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

    public MenuResponse toResponse() {
        return MenuResponse.of(id, name, price.amountToInt(), menuGroup.toResponse(), menuProducts.toResponse());
    }
}

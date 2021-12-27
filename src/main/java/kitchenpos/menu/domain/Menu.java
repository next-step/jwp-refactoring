package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @Column
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = MenuProducts.empty();

    protected Menu() {
    }

    private Menu(Name name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        addMenuProducts(menuProducts.getMenuProducts());
    }

    private Menu(Long id, Name name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this(name, price, menuGroupId, menuProducts);
        this.id = id;
    }

    public static Menu of(Name name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public static Menu of(Long id, Name name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
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

    private void addMenuProducts(List<MenuProduct> menuProducts) {
        menuProducts.forEach(menuProduct ->
        {
            menuProduct.setMenu(this);
            this.menuProducts.add(menuProduct);
        });
    }
}

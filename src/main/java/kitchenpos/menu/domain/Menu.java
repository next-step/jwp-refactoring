package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;
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
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    private Menu(Long id, Name name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this(name, price, menuGroupId, menuProducts);
        this.id = id;
    }

    public Menu(Name name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        addMenuProducts(menuProducts);
    }

    public static Menu of(Long id, Name name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }

    public static Menu of(Name name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    private void addMenuProducts(MenuProducts menuProducts) {
        menuProducts.getMenuProducts().forEach(menuProduct -> {
            menuProduct.toMenu(this);
            this.menuProducts.add(menuProduct);
        });
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }
}

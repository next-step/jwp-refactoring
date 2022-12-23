package kitchenpos.menu.domain;

import kitchenpos.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {}

    public Menu(Long id, String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.price = price;
        this.name = name;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu create(String name, BigDecimal priceValue, MenuGroup menuGroup, Price sumPrice, MenuProducts menuProducts) {
        Price price = Price.create(priceValue);
        MenuValidator.validate(price, sumPrice);
        return new Menu(name, price, menuGroup, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.value();
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.value();
    }

}

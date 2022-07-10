package kitchenpos.menu.domain;

import kitchenpos.domain.Price;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(String name, Integer price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, Price.from(price), menuGroupId, new MenuProducts(menuProducts));
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(this));
    }

    public Menu(Long id, String name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public BigDecimal getPriceBigDecimal() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}

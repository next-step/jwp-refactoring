package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;

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

    private Menu(String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        this.price = Price.from(price);
        this.menuProducts = menuProducts;
        menuProducts.setMenu(this);
        this.name = name;
        this.menuGroupId = menuGroupId;
    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public MenuProducts 메뉴세트목록() {
        return menuProducts;
    }
}

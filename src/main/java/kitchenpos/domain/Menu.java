package kitchenpos.domain;

import javax.persistence.*;
import java.util.ArrayList;
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
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(String name, long price, Long menuGroupId) {
        this.name = name;
        this.price = Price.valueOf(price);
        this.menuGroupId = menuGroupId;
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        menuProduct.toMenu(this);
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public boolean moreExpensiveThen(Price price) {
        return this.price.compareTo(price) > 0;
    }
}

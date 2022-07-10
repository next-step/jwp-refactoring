package kitchenpos.menu.domain;

import kitchenpos.domain.Price;

import javax.persistence.*;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {}

    public Menu(String name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts.addMenuProducts(menuProducts);

        this.menuProducts.associateMenu(this);
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

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}

package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {}

    public Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        validateMenu(menuProducts, price);

        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts.addMenuProducts(menuProducts);

        this.menuProducts.associateMenu(this);
    }

    public void validateMenu(MenuProducts menuProducts, Price price) {
        if (menuProducts == null || menuProducts.isEmpty()) {
            throw new IllegalArgumentException("메뉴에 상품이 포함되어 있지 않습니다.");
        }
        if (menuProducts.isNotSameTotalPriceByPrice(price)) {
            throw new IllegalArgumentException("메뉴의 가격과 상품의 총 가격이 일치하지 않습니다.");
        }
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
}

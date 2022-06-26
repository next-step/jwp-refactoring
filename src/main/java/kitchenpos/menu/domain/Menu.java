package kitchenpos.menu.domain;

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

    public Menu(Long id, String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts.addMenuProducts(menuProducts);
    }

    public Menu(String name, Price price, MenuGroup menuGroup) {
        this(null, name, price, menuGroup, new MenuProducts());
    }

    public void validateMenuAndProductTotalPrice() {
        if (isNotSameMenuAndProductTotalPrice()) {
            throw new IllegalArgumentException("메뉴의 가격과 상품의 총 가격이 일치하지 않습니다.");
        }
    }

    public void addMenuProducts(MenuProducts menuProducts) {
        this.menuProducts.addMenuProducts(menuProducts);
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

    private boolean isNotSameMenuAndProductTotalPrice() {
        return this.menuProducts
                .getTotalProductPrice()
                .isNotSame(this.price);
    }
}

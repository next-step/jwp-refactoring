package kitchenpos.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Money price;

    @JoinColumn(name = "menuGroupId")
    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menuId")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(String name, Money price, MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void checkAllowProductsPrice() {
        checkGraterThanMenuPrice(menuProducts.stream()
                .map(MenuProduct::getAmount)
                .reduce(Money.ZERO_MONEY, Money::sum));
    }

    private void checkGraterThanMenuPrice(Money sum) {
        if (this.price.isGraterThan(sum)) {
            throw new IllegalArgumentException("메뉴의 가격이 상품 가격 보다 큽니다.");
        }
    }

    public void add(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public static Menu of(String name, Money price, MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }
}

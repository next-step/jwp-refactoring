package kitchenpos.menu.domain;

import kitchenpos.converter.MoneyConverter;
import kitchenpos.infra.Money;
import kitchenpos.menugroup.domain.MenuGroup;

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

    @Convert(converter = MoneyConverter.class)
    @Column
    private Money price;

    @JoinColumn(name = "menuGroupId")
    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    public Menu(String name, Money price, MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public Menu(String name, Money price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
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


    public void addProducts(List<MenuProduct> menuProducts) {
        checkGraterThanMenuPrice(menuProducts.stream()
                .map(MenuProduct::getAmount)
                .reduce(Money.ZERO_MONEY, Money::sum));
        this.menuProducts.addAll(menuProducts);
    }

    private void checkGraterThanMenuPrice(Money sum) {
        if (this.price.isGraterThan(sum)) {
            throw new IllegalArgumentException("메뉴의 가격이 상품 가격 보다 큽니다.");
        }
    }
}

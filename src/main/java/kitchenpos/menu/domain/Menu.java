package kitchenpos.menu.domain;

import kitchenpos.converter.MoneyConverter;
import kitchenpos.infra.Money;
import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.*;
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

    @Embedded
    private final MenuProducts menuProducts = new MenuProducts();

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
        return menuProducts.getProducts();
    }

    public void addProducts(List<MenuProduct> menuProducts) {
        checkGraterThanMenuPrice(menuProducts.stream()
                .map(MenuProduct::getAmount)
                .reduce(Money.ZERO_MONEY, Money::sum));
        this.menuProducts.addAll(menuProducts);
    }

    private void checkGraterThanMenuPrice(Money sum) {
        if (price.isGraterThan(sum)) {
            throw new IllegalArgumentException("메뉴의 가격이 상품 가격 보다 큽니다.");
        }
    }
}

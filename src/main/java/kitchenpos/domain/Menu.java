package kitchenpos.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.exception.ExceedingTotalPriceException;
import kitchenpos.exception.MenuDetailMismatchException;
import kitchenpos.exception.MenuMismatchException;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    @Column(nullable = false)
    private Price price = new Price();

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(String name, Price price, MenuProducts menuProducts) {
        this(null, name, price, menuProducts);
    }

    Menu(Long id, String name, Price price, MenuProducts menuProducts) {
        checkArguments(name, price, menuProducts);
        checkPriceAndSummation(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuProducts = menuProducts;
    }

    private void checkArguments(String name, Price price, MenuProducts menuProducts) {
        if (Objects.isNull(name) || Objects.isNull(price) || Objects.isNull(menuProducts)) {
            throw new IllegalArgumentException("메뉴를 생성하려면 모든 필수값이 입력되어야 합니다.");
        }
    }

    private void checkPriceAndSummation(Price price, MenuProducts menuProducts) {
        if (price.isBiggerThan(menuProducts.summation())) {
            throw new ExceedingTotalPriceException("메뉴 가격이 제품 가격의 총 합을 초과합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public Price getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Menu menu = (Menu)o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void validateOrder(MenuOption menuOption, List<MenuDetailOption> menuDetailOptions) {
        if (!isSatisfiedBy(menuOption)) {
            throw new MenuMismatchException("메뉴가 변경되었습니다.");
        }

        if (!isSatisfiedBy(menuDetailOptions)) {
            throw new MenuDetailMismatchException("메뉴 구성이 변경되었습니다.");
        }
    }

    private boolean isSatisfiedBy(MenuOption menuOption) {
        if (!this.name.equals(menuOption.getName())) {
            return false;
        }

        return this.price.hasSameValueAs(menuOption.getPrice());
    }

    private boolean isSatisfiedBy(List<MenuDetailOption> menuDetailOptions) {
        return menuProducts.isSatisfiedBy(menuDetailOptions);
    }
}

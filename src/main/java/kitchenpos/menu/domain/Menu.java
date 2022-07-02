package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.common.domain.Price;

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
    private final MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProductList) {
        this(name, new Price(price), menuGroup, menuProductList);
    }

    public Menu(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProductList) {
        validate(price, menuProductList);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        menuProductList.forEach(menuProducts::add);
    }

    private void validate(Price price, List<MenuProduct> menuProducts) {
        if (price.biggerThan(getTotalPrice(menuProducts))) {
            throw new IllegalArgumentException("메뉴 내 제품가격의 합보다 메뉴가격이 클 수 없습니다.");
        }
    }

    public static Price getTotalPrice(List<MenuProduct> menuProductList) {
        Price totalPrice = new Price(Price.MIN_PRICE);
        for (MenuProduct menuProduct : menuProductList) {
            Price price = menuProduct.getPrice();
            totalPrice.sum(price);
        }
        return totalPrice;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.get();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Menu menu = (Menu) o;

        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

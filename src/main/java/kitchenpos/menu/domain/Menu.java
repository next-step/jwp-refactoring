package kitchenpos.menu.domain;

import kitchenpos.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Embedded
    private Amount amount;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    public Menu() {

    }

    public Menu(String name, BigDecimal amount, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.name = name;
        this.amount = new Amount(amount);
        this.menuGroup = menuGroup;
        this.menuProducts = new MenuProducts(menuProducts, id);

        validatePriceAndPriceSumOfMenuProducts();
    }

    // 메뉴의 가격이 메뉴의 메뉴제품들 가격 합보다 비싸면 Illegal
    public void validatePriceAndPriceSumOfMenuProducts() {
        BigDecimal priceSumOfMenuProducts = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts.get()) {
            priceSumOfMenuProducts = priceSumOfMenuProducts.add(menuProduct.getPrice());
        }

        amount.checkIfOverThan(priceSumOfMenuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getAmount() {
        return amount.get();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name) && Objects.equals(amount, menu.amount) && Objects.equals(menuGroup, menu.menuGroup) && Objects.equals(menuProducts, menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, amount, menuGroup, menuProducts);
    }
}

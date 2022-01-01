package kitchenpos.menu.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.IllegalArgumentException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @Column(name = "menu_group_id", nullable = false)
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(Long id, String name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        checkMenuPrice(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu of(String name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        return new Menu(null, name, price, menuGroupId, menuProducts);
    }

    private void checkMenuPrice(Price price, MenuProducts menuProducts) {
        BigDecimal totalPrice = menuProducts.calculateTotalPrice();

        if (price.isGreaterThan(totalPrice)) {
            throw new IllegalArgumentException("메뉴의 가격이 상품 가격의 합계보다 클 수 없습니다.");
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.asList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Price calculateMenuProductsPrice(BigDecimal totalPrice) {
        return null;
    }
}

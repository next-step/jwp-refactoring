package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {

    public static final String MESSAGE_VALIDATE_PRICE = "가격은 메뉴 상품들의 금액 합계와 같아야 합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    @Column(nullable = false)
    private Price price;

    @Column(nullable = false)
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        validatePrice(new Price(price), menuProducts);
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
        menuProducts.changeMenu(this);
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
        return menuProducts.getMenuProducts();
    }

    private void validatePrice(Price price, MenuProducts menuProducts) {
        if (!price.isEqual(menuProducts.totalPrice())) {
            throw new IllegalArgumentException(MESSAGE_VALIDATE_PRICE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name) && Objects.equals(price, menu.price) && Objects.equals(menuGroupId, menu.menuGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroupId);
    }
}

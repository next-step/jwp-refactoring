package kitchenpos.menu.domain;

import kitchenpos.price.domain.Amount;
import kitchenpos.price.domain.Price;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


import static java.util.Objects.requireNonNull;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;
    @Column(nullable = false)
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this.name = requireNonNull(name, "menuGroupId");
        this.price = new Price(price);
        this.menuGroupId = requireNonNull(menuGroupId, "menuGroupId");
    }

    protected Menu() {
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        validateAmount(menuProducts);
        this.menuProducts.addAll(this, menuProducts);
    }

    private void validateAmount(List<MenuProduct> menuProducts) {
        Amount amount = calculateAmount(menuProducts);
        if (price.toAmount().isGatherThan(amount)) {
            throw new IllegalArgumentException("상품들 금액의 합이 메뉴 가격보다 클 수 없습니다.");
        }
    }

    private Amount calculateAmount(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::calculateAmount)
                .reduce(Amount.ZERO, Amount::add);
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
        return menuProducts.get();
    }
}

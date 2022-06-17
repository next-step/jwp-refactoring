package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import static java.util.Objects.requireNonNull;

@Entity(name = "menu")
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;
    @Column(nullable = false)
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public MenuEntity(String name, BigDecimal price, Long menuGroupId) {
        this.name = new Name(name);
        this.price = new Price(price);
        this.menuGroupId = requireNonNull(menuGroupId, "menuGroupId");
    }

    protected MenuEntity() {
    }

    public void addMenuProducts(List<MenuProductEntity> menuProducts) {
        validateAmount(menuProducts);
        this.menuProducts.addAll(this, menuProducts);
    }

    private void validateAmount(List<MenuProductEntity> menuProducts) {
        Amount amount = calculateAmount(menuProducts);
        if (price.toAmount().isGatherThan(amount)) {
            throw new InvalidPriceException("상품들 금액의 합이 메뉴 가격보다 클 수 없습니다.");
        }
    }

    private Amount calculateAmount(List<MenuProductEntity> menuProducts) {
        return menuProducts.stream()
                           .map(MenuProductEntity::calculateAmount)
                           .reduce(Amount.ZERO, Amount::add);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductEntity> getMenuProducts() {
        return menuProducts.get();
    }
}

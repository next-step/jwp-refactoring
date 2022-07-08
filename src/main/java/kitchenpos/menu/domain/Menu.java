package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {

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

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this.name = new Name(name);
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        this.id = id;
        this.name = new Name(name);
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        validateSumPrice(menuProducts);
        this.menuProducts.addAll(this, menuProducts);
    }

    private void validateSumPrice(List<MenuProduct> menuProducts) {
        BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::calculateSumPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (price.isGatherThan(sum)) {
            throw new IllegalArgumentException("메뉴의 가격은 전체 상품의 가격의 합보다 작거나 같아야합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.get();
    }

}

package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.common.domain.Price;
import kitchenpos.exception.InvalidArgumentException;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public List<MenuProduct> get() {
        return this.menuProducts;
    }

    public void validatePrice(Price price) {
        if (price.isGreaterThan(getTotalPrice())) {
            throw new InvalidArgumentException("메뉴의 총 가격은 구성하는 상품의 총가격보다 작거나 같아야 합니다.");
        }
    }

    protected void add(MenuProduct menuProduct) {
        if (!contains(menuProduct)) {
            menuProducts.add(menuProduct);
        }
    }

    protected Price getTotalPrice() {
        BigDecimal sum = menuProducts.stream()
            .map(it -> it.getPrice().getPrice())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return Price.valueOf(sum);
    }

    private boolean contains(MenuProduct menuProduct) {
        return menuProducts.stream()
            .anyMatch(it -> it.equalMenuProduct(menuProduct));
    }
}

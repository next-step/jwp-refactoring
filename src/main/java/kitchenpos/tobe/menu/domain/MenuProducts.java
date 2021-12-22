package kitchenpos.tobe.menu.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.tobe.common.domain.Price;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(
        name = "menu_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_menu_product_to_menu")
    )
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        validate(menuProducts);
        this.menuProducts = menuProducts;
    }

    private void validate(final List<MenuProduct> menuProducts) {
        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException("1 개 이상의 등록된 상품으로 메뉴를 등록할 수 있습니다.");
        }
    }

    public Price calculateTotalPrice() {
        return menuProducts.stream()
            .map(MenuProduct::calculateTotalPrice)
            .reduce(Price.ZERO, Price::add);
    }

    public List<MenuProduct> asList() {
        return Collections.unmodifiableList(menuProducts);
    }

    public List<Long> getProductIds() {
        final List<Long> productIds = menuProducts
            .stream()
            .map(MenuProduct::getProductId)
            .collect(Collectors.toList());
        return Collections.unmodifiableList(productIds);
    }
}

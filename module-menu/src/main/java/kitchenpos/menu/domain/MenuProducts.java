package kitchenpos.menu.domain;

import kitchenpos.common.domain.Quantity;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", fetch = LAZY, cascade = ALL, orphanRemoval = true)
    private final List<MenuProduct> menuProducts;

    public MenuProducts() {
        this(new ArrayList<>());
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> values() {
        return menuProducts;
    }

    public static MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public void setMenu(Menu menu) {
        menuProducts.forEach(m -> m.setMenu(menu));
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    public Quantity getQuantity(Long productId) {
        return menuProducts.stream()
                .filter(m -> m.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 상품이 없습니다."))
                .getQuantity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProducts that = (MenuProducts) o;
        return Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuProducts);
    }
}

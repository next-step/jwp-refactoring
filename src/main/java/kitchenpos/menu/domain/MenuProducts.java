package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts(final List<MenuProduct> menuProducts) {
        validate(menuProducts);
        this.menuProducts.addAll(menuProducts);
    }

    public MenuProducts() {

    }

    private void validate(final List<MenuProduct> menuProducts) {
        if (CollectionUtils.isEmpty(menuProducts)) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> ids() {
        return menuProducts.stream()
            .map(MenuProduct::getProductId)
            .collect(Collectors.toList());
    }

    public List<MenuProduct> toList() {
        return menuProducts;
    }

    public void addAll(final List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    public BigDecimal totalPrice() {
        return menuProducts.stream()
            .map(MenuProduct::price)
            .reduce(BigDecimal::add)
            .orElseThrow(IllegalArgumentException::new);
    }

    public void add(final MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    public void updateMenu(final Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.updateMenu(menu));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final MenuProducts that = (MenuProducts)o;
        return Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuProducts);
    }
}

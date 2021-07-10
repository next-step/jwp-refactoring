package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu",cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts) {
        validate(menuProducts);
        this.menuProducts = menuProducts;
    }

    protected MenuProducts() {

    }

    private void validate(List<MenuProduct> menuProducts) {
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

    public void addAll(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    public BigDecimal totalPrice() {
        return menuProducts.stream()
            .map(MenuProduct::price)
            .reduce(BigDecimal::add)
            .orElseThrow(IllegalAccessError::new);
    }
}

package kitchenpos.menu.domain;

import static kitchenpos.exception.KitchenposExceptionMessage.MENU_PRICE_CANNOT_OVER_THAN_PRODUCT_PRICE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.exception.KitchenposException;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public void add(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    public void setMenu(final Menu menu) {
        this.menuProducts.forEach(menuProduct -> menuProduct.setMenu(menu));
    }

    public void checkOverPrice(final BigDecimal price) {
        BigDecimal sum = this.menuProducts.stream()
                                          .map(this::calculatePrice)
                                          .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (price.compareTo(sum) > 0) {
            throw new KitchenposException(MENU_PRICE_CANNOT_OVER_THAN_PRODUCT_PRICE);
        }
    }

    private BigDecimal calculatePrice(MenuProduct menuProduct) {
        return menuProduct.getProductPrice()
                          .multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
    }

    public <R> List<R> convertAll(Function<MenuProduct, R> converter) {
        return this.menuProducts.stream()
                                .map(converter)
                                .collect(Collectors.toList());
    }
}

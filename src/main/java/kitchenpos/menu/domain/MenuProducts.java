package kitchenpos.menu.domain;

import kitchenpos.menu.exceptions.InputMenuDataErrorCode;
import kitchenpos.menu.exceptions.InputMenuDataException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Embeddable
public class MenuProducts implements Iterable<MenuProduct> {
    private static final int MIN_PRICE = 0;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {

    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    @Override
    public Iterator<MenuProduct> iterator() {
        return this.menuProducts.iterator();
    }

    public void validSum(BigDecimal menuPrice) {
        BigDecimal productAmount = calculateMenuProductPrice();
        if (menuPrice.compareTo(productAmount) > MIN_PRICE) {
            throw new InputMenuDataException(InputMenuDataErrorCode.THE_SUM_OF_MENU_PRICE_IS_LESS_THAN_SUM_OF_PRODUCTS);
        }
    }

    private BigDecimal calculateMenuProductPrice() {
        return this.menuProducts.stream()
                .map(it-> it.getProduct().getPrice().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

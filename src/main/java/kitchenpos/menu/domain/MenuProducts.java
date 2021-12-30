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

    @OneToMany(mappedBy = "menuId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {

    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public List<MenuProduct> menuProducts() {
        return menuProducts;
    }

    @Override
    public Iterator<MenuProduct> iterator() {
        return this.menuProducts.iterator();
    }

    public void validSum(BigDecimal menuPrice) {
        BigDecimal productAmount = calculateMenuProductPrice();
        if (isBiggerMenuPriceThanProductAmount(menuPrice, productAmount)) {
            throw new InputMenuDataException(InputMenuDataErrorCode.THE_SUM_OF_MENU_PRICE_IS_LESS_THAN_SUM_OF_PRODUCTS);
        }
    }

    private boolean isBiggerMenuPriceThanProductAmount(BigDecimal menuPrice, BigDecimal productAmount) {
        return menuPrice.compareTo(productAmount) > 0;
    }

    private BigDecimal calculateMenuProductPrice() {
        return this.menuProducts.stream()
                .map(menuProduct -> menuProduct.getProductAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

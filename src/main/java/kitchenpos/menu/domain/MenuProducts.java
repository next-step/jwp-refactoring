package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.ExceptionType;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<MenuProduct> items = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> items, Price price) {
        validateMenuProducts(items, price);
        this.items = items;
    }

    private void validateMenuProducts(List<MenuProduct> items, Price price) {
        items.forEach(MenuProduct::validateHasProduct);
        validatePriceOverMenuPrice(items, price);
    }

    private void validatePriceOverMenuPrice(List<MenuProduct> items, Price price) {
        Price sum = new Price(BigDecimal.ZERO);
        for (MenuProduct item : items) {
            sum = sum.add(item.getTotalPrice());
        }

        if (price.isOverThan(sum)) {
            throw new CannotCreateException(ExceptionType.IS_NOT_OVER_THAN_MENU_PRICE);
        }
    }

    public List<MenuProduct> getItems() {
        return items;
    }

    public void mapInto(Menu menu) {
        this.items.forEach(it -> it.mapInto(menu));
    }
}

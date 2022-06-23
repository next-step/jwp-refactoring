package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<MenuProductEntity> items = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProductEntity> items, Price price) {
        validateMenuProducts(items, price);
        this.items = items;
    }

    private void validateMenuProducts(List<MenuProductEntity> items, Price price) {
        items.forEach(MenuProductEntity::validateHasProduct);
        validatePriceOverMenuPrice(items, price);
    }

    private void validatePriceOverMenuPrice(List<MenuProductEntity> items, Price price) {
        Price sum = new Price(BigDecimal.ZERO);
        for (MenuProductEntity item : items) {
            sum = sum.add(item.getTotalPrice());
        }

        if (price.isOverThan(sum)) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuProductEntity> getItems() {
        return items;
    }

    public void mapInto(MenuEntity menu) {
        this.items.forEach(it -> it.mapInto(menu));
    }
}

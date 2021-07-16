package domain.menu;

import common.valueobject.Price;
import domain.menu.exception.BadMenuPriceException;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {

    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 100)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id", nullable = false, insertable = false, updatable = false)
    private final List<MenuProduct> menuProducts;

    protected MenuProducts() {
        this.menuProducts = new ArrayList<>();
    }

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public List<MenuProduct> getUnmodifiableList() {
        return Collections.unmodifiableList(menuProducts);
    }

    public void validatePrice(Price menuPrice) {
        Price sumPrice = Price.of(menuProducts.stream()
                .map(MenuProduct::getCalculatedPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        if (menuPrice.isMoreExpensiveThan(sumPrice)) {
            throw new BadMenuPriceException();
        }
    }

    public void registerAll(Long menuId) {
        menuProducts.forEach(menuProduct -> menuProduct.registerMenu(menuId));
    }
}

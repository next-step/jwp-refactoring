package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts addList(List<MenuProduct> menuProductList) {
        menuProductList.stream()
                .forEach(menuProducts::add);

        return this;
    }

    public BigDecimal getMenuProductPriceSum() {
        int productsPriceSum = menuProducts.stream()
                .mapToInt(menuProduct -> menuProduct.getMenuProductPrice().intValue())
                .sum();
        return new BigDecimal(productsPriceSum);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}

package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {
    @Transient
    private Menu menu;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts(){

    }

    private MenuProducts(Menu menu, List<MenuProduct> menuProducts) {
        this.menu = menu;
        setMenuProducts(menuProducts);
    }

    public static MenuProducts of(Menu menu, List<MenuProduct> menuProducts) {
        return new MenuProducts(menu, menuProducts);
    }

    public void setMenuProducts(List<MenuProduct> menuProducts) {
        if(CollectionUtils.isEmpty(menuProducts)){
            return;
        }
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(menu));
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> getMenuProducts() {
        return this.menuProducts;
    }

    public BigDecimal getSum() {
        return getMenuProducts()
                .stream()
                .map(MenuProduct::getPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}

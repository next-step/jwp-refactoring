package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProductEntity> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProductEntity> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProductEntity> values() {
        return Collections.unmodifiableList(this.menuProducts);
    }

    public void updateMenu(MenuEntity menuEntity) {
        for (MenuProductEntity menuProduct : menuProducts) {
            menuProduct.updateMenu(menuEntity);
        }
    }

    public BigDecimal getTotalPrice() {
        return menuProducts.stream().map(MenuProductEntity::getMenuProductPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}

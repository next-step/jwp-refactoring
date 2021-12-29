package kitchenpos.menu.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName : kitchenpos.menu.domain
 * fileName : MenuProducts
 * author : haedoang
 * date : 2021-12-22
 * description :
 */
@Embeddable
public class MenuProducts {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    public List<MenuProduct> value() {
        return menuProducts;
    }

    public void add(List<MenuProduct> newMenuProducts) {
        menuProducts.addAll(newMenuProducts);
    }
}

package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
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

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    public List<MenuProduct> value() {
        return menuProducts;
    }

    public void add(Menu menu, List<MenuProduct> newMenuProducts) {
        newMenuProducts.forEach(
            it -> menuProducts.add(it.by(menu))
        );
    }
}

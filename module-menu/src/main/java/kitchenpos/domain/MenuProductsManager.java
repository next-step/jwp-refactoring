package kitchenpos.domain;

import com.google.common.collect.ImmutableList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProductsManager {

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new LinkedList<>();

    protected MenuProductsManager() {
    }

    public void add(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    public List<MenuProduct> getMenuProducts() {
        return ImmutableList.copyOf(menuProducts);
    }
}

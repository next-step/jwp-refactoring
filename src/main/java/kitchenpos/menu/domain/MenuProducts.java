package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public void add(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public List<MenuProduct> getAll() {
        return this.menuProducts;
    }
}

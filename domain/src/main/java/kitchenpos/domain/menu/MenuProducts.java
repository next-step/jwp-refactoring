package kitchenpos.domain.menu;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    @JoinColumn(name = "menuId")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public void add(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public List<MenuProduct> getAll() {
        return this.menuProducts;
    }
}

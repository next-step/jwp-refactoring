package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<MenuProduct> items = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> items, Menu menu) {
        mapInto(items, menu);
        this.items = items;
    }

    public List<MenuProduct> getItems() {
        return items;
    }

    public void mapInto(List<MenuProduct> items, Menu menu) {
        items.forEach(it -> it.mapInto(menu));
    }
}

package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "kitchenpos/menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> elements = new ArrayList<>();

    public MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.elements = menuProducts;
    }

    public List<MenuProduct> getElements() {
        return Collections.unmodifiableList(new ArrayList<>(this.elements));
    }

    public void addMenu(Menu menu) {
        elements.forEach(menuProduct -> menuProduct.updateMenu(menu));
    }
}

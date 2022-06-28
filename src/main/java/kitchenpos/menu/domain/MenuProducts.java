package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> elements = new ArrayList<>();

    public List<MenuProduct> getElements() {
        return elements;
    }

    public void addAll(final Menu menu, final List<MenuProduct> menuProducts) {
        for (MenuProduct menuProduct : menuProducts) {
            elements.add(menuProduct);
            menuProduct.setMenu(menu);
        }
    }
}

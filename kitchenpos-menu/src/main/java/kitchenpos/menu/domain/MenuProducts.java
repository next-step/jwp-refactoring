package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu" , cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<MenuProduct> menuProductItems;

    protected MenuProducts() {
        this.menuProductItems = new ArrayList<>();
    }

    public MenuProducts(List<MenuProduct> menuProductItems) {
        this.menuProductItems = new ArrayList<>(menuProductItems);
    }

    public List<MenuProduct> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(this.menuProductItems));
    }

    public void changeMenu(Menu menu) {
        this.menuProductItems.forEach(menuProduct -> menuProduct.changeMenu(menu));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuProducts that = (MenuProducts) o;

        return menuProductItems.equals(that.menuProductItems);
    }

    @Override
    public int hashCode() {
        return menuProductItems.hashCode();
    }
}

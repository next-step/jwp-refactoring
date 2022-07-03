package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(fetch = FetchType.LAZY)
    private final List<MenuProduct> elements = new ArrayList<>();

    public MenuProducts() {
    }

    public void add(MenuProduct menuProduct) {
        elements.add(menuProduct);
    }

    public List<MenuProduct> get() {
        return new ArrayList<>(elements);
    }

}

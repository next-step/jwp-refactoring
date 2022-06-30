package kitchenpos.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")
    private List<MenuProduct> elements = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        elements = new ArrayList<>(menuProducts);
    }

    public List<MenuProduct> elements() {
        return Collections.unmodifiableList(elements);
    }
}

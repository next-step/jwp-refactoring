package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> elements = new ArrayList<>();

    public void add(MenuProduct menuProduct) {
        elements.add(menuProduct);
    }

    public List<MenuProduct> getElements() {
        return elements;
    }
}

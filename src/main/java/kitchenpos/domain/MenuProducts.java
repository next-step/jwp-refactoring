package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProductEntity> elements = new ArrayList<>();

    public void add(MenuProductEntity menuProduct) {
        elements.add(menuProduct);
    }

    public List<MenuProductEntity> getElements() {
        return elements;
    }
}

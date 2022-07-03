package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> elements;

    public MenuProducts() {
        elements = new ArrayList<>();
    }

    public MenuProducts(Menu menu, List<MenuProduct> elements) {
        this.elements = elements;
        for (final MenuProduct menuProduct : this.elements) {
            menuProduct.connectedBy(menu);
        }
    }

    public List<Long> getProductIds() {
        return elements.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    public List<MenuProduct> getMenuProducts() {
        return elements;
    }
}

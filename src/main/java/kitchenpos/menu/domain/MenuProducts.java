package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.menu.exception.NotExistMenuProductException;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<MenuProduct> elements = new ArrayList<>();

    protected MenuProducts(){

    }

    void addMenuProducts(List<MenuProduct> menuProducts) {
        elements.addAll(menuProducts);

    }

    void includeToMenu(Menu menu) {
        elements.forEach(menuProduct -> menuProduct.includeToMenu(menu));
    }

    List<MenuProduct> toList(){
        return Collections.unmodifiableList(elements);
    }

    MenuProduct findMenuProductByProductId(Long id) {
        return elements.stream()
                .filter(menuProduct -> menuProduct.getProductId().equals(id))
                .findFirst()
                .orElseThrow(NotExistMenuProductException::new);
    }
}

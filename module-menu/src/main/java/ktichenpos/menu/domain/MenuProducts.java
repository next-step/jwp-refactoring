package ktichenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import ktichenpos.menu.exception.NotExistMenuProductException;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<MenuProduct> elements = new ArrayList<>();

    protected MenuProducts() {

    }

    public MenuProducts(List<MenuProduct> inputs) {
        elements.addAll(inputs);
    }

    void addMenuProducts(List<MenuProduct> menuProducts) {
        elements.addAll(menuProducts);
    }

    void includeToMenu(Menu menu) {
        elements.forEach(menuProduct -> menuProduct.includeToMenu(menu));
    }

    public List<MenuProduct> toList() {
        return Collections.unmodifiableList(elements);
    }

    public List<Long> productIds() {
        return elements
                .stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    public long findQuantityByProductId(Long id) {
        return elements.stream()
                .filter(menuProduct -> menuProduct.getProductId().equals(id))
                .findFirst()
                .orElseThrow(NotExistMenuProductException::new)
                .getQuantity();
    }
}

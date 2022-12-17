package kitchenpos.domain;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.dto.ProductQuantityPair;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public void add(MenuProduct menuProduct) {
        if (!menuProducts.contains(menuProduct)) {
            this.menuProducts.add(menuProduct);
        }
    }

    public List<ProductQuantityPair> getProductQuantityPairs() {
        return this.menuProducts.stream()
            .map(menuProduct -> new ProductQuantityPair(menuProduct.getProduct(), menuProduct.getQuantity()))
            .collect(toList());
    }
}

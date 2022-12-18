package kitchenpos.menu.domain;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.product.domain.Product;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public void add(MenuProduct menuProduct) {
        if (!menuProducts.contains(menuProduct)) {
            this.menuProducts.add(menuProduct);
        }
    }

    public List<MenuProductResponse> getMenuProductResponse() {
        return this.menuProducts.stream()
            .map(menuProduct -> {
                Product product = menuProduct.getProduct();
                return new MenuProductResponse(product.getName(), product.getPrice(), menuProduct.getQuantity());
            })
            .collect(toList());
    }
}

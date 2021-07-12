package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public List<MenuProduct> list() {
        return Collections.unmodifiableList(menuProducts);
    }

    public void validationByPrice() {
        for (MenuProduct menuProduct : menuProducts) {
            validationByPrice(menuProduct.getProduct(), menuProduct.getMenu());
        }
    }

    private void validationByPrice(Product product, Menu menu) {
        BigDecimal totalPrice = calcTotalPrice(product);

        if (menu.getPrice().compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 `[메뉴의 수량] X [상품의 가격]` 보다 비쌀 수 없다.");
        }
    }

    private BigDecimal calcTotalPrice(final Product product) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        return sum;
    }

    public void add(final MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }
}

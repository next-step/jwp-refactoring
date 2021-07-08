package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> menuProducts;

    public MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public MenuProducts(List<MenuProductRequest> menuProductRequests, List<Product> findProducts) {
        List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map(menuProductRequest -> new MenuProduct(getMatchedProduct(menuProductRequest, findProducts), menuProductRequest.getQuantity()))
                .collect(Collectors.toList());

        this.menuProducts = menuProducts;
    }

    private Product getMatchedProduct(MenuProductRequest menuProductRequest, List<Product> findProducts) {
        return findProducts.stream()
                .filter(product -> product.sameProduct(menuProductRequest.getProductId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 상품입니다."));
    }

    public Price getProductTotalPrice() {
        return menuProducts.stream()
                .map(menuProduct -> menuProduct.getProduct()
                        .getPrice().multiply(menuProduct.getQuantity()))
                .reduce(new Price(BigDecimal.ZERO), Price::add)
                ;
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }

    public void matchMenu(Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.matchMenu(menu));
    }
}

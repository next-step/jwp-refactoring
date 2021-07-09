package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
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

    public MenuProducts(MenuRequest menuRequests, List<Product> findProducts) {

        List<MenuProduct> menuProducts = createMenuProduct(menuRequests, findProducts);

        priceValidator(menuRequests, findProducts);

        this.menuProducts = menuProducts;
    }

    private List<MenuProduct> createMenuProduct(MenuRequest menuRequests, List<Product> findProducts) {
        return menuRequests.getMenuProducts()
                .stream()
                .map(menuProductRequest -> new MenuProduct(getMatchedProduct(menuProductRequest, findProducts).getId(), menuProductRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    private void priceValidator(MenuRequest menuRequests, List<Product> findProducts) {
        Price totalPrice = menuRequests.getMenuProducts()
                .stream()
                .map(menuProductRequest -> getMatchedProduct(menuProductRequest, findProducts).getPrice().multiply(menuProductRequest.getQuantity()))
                .reduce(new Price(BigDecimal.ZERO), Price::add);

        if (totalPrice.compareTo(new Price(menuRequests.getPrice())) < 0) {
            throw new IllegalArgumentException("상품의 총 가격보다 메뉴의 가격이 더 높을수는 없습니다.");
        }
    }

    private Product getMatchedProduct(MenuProductRequest menuProductRequest, List<Product> findProducts) {
        return findProducts.stream()
                .filter(product -> product.sameProduct(menuProductRequest.getProductId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 상품입니다."));
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }

    public void matchMenu(Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.matchMenu(menu));
    }
}

package kitchenpos.menu.domain;

import kitchenpos.common.exception.InvalidPriceException;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.exception.NotExistProductException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
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

    public MenuProducts(List<MenuProductDto> menuProductDtos, List<Product> findProducts, Price menuPrice) {

        List<MenuProduct> menuProducts = createMenuProduct(menuProductDtos, findProducts);

        priceValidator(menuProductDtos, findProducts, menuPrice);

        this.menuProducts = menuProducts;
    }

    private List<MenuProduct> createMenuProduct(List<MenuProductDto> menuProductDtos, List<Product> findProducts) {
        return menuProductDtos
                .stream()
                .map(menuProductDto -> new MenuProduct(getMatchedProduct(menuProductDto, findProducts).getId(), menuProductDto.getQuantity()))
                .collect(Collectors.toList());
    }

    private void priceValidator(List<MenuProductDto> menuProductDtos, List<Product> findProducts, Price menuPrice) {
        Price totalPrice = menuProductDtos
                .stream()
                .map(menuProductDto -> getMatchedProduct(menuProductDto, findProducts).getPrice().multiply(menuProductDto.getQuantity()))
                .reduce(new Price(BigDecimal.ZERO), Price::add);

        if (totalPrice.compareTo(menuPrice) < 0) {
            throw new InvalidPriceException("상품의 총 가격보다 메뉴의 가격이 더 높을수는 없습니다.");
        }
    }

    private Product getMatchedProduct(MenuProductDto menuProductDto, List<Product> findProducts) {
        return findProducts.stream()
                .filter(product -> product.sameProduct(menuProductDto.getProductId()))
                .findFirst()
                .orElseThrow(() -> new NotExistProductException("존재하지 않는 상품입니다."));
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }

    public void matchMenu(Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.matchMenu(menu));
    }
}

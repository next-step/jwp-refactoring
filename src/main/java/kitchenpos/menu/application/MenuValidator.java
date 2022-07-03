package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.Exception.InvalidMenuPriceException;
import kitchenpos.Exception.NotFoundProductException;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;

    public MenuValidator(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        List<Long> menuProductIds = menu.getMenuProducts().getMenuProducts().stream().map(MenuProduct::getProductId)
                .collect(Collectors.toList());

        List<Product> products = productRepository.findByIdIn(menuProductIds);

        validateNotFoundProduct(menu, products);
        validateSumPrice(menu, products);
    }

    private void validateNotFoundProduct(Menu menu, List<Product> products) {
        if (menu.getMenuProductsSize() != products.size()) {
            throw new NotFoundProductException("존재하지 않는 상품을 메뉴상품으로 등록할 수 없습니다.");
        }
    }

    private void validateSumPrice(Menu menu, List<Product> products) {
        Price sumOfMenuProductPrice = sumOfProductPrice(menu, products);
        if (menu.isMoreExpensiveThanMenuProductsPrice(sumOfMenuProductPrice)) {
            throw new InvalidMenuPriceException(menu.getPrice(), sumOfMenuProductPrice);
        }
    }

    private Price sumOfProductPrice(Menu menu, List<Product> products) {

        List<MenuProduct> menuProducts = menu.getMenuProducts().getMenuProducts();

        return menuProducts.stream()
                .map(menuProduct -> calculateMenuProductPrice(products, menuProduct))
                .reduce(Price.from(BigDecimal.ZERO), Price::add);
    }

    private Price calculateMenuProductPrice(List<Product> products, MenuProduct menuProduct) {
        return calculatePrice(findProduct(products, menuProduct), menuProduct);
    }

    private Price calculatePrice(Product product, MenuProduct menuProduct) {
        return product.priceByQuantity(menuProduct.getQuantity());
    }

    private Product findProduct(List<Product> products, MenuProduct menuProduct) {
        Product product = products.stream()
                .filter(p -> p.isEqualToId(menuProduct.getProductId())).findFirst().orElseThrow(
                        NotFoundProductException::new);
        return product;
    }

}

package kitchenpos.menu.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.common.domain.Price;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Products;
import kitchenpos.product.exception.NotFoundProductException;
import kitchenpos.menu.exception.NotCorrectMenuPriceException;

@Component
public class MenuValidator {
    private final ProductService productService;

    public MenuValidator (
        final ProductService productService
    ) {
        this.productService = productService;
    }

    public void checkAllFindProducts(MenuProducts menuProducts) {
        Products products = findProducts(menuProducts);

        if (menuProducts.size() != products.size()) {
            throw new NotFoundProductException("요청된 상품과 조회된 상품의 수가 일치하지 않습니다.");
        }
    }

    public void checkMenuPrice(Price menuPrice, MenuProducts menuProducts) {
        Products products = findProducts(menuProducts);

        Price sumOfProductsPrice = getSumOfProductsPrice(menuProducts, products);

        if (menuPrice.compareTo(sumOfProductsPrice) > 0) {
            throw new NotCorrectMenuPriceException("메뉴의 가격은 상품들의 가격의 합보다 클 수 없습니다.");
        }
    }

    private Products findProducts(MenuProducts menuProducts) {
        List<Long> productIds = menuProducts.getProductIds().stream()
                                            .map(ProductId::value)
                                            .collect(Collectors.toList());

        return Products.of(productService.findAllByIds(productIds));
    }

    public Price getSumOfProductsPrice(MenuProducts menuProducts, Products products) {
        Price sumOfProductsPrice = Price.of(0);

        for (int index = 0; index < menuProducts.size(); index++) {
            MenuProduct menuProduct = menuProducts.get(index);
            Product product = products.findById(menuProduct.getProductId().value());

            sumOfProductsPrice = sumOfProductsPrice.add(product.getPrice().multiply(menuProduct.getQuantity()));
        }

        return sumOfProductsPrice;
    }
}

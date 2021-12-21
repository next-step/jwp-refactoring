package kitchenpos.application.menu;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.application.product.ProductService;
import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.Products;
import kitchenpos.exception.menu.NotCorrectMenuPriceException;
import kitchenpos.exception.product.NotFoundProductException;
import kitchenpos.vo.ProductId;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductService productService;

    public MenuValidator(
            final MenuGroupRepository menuGroupRepository,
            final ProductService productService
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.productService = productService;
    }

    public void validateForCreate(Menu menu) {
        checkAllFindProducts(menu);
        checkMenuPrice(menu);
    }

    private void checkAllFindProducts(Menu menu) {
        List<Long> productIds = menu.getProductIds().stream().map(ProductId::value).collect(Collectors.toList());
        Products products = Products.of(productService.findAllByIds(productIds));

        if (productIds.size() != products.size()) {
            throw new NotFoundProductException();
        }
    }

    private void checkMenuPrice(Menu menu) {
        List<Long> productIds = menu.getProductIds().stream().map(ProductId::value).collect(Collectors.toList());
        Products products = Products.of(productService.findAllByIds(productIds));

        Price sumOfProductsPrice = Price.of(0);

        for (int index = 0; index < menu.getMenuProducts().size(); index++) {
            MenuProduct menuProduct = menu.getMenuProducts().get(index);
            Product product = products.findById(menuProduct.getProductId().value());

            sumOfProductsPrice = sumOfProductsPrice.add(product.getPrice().multiply(menuProduct.getQuantity()));
        }

        if (menu.getPrice().compareTo(sumOfProductsPrice) > 0) {
            throw new NotCorrectMenuPriceException();
        }
    }
}

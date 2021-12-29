package kitchenpos.menu.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.common.exception.KitchenposNotFoundException;
import kitchenpos.common.price.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        Price sum = calculateSum(menu.getMenuProductList());
        menu.validatePrice(sum);
    }

    private Price calculateSum(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(menuProduct -> {
                Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(KitchenposNotFoundException::new);

                return menuProduct.calculatePrice(product.getPrice());
            })
            .reduce(Price.ZERO, Price::add);
    }
}

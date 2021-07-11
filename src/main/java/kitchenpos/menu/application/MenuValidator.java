package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.exception.MenuPriceExceedException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validationMenuProductPrices(Price menuPrice, List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);

        Price sumPrice = products.stream()
            .map(Product::getPrice)
            .reduce(new Price(0), Price::plus);

        if (menuPrice.compareTo(sumPrice) > 0) {
            throw new MenuPriceExceedException();
        }
    }

}

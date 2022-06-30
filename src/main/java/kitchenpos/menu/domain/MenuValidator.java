package kitchenpos.menu.domain;

import java.util.List;
import kitchenpos.exception.InvalidPriceException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateProductsTotalPrice(Menu menu) {
        final List<Product> products = productRepository.findAll();
        final Price totalPrice = menu.products().totalPrice(products);
        if (menu.isMoreThan(totalPrice)) {
            throw new InvalidPriceException("제품의 합은 메뉴 가격보다 작을 수 없습니다.");
        }
    }
}

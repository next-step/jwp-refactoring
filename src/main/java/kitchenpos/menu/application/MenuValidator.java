package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.common.ErrorMessage;
import kitchenpos.exception.InvalidPriceException;
import kitchenpos.exception.NotExistException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.dto.MenuProductRequest;
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

    public Long findProductId(MenuProductRequest menuProduct) {
        return productRepository.findById(menuProduct.getProductId())
                .orElseThrow(() -> new NotExistException(ErrorMessage.NOT_EXIST_PRODUCT.message()))
                .getId();
    }
}

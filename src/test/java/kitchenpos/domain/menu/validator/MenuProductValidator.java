package kitchenpos.domain.menu.validator;

import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;

@Component
public class MenuProductValidator {
    private static final String NOT_EXIST_MENU = "Menu 가 존재하지 않습니다.";
    private static final String NOT_EXIST_PRODUCT = "Product 가 존재하지 않습니다.";

    private final ProductRepository productRepository;

    public MenuProductValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateMenuProduct(MenuProduct menuProduct) {
        validateExistMenu(menuProduct.getMenu());
        validateProduct(menuProduct.getProductId());
    }

    private void validateExistMenu(Menu menu) {
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException(NOT_EXIST_MENU);
        }
    }

    private void validateProduct(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);

        if (!productOpt.isPresent()) {
            throw new EntityNotFoundException(NOT_EXIST_PRODUCT);
        }
    }
}

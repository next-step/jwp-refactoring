package kitchenpos.menu.validator;

import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.menu.exception.MenuExceptionType;
import kitchenpos.menu.persistence.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.exception.ProductException;
import kitchenpos.product.exception.ProductPriceException;
import kitchenpos.product.exception.ProductPriceExceptionType;
import kitchenpos.product.persistence.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static kitchenpos.product.exception.ProductExceptionType.NONE_EXISTS_PRODUCT;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validatePrice(MenuRequest request) {
        BigDecimal amount = getAmount(request);
        MenuPrice price = MenuPrice.of(request.getPrice());
        if(price.isExceedPrice(amount)){
            throw new MenuException(MenuExceptionType.EXCEED_MENU_PRODUCT_PRICE);
        };
    }

    private BigDecimal getAmount(MenuRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(it -> findProductById(it.getProductId()).getPrice()
                        .multiply(BigDecimal.valueOf(it.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductException(NONE_EXISTS_PRODUCT));
    }
}

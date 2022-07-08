package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.common.exception.InvalidValueException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(MenuRequest menuRequest) {
        validateMenuProductsPriceThanMenuPrice(menuRequest.getPrice(), menuRequest.toMenuProducts());
    }

    private void validateMenuProductsPriceThanMenuPrice(BigDecimal price, List<MenuProduct> menuProducts) {
        BigDecimal totalPrice = calculateMenuProductsPrice(menuProducts);

        if(price.compareTo(totalPrice) > 0) {
            throw new InvalidValueException();
        }
    }

    private BigDecimal calculateMenuProductsPrice(List<MenuProduct> menuProducts) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for(final MenuProduct menuProduct : menuProducts) {
            final Product product = findProductById(menuProduct.getProductId());

            BigDecimal multiply = product.getPrice()
                .multiply(new BigDecimal(menuProduct.getQuantity()));

            totalPrice = totalPrice.add(multiply);
        }

        return totalPrice;
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(EntityNotFoundException::new);
    }
}

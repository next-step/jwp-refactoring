package kitchenpos.menu.validator;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.exception.ErrorCode;
import kitchenpos.exception.KitchenposException;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateCreate(MenuRequest menuRequest){
        validatePriceGreaterThanSum(menuRequest.getMenuProducts(), menuRequest.getPrice());
    }

    private void validatePriceGreaterThanSum(List<MenuProductRequest> menuProducts, BigDecimal price) {
        if (price.compareTo(getSumPriceFromMenuProducts(menuProducts)) > 0) {
            throw new KitchenposException(ErrorCode.PRICE_GREATER_THAN_SUM);
        }
    }

    private BigDecimal getSumPriceFromMenuProducts(List<MenuProductRequest> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;

        for (final MenuProductRequest menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new KitchenposException(ErrorCode.NOT_FOUND_PRODUCT));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        return sum;
    }
}

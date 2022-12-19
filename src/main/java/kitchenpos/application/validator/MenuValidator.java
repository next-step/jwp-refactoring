package kitchenpos.application.validator;

import static kitchenpos.exception.ErrorCode.PRICE_IS_NULL_OR_MINUS;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.exception.ErrorCode;
import kitchenpos.exception.KitchenposException;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateCreate(MenuRequest menuRequest){
        validatePrice(menuRequest.getPrice());
        validatePriceGreaterThanSum(menuRequest.getMenuProducts(), menuRequest.getPrice());
    }

    private void validatePrice(BigDecimal price){
        if(validatePriceNull(price) || validatePriceLessThanZero(price)){
            throw new KitchenposException(PRICE_IS_NULL_OR_MINUS);
        }
    }

    private boolean validatePriceNull(BigDecimal price){
        return Objects.isNull(price);
    }

    private boolean validatePriceLessThanZero(BigDecimal price){
        return price.compareTo(BigDecimal.ZERO) < 0;
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

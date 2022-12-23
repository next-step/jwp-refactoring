package kitchenpos.menu.application.validator;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.ErrorCode;
import kitchenpos.common.exception.KitchenposException;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.request.MenuRequest;
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
        if (Price.of(price).compareTo(getTotalPriceFromMenuProducts(menuProducts)) > 0) {
            throw new KitchenposException(ErrorCode.PRICE_GREATER_THAN_SUM);
        }
    }

    private Price getTotalPriceFromMenuProducts(List<MenuProductRequest> menuProducts){
        return menuProducts.stream()
                .map(this::getMenuProductPrice)
                .reduce(Price::add)
                .orElse(Price.of(BigDecimal.ZERO));
    }

    private Price getMenuProductPrice(MenuProductRequest menuProduct){
        return getProductPrice(menuProduct.getProductId()).multiply(menuProduct.getQuantity());
    }

    private Price getProductPrice(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new KitchenposException(ErrorCode.NOT_FOUND_PRODUCT))
                .getPrice();
    }
}

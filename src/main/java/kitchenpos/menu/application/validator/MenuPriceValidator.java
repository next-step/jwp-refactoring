package kitchenpos.menu.application.validator;

import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.CannotCreateException;
import kitchenpos.common.exception.ExceptionType;
import kitchenpos.menu.domain.request.MenuProductRequest;
import kitchenpos.menu.domain.request.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MenuPriceValidator implements MenuValidator {

    private final ProductRepository productRepository;

    public MenuPriceValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void execute(MenuRequest menuRequest) {
        validatePriceOverMenuPrice(menuRequest.getMenuProductRequests(), new Price(menuRequest.getPrice()));
    }

    public void validatePriceOverMenuPrice(List<MenuProductRequest> menuProductRequests, Price price) {
        Price sum = new Price(BigDecimal.ZERO);
        for (MenuProductRequest item : menuProductRequests) {
            Product product = findByProductId(item.getProductId());
            Price menuProductPrice = new Price(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            sum = sum.add(menuProductPrice);
        }

        if (price.isOverThan(sum)) {
            throw new CannotCreateException(ExceptionType.IS_NOT_OVER_THAN_MENU_PRICE);
        }
    }

    private Product findByProductId(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CannotCreateException(ExceptionType.NOT_EXIST_PRODUCT));
    }
}

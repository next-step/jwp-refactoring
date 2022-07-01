package kitchenpos.menu.application.validator;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.domain.Price;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

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

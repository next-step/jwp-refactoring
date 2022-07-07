package kitchenpos.menu.application.validator;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MenuPriceValidator implements MenuValidator{
    private final ProductService productService;

    public MenuPriceValidator(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void validate(MenuRequest request) {
        Price price = new Price(request.getPrice());
        Price sum = new Price(BigDecimal.ZERO);
        for (MenuProductRequest item : request.getMenuProducts()) {
            Product product = productService.findByProductId(item.getProductId());
            Price menuProductPrice = new Price(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            sum = sum.add(menuProductPrice);
        }

        if (price.isOverThan(sum)) {
            throw new BadRequestException(ErrorCode.INVALID_MENU_PRICE);
        }
    }
}

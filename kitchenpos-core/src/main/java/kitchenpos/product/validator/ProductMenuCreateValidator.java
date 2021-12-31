package kitchenpos.product.validator;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProductGroup;
import kitchenpos.menu.exception.IllegalMenuPriceException;
import kitchenpos.menu.validator.MenuPriceMenuCreateValidator;
import kitchenpos.product.Product;
import kitchenpos.product.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ProductMenuCreateValidator implements MenuPriceMenuCreateValidator {
    private static final String ILLEGAL_PRICE_ERROR_MESSAGE = "가격은 포함된 구성된 상품들의 금액 보다 작거나 같아야 한다.";
    private final ProductRepository productRepository;

    public ProductMenuCreateValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        final BigDecimal totalPrice = calcTotalPrice(menu.getMenuProductGroup());
        if (menu.isLessThenMenuPrice(totalPrice)) {
            throw new IllegalMenuPriceException(ILLEGAL_PRICE_ERROR_MESSAGE);
        }
    }

    private BigDecimal calcTotalPrice(MenuProductGroup menuProductGroup) {
        final List<Long> productIds = menuProductGroup.getProductIds();
        final List<Product> products = productRepository.findAllByIds(productIds);
        return menuProductGroup.calcTotalPrice(products);
    }
}

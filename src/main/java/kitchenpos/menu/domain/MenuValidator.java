package kitchenpos.menu.domain;

import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@Component
public class MenuValidator {

    public static final String MENU_PRICE_EXCEPTION_MESSAGE = "메뉴의 가격이 메뉴 상품의 합보다 클 수 없다.";

    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        validateExistProduct(menu.getMenuProducts().getMenuProducts());
        validatePrice(menu.getPrice(), menu.getMenuProducts());
    }

    private void validateExistProduct(List<MenuProduct> menuProducts) {
        for (final MenuProduct menuProduct : menuProducts) {
            productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
        }
    }

    private void validatePrice(Price price, MenuProducts menuProducts) {
        if (price.getPrice().compareTo(sum(menuProducts.getMenuProducts())) > 0) {
            throw new IllegalArgumentException(MENU_PRICE_EXCEPTION_MESSAGE);
        }
    }

    private BigDecimal sum(List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(findProduct(menuProduct.getProductId()).getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);
    }
}

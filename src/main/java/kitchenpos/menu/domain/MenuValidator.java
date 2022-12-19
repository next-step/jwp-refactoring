package kitchenpos.menu.domain;

import java.math.BigDecimal;
import kitchenpos.constants.ErrorMessages;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validateBeforeCreateMenu(Menu menu) {
        menu.validateName();
        checkMenuGroupExist(menu);
        validateMenuPrice(getMenuProductPriceSum(menu.getMenuProducts()), menu.getPrice());
    }

    private void checkMenuGroupExist(Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException(ErrorMessages.MENU_GROUP_DOES_NOT_EXIST);
        }
    }

    private void validateMenuPrice(MenuPrice menuProductPriceSum, MenuPrice menuPrice) {
        if (menuPrice.compareTo(menuProductPriceSum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private MenuPrice getMenuProductPriceSum(MenuProducts menuProducts) {
        MenuPrice sum = new MenuPrice(BigDecimal.ZERO);
        menuProducts.stream()
                .forEach(menuProduct -> {
                    ProductPrice productPrice = findProductAndGetPrice(menuProduct);
                    sum.add(productPrice.multiply(menuProduct.getQuantity()));
                });
        return sum;
    }

    private ProductPrice findProductAndGetPrice(MenuProduct menuProduct) {
        final Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
        return product.getPrice();
    }
}

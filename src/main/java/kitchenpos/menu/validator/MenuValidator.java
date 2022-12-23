package kitchenpos.menu.validator;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.port.MenuGroupPort;
import kitchenpos.product.domain.Product;
import kitchenpos.product.port.ProductPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static kitchenpos.common.constants.ErrorCodeType.MENU_PRICE_NOT_OVER_SUM_PRICE;

@Service
@Transactional
public class MenuValidator {
    private final MenuGroupPort menuGroupPort;
    private final ProductPort productPort;

    public MenuValidator(MenuGroupPort menuGroupPort, ProductPort productPort) {
        this.menuGroupPort = menuGroupPort;
        this.productPort = productPort;
    }


    public void validCheckMakeMenu(Menu menu) {
        validCheckIsExistMenuGroup(menu.getMenuGroupId());
        validCheckMeuProductPrice(menu);
    }

    private void validCheckIsExistMenuGroup(Long menuGroupId) {
        menuGroupPort.findById(menuGroupId);
    }

    public void validCheckMeuProductPrice(Menu menu) {
        BigDecimal amount = getAmount(menu);
        MenuPrice menuPrice = new MenuPrice(menu.getMenuPrice());

        if (menuPrice.isBiggerThen(amount)) {
            throw new IllegalArgumentException(MENU_PRICE_NOT_OVER_SUM_PRICE.getMessage());
        }
    }


    private BigDecimal getAmount(Menu menu) {
        return menu.getMenuProducts()
                .stream()
                .map(this::getProductPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getProductPrice(MenuProduct menuProduct) {
        Product product = productPort.findById(menuProduct.getProductId());
        return product.getCalculateMultiplyQuantity(menuProduct.getQuantity());
    }
}


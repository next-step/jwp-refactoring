package kitchenpos.menu.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.ProductRepository;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository,
        ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validateCreate(Menu menu) {
        validateExistMenuGroup(menu.getMenuGroupId());
        validateMenuPrice(menu);
    }

    private void validateMenuPrice(Menu menu) {
        BigDecimal sumPrice = sumProductsPrice(menu.getMenuProducts().getValue());
        if (menu.getPrice().isGreaterThanSumPrice(sumPrice)) {
            throw new BadRequestException(WRONG_VALUE);
        }
    }

    private void validateExistMenuGroup(Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId).orElseThrow(() -> new NotFoundException(NOT_FOUND_DATA));
    }

    private BigDecimal sumProductsPrice(List<MenuProduct> menuProducts) {
        return menuProducts.stream().map(menuProduct -> findProductPrice(menuProduct.getProductId())
                .multiplyQuantity(menuProduct.getQuantity()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Price findProductPrice(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_DATA))
            .getPrice();
    }

}

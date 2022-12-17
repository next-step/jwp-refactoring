package kitchenpos.menu.application;

import org.springframework.stereotype.Component;

import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.ProductPrice;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductPriceCalculator menuProductPriceCalculator;

    public MenuValidator(MenuGroupRepository menuGroupRepository, MenuProductPriceCalculator menuProductPriceCalculator) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductPriceCalculator = menuProductPriceCalculator;
    }

    public void validate(MenuRequest menuRequest) {
        validateMenuGroupExists(menuRequest);
        validateMenuProductSum(menuRequest);
    }

    private void validateMenuGroupExists(MenuRequest menuRequest) {
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }
    }

    private void validateMenuProductSum(MenuRequest menuRequest) {
        ProductPrice sum = menuProductPriceCalculator.calculateSum(menuRequest.getMenuProducts());
        if (sum.isLessThan(menuRequest.getPrice())) {
            throw new IllegalArgumentException("메뉴 가격은 상품 가격의 합계보다 비쌀 수 없습니다.");
        }
    }
}

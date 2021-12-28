package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.application.MenuProductService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Component
public class MenuValidator {
    private final MenuProductService menuProductService;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(MenuProductService menuProductService, MenuGroupRepository menuGroupRepository) {
        this.menuProductService = menuProductService;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(Menu menu) {
        checkMenuGroupIsValid(menu);
        checkMenuProductIsEmpty(menu);
        checkMenuPrice(menu);
    }

    private void checkMenuPrice(Menu menu) {
        BigDecimal totalPrice = menuProductService.calculateTotalPrice(menu.getMenuProducts());
        Price menuPrice = menu.getPrice();

        if (menuPrice.isMinus()) {
            throw new IllegalArgumentException("메뉴의 가격은 음수가 될 수 없습니다.");
        }

        if (menuPrice.isGreaterThan(totalPrice)) {
            throw new IllegalArgumentException("메뉴의 가격이 상품 가격의 합계보다 클 수 없습니다.");
        }
    }

    private void checkMenuGroupIsValid(Menu menu) {
        menuGroupRepository.findById(menu.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹은 필수 입력 항목입니다."));
    }

    private void checkMenuProductIsEmpty(Menu menu) {
        List<MenuProduct> menuProducts = menu.getMenuProducts().asList();
        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException("메뉴 상품 목록이 비어있습니다.");
        }
    }
}

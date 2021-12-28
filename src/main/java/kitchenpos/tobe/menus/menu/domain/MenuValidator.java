package kitchenpos.tobe.menus.menu.domain;

import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.tobe.common.DomainService;
import kitchenpos.tobe.common.domain.Validator;
import kitchenpos.tobe.menus.menu.infra.RestProductClient;
import kitchenpos.tobe.menus.menugroup.domain.MenuGroupRepository;

@DomainService
public class MenuValidator implements Validator<Menu> {

    private final MenuGroupRepository menuGroupRepository;
    private final RestProductClient restProductClient;

    public MenuValidator(
        final MenuGroupRepository menuGroupRepository,
        final RestProductClient restProductClient
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.restProductClient = restProductClient;
    }

    public void validate(final Menu menu) {
        menuPriceShouldBeLessThanOrEqualToMenuProductsPrice(menu);
        menuShouldBelongToRegisteredMenuGroup(menu.getMenuGroupId());
        menuShouldConsistOfRegisteredProducts(menu.getProductIds());
    }

    private void menuPriceShouldBeLessThanOrEqualToMenuProductsPrice(final Menu menu) {
        if (menu.isOverpriced()) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품 금액의 합보다 적거나 같아야 합니다.");
        }
    }

    private void menuShouldBelongToRegisteredMenuGroup(final Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
            .orElseThrow(() -> new NoSuchElementException("메뉴는 특정 메뉴 그룹에 속해야 합니다."));
    }

    private void menuShouldConsistOfRegisteredProducts(final List<Long> productIds) {
        if (!restProductClient.existAll(productIds)) {
            throw new NoSuchElementException("상품이 없으면 메뉴를 등록할 수 없습니다.");
        }
    }
}

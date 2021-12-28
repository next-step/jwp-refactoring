package kitchenpos.tobe.menus.menu.domain;

import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.tobe.common.DomainService;
import kitchenpos.tobe.common.domain.Validator;
import kitchenpos.tobe.menus.menugroup.domain.MenuGroupRepository;
import kitchenpos.tobe.product.domain.ProductRepository;

@DomainService
public class MenuValidator implements Validator<Menu> {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(
        final MenuGroupRepository menuGroupRepository,
        final ProductRepository productRepository
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
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
        final int sizeOfExpectedProducts = productIds.size();
        final int sizeOfActualProducts = productRepository.findAllByIdIn(productIds).size();
        if (sizeOfExpectedProducts != sizeOfActualProducts) {
            throw new NoSuchElementException("상품이 없으면 메뉴를 등록할 수 없습니다.");
        }
    }
}

package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.dao.MenuGroupRepository;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.dao.ProductRepository;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class MenuValidator {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                         ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validateOrderLineItemsCheck(List<Long> menuIds) {
        long menuCount = menuRepository.countByIdIn(menuIds);

        if (menuIds.size() != menuCount) {
            throw new IllegalArgumentException("존재하지 않는 메뉴입니다.");
        }
    }

    public void validateMenuGroupCheck(Long menuGroupId) {
        if(!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴그룹입니다.");
        }
    }

    public void validatePriceCheck(final MenuRequest menuRequest) {
        BigDecimal sum = BigDecimal.ZERO;

        for (final MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
            sum = sum.add(product.multiplyPrice(menuProductRequest.getQuantity()));
        }

        if (sum.compareTo(menuRequest.getPrice()) < 0) {
            throw new IllegalArgumentException("메뉴의 금액은 상품의 합 보다 작아야합니다.");
        }
    }
}

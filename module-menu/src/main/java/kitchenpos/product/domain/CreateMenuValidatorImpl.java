package kitchenpos.product.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.CreateMenuValidator;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CreateMenuValidatorImpl implements CreateMenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public CreateMenuValidatorImpl(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void validateCreateMenu(Long menuGroupId, BigDecimal price, List<MenuProduct> menuProducts) {
        menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다."));

        if (!getTotalPrice(menuProducts).isExpensiveThan(price)) {
            throw new IllegalArgumentException("메뉴 가격이 올바르지 않습니다. " + Price.of(price));
        }
    }

    public Price getTotalPrice(List<MenuProduct> menuProducts) {
        Price sum = Price.of(BigDecimal.ZERO);
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품 정보가 존재하지 않습니다."));

            sum = sum.add(product.getPrice().multiply(menuProduct.getQuantity()));
        }
        return sum;
    }
}

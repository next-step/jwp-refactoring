package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(final Menu menu) {
        validatePrice(menu);
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹을 찾을 수 없습니다");
        }
    }

    private void validatePrice(final Menu menu) {
        final BigDecimal price = menu.getPrice();

        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("가격은 필수값입니다");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 음수일 수 없습니다");
        }

        final BigDecimal totalPriceOfProducts = calculateTotalPriceOfProducts(menu.getMenuProducts());
        if (price.compareTo(totalPriceOfProducts) > 0) {
            throw new IllegalArgumentException("메뉴의 가격이 상품 가격 전체의 총합보다 클 수 없습니다");
        }
    }

    private BigDecimal calculateTotalPriceOfProducts(final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다"));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }
}

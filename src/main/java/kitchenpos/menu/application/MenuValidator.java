package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(ProductRepository productRepository, MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(MenuRequest menuRequest) {
        validatePriceNotNull(menuRequest.getPrice());
        validatePriceGreaterThanZero(menuRequest.getPrice());
        validateMenuGroupExists(menuRequest);
        validateMenuProductSum(menuRequest);
    }

    private void validatePriceNotNull(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("메뉴의 가격이 null일 수 없습니다.");
        }
    }

    private void validatePriceGreaterThanZero(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 1) {
            throw new IllegalArgumentException("메뉴의 가격이 0이하일 수 없습니다.");
        }
    }

    private void validateMenuGroupExists(MenuRequest menuRequest) {
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }
    }

    private void validateMenuProductSum(MenuRequest menuRequest) {
        BigDecimal sum = menuProductSum(menuRequest);
        if (menuRequest.getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 상품 가격의 합계보다 비쌀 수 없습니다.");
        }
    }

    private BigDecimal menuProductSum(MenuRequest menuRequest) {
        List<Product> products = productRepository.findAllById(menuRequest.getProductIds());
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProduct : menuRequest.getMenuProducts()) {
            BigDecimal price = products.stream()
                .filter(product -> product.hasId(menuProduct.getProductId()))
                .map(Product::getPrice)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 ID 입니다."));

            sum = sum.add(price.multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }
}

package kitchenpos.menu.application;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu_group.domain.MenuGroupRepository;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validateCreateMenu(MenuRequest menuRequest) {
        checkExistMenuGroup(menuRequest);

        checkOverPrice(menuRequest.getPrice(), menuRequest.getMenuProducts());
    }

    private void checkExistMenuGroup(MenuRequest menuRequest) {
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다");
        }
    }

    private void checkOverPrice(BigDecimal requestPrice, List<MenuProductRequest> menuProductRequests) {
        BigDecimal totalPrice = menuProductRequests.stream()
                .map(this::getMenuProductPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        if (requestPrice.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException("메뉴 금액이 전체 메뉴 상품 금액의 합보다 많습니다.");
        }
    }

    private BigDecimal getMenuProductPrice(MenuProductRequest menuProductRequest) {
        BigDecimal productPrice = getProductPrice(menuProductRequest.getProductId());


        return productPrice.multiply(BigDecimal.valueOf(menuProductRequest.getQuantity()));
    }

    private BigDecimal getProductPrice(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new)
                .getPrice();
    }
}

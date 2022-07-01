package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, final ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(MenuRequest menuRequest) {
        validateMenuGroup(menuRequest.getMenuGroupId());
        validatePrice(Price.from(menuRequest.getPrice()), menuRequest.getMenuProducts());
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
    }

    private void validatePrice(Price menuPrice, List<MenuProductRequest> menuProductRequests) {
        Price totalPrice = getMenuProductTotalPrice(menuProductRequests);
        if (totalPrice.isGreaterThan(menuPrice)) {
            throw new IllegalArgumentException("상품들 금액의 합이 메뉴 가격보다 클 수 없습니다");
        }
    }

    private Price getMenuProductTotalPrice(List<MenuProductRequest> menuProductRequests) {
        Price totalPrice = Price.from(0);
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = getProduct(menuProductRequest.getProductId());
            totalPrice.add(Price.multiply(product, menuProductRequest.getQuantity()));
        }

        return totalPrice;
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    }
}

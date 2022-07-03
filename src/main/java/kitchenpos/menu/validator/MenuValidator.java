package kitchenpos.menu.validator;

import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(MenuRequest menuRequest) {
        validateMenuGroup(menuRequest.getMenuGroupId());
        validateMenuPrice(new Price(menuRequest.getPrice()), menuRequest.getMenuProductRequests());
    }

    private void validateMenuGroup(Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴그룹입니다."));
    }

    private void validateMenuPrice(Price price, List<MenuProductRequest> menuProductRequests) {
        if (price.biggerThan(totalPrice(menuProductRequests))) {
            throw new IllegalArgumentException("메뉴 내 제품가격의 합보다 메뉴가격이 클 수 없습니다.");
        }
    }

    private Price totalPrice(List<MenuProductRequest> menuProductRequests) {
        Price totalPrice = new Price(Price.MIN_PRICE);
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = getProductById(menuProductRequest.getProductId());
            totalPrice.sum(product.getPrice());
        }
        return totalPrice;
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }
}

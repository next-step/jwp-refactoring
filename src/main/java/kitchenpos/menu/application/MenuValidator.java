package kitchenpos.menu.application;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.exception.MenuProductException;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validation(MenuRequest menuRequest) {
        validateMenuGroup(menuRequest.getMenuGroupId());
        validateProduct(menuRequest.getPrice(), menuRequest.getMenuProductRequests());
    }

    private void validateMenuGroup(Long menuGroupId) {
        if(!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("유효하지 않은 메뉴그룹입니다.");
        }
    }

    private void validateProduct(BigDecimal price, List<MenuProductRequest> menuProductRequests) {
        if (price.compareTo(menuProductsTotalPrice(menuProductRequests)) > 0) {
            throw new MenuProductException(MenuProductException.MENU_PRICE_MORE_EXPENSIVE_PRODUCTS_MSG);
        }
    }

    private BigDecimal menuProductsTotalPrice(List<MenuProductRequest> menuProductRequests) {
        BigDecimal total = BigDecimal.ZERO;
        for(MenuProductRequest menuProductRequest : menuProductRequests) {
            BigDecimal price = productPrice(menuProductRequest).multiply(BigDecimal.valueOf(menuProductRequest.getQuantity()));
            total = total.add(price);
        }
        return total;
    }

    private BigDecimal productPrice(MenuProductRequest menuProductRequest) {
        Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(NoSuchElementException::new);
        return product.getPrice();
    }
}

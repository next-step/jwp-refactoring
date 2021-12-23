package kitchenpos.menu.application;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu_group.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
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
        BigDecimal totalPrice = getTotalPrice(menuProductRequests);

        if (requestPrice.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException("메뉴 금액이 전체 메뉴 상품 금액의 합보다 많습니다.");
        }
    }

    private BigDecimal getTotalPrice(List<MenuProductRequest> menuProductRequests) {
        List<Product> products = getProducts(menuProductRequests);

        return menuProductRequests.stream()
                .map(getMenuProductPrice(products))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private List<Product> getProducts(List<MenuProductRequest> menuProductRequests) {
        List<Long> productIds = menuProductRequests.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());

        return productRepository.findAllById(productIds);
    }

    private Function<MenuProductRequest, BigDecimal> getMenuProductPrice(List<Product> products) {
        return menuProductRequest -> {
            Product product = findMatchProduct(products, menuProductRequest);
            BigDecimal productPrice = product.getPrice();

            return productPrice.multiply(BigDecimal.valueOf(menuProductRequest.getQuantity()));
        };
    }

    private Product findMatchProduct(List<Product> products, MenuProductRequest menuProductRequest) {
        return products.stream()
                .filter(product -> product.getId().equals(menuProductRequest.getProductId()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

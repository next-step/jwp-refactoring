package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(MenuRequest menuRequest) {
        validateMenuGroup(menuRequest.getMenuGroupId());
        validateProductsPrice(Price.from(menuRequest.getPrice()), menuRequest.getMenuProducts());

    }

    private void validateProductsPrice(Price price, List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = getMenuProducts(menuProductRequests);
        BigDecimal productAmount = menuProducts.stream()
                .map(MenuProduct::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        if (price.biggerThan(productAmount)) {
            throw new IllegalArgumentException("가격이 상품들의 가격의 합보다 클 수 없습니다.");
        }

    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴그룹이 존재하지 않습니다.");
        }
    }

    private List<MenuProduct> getMenuProducts(List<MenuProductRequest> request) {
        return request.stream()
                .map(this::getMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct getMenuProduct(MenuProductRequest menuProductRequest) {
        final Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
        return MenuProduct.from(product, menuProductRequest.getQuantity());
    }
}

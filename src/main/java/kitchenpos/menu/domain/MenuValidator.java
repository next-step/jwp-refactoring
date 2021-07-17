package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(final MenuRequest menuRequest) {
        validateName(menuRequest.getName());
        validateMenuGroup(menuRequest.getMenuGroupId());
        validateProduct(menuProductRequests(menuRequest));
        final Price price = new Price(menuRequest.getPrice());
        validateMenuPrice(price, priceOfProducts(menuProductRequests(menuRequest)));
    }

    private void validateMenuPrice(final Price price, final BigDecimal priceOfProducts) {
        if (price.greaterThan(priceOfProducts)) {
            throw new IllegalArgumentException("요청한 금액은 전체 메뉴별 가격보다 클 수 없습니다.");
        }
    }

    private List<MenuProductRequest> menuProductRequests(final MenuRequest menuRequest) {
        return menuRequest.getMenuProducts();
    }

    private void validateName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("메뉴명이 없습니다.");
        }
    }

    private void validateMenuGroup(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
    }

    private void validateProduct(final List<MenuProductRequest> menuProducts) {
        if (CollectionUtils.isEmpty(menuProducts)) {
            throw new IllegalArgumentException("제품 목록이 비어 있습니다.");
        }

        menuProducts.stream()
            .map(MenuProductRequest::getProductId)
            .forEach(id -> {
                if (!productRepository.existsById(id)) {
                    throw new IllegalArgumentException("제품이 존재하지 않습니다.");
                }
            });
    }

    private BigDecimal priceOfProducts(final List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
            .map(request -> findPriceOfProduct(request).multiply(quantity(request)))
            .reduce(BigDecimal::add)
            .orElseThrow(IllegalArgumentException::new);
    }

    private BigDecimal quantity(final MenuProductRequest request) {
        final Quantity quantity = new Quantity(request.getQuantity());

        return BigDecimal.valueOf(quantity.value());
    }

    private BigDecimal findPriceOfProduct(final MenuProductRequest request) {
        return productRepository.findById(request.getProductId())
            .map(Product::getPrice)
            .orElseThrow(IllegalArgumentException::new);
    }
}

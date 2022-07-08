package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, MenuRepository menuRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
    }

    public void validate(MenuRequest menuRequest) {
        validateMenuGroup(menuRequest.getMenuGroupId());
        validateProductsPrice(Price.from(menuRequest.getPrice()), menuRequest.getMenuProducts());

    }

    private void validateProductsPrice(Price price, List<MenuProductRequest> menuProductRequests) {
        Price totalPrice = totalPriceOfProducts(menuProductRequests);

        if (price.biggerThan(totalPrice)) {
            throw new IllegalArgumentException("가격이 상품들의 가격의 합보다 클 수 없습니다.");
        }
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴그룹이 존재하지 않습니다.");
        }
    }

    private Price totalPriceOfProducts(List<MenuProductRequest> menuProductRequests) {
        Price total = Price.from(0);
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = getProduct(menuProductRequest);
            Price price = Price.multiply(product, menuProductRequest.getQuantity());
            total = total.add(price);
        }

        return total;
    }

    private Product getProduct(MenuProductRequest menuProductRequest) {
        return productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    }

    public void validateOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        List<Long> menuIds = getMenuIds(orderLineItemRequests);
        int countById = menuRepository.countByIdIn(menuIds);
        if (countById != menuIds.size()) {
            throw new IllegalArgumentException("메뉴가 존재하지 않습니다.");
        }

    }

    private List<Long> getMenuIds(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }
}

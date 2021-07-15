package kitchenpos.order.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.generic.exception.IllegalOperationException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductOption;
import kitchenpos.menu.domain.MenuOption;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.generic.exception.MenuDetailMismatchException;
import kitchenpos.generic.exception.MenuMismatchException;
import kitchenpos.generic.exception.MenuNotFoundException;
import kitchenpos.generic.exception.OrderTableNotFoundException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductOption;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Products;
import kitchenpos.generic.exception.ProductMismatchException;
import kitchenpos.generic.exception.ProductNotFoundException;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, ProductRepository productRepository,
            OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Order order) {
        validate(order, getOrderTable(order));
    }

    void validate(Order order, OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalOperationException("빈 테이블에 주문할 수 없습니다.");
        }

        if (order.getOrderLineItems().isEmpty()) {
            throw new IllegalArgumentException("주문 상세 내역은 하나 이상 존재해야 합니다.");
        }

        order.getOrderLineItems()
            .forEach(item -> validateOrderLineItem(item, getMenu(item)));
    }

    private void validateOrderLineItem(OrderLineItem orderLineItem, Menu menu) {
        MenuOption menuOption = orderLineItem.getMenuOption();
        if (!menu.isSatisfiedBy(menuOption)) {
            throw new MenuMismatchException("메뉴가 변경되었습니다.");
        }

        validateOrderLineItemDetail(orderLineItem.getOrderLineItemDetails(), menu.getMenuProducts());
    }

    private void validateOrderLineItemDetail(OrderLineItemDetails orderLineItemDetails, MenuProducts menuProducts) {
        List<MenuProductOption> menuProductOptions = orderLineItemDetails.toMenuDetailOptions();
        if (!menuProducts.isSatisfiedBy(menuProductOptions)) {
            throw new MenuDetailMismatchException("메뉴 세부항목이 변경되었습니다.");
        }

        validateProduct(orderLineItemDetails, getProducts(menuProducts));
    }

    private void validateProduct(OrderLineItemDetails orderLineItemDetails, Products products) {
        List<ProductOption> productOptions = orderLineItemDetails.toProductOptions();
        if (!products.isSatisfiedBy(productOptions)) {
            throw new ProductMismatchException("제품 정보가 변경되었습니다.");
        }
    }

    public void checkChangeable(Order order) {
        if (order.isCompleted()) {
            throw new IllegalOperationException("완결 된 주문은 상태를 변경할 수 없습니다.");
        }
    }

    private Menu getMenu(OrderLineItem orderLineItem) {
        return menuRepository.findById(orderLineItem.getMenuId())
            .orElseThrow(() -> new MenuNotFoundException("해당 ID의 메뉴가 존재하지 않습니다."));
    }

    private Products getProducts(MenuProducts menuProducts) {
        return Products.of(menuProducts.mapList(this::getProduct));
    }

    private Product getProduct(MenuProduct menuProduct) {
        return productRepository.findById(menuProduct.getProductId())
            .orElseThrow(() -> new ProductNotFoundException("해당 ID의 제품이 존재하지 않습니다."));
    }

    private OrderTable getOrderTable(Order order) {
        return orderTableRepository.findById(order.getOrderTableId())
            .orElseThrow(() -> new OrderTableNotFoundException("해당 ID 의 주문 테이블이 존재하지 않습니다."));
    }
}

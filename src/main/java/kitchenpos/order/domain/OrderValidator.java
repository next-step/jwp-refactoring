package kitchenpos.order.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.generic.exception.IllegalOperationException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDetailOption;
import kitchenpos.menu.domain.MenuOption;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.MenuMismatchException;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.order.exception.OrderTableNotFoundException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductMismatchException;
import kitchenpos.product.exception.ProductNotFoundException;

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

        List<MenuDetailOption> menuDetailOptions = orderLineItem.getMenuDetailOptions();
        if (!menu.getMenuProducts().isSatisfiedBy(menuDetailOptions)) {
            throw new MenuMismatchException("메뉴 세부항목이 변경되었습니다.");
        }

        menuDetailOptions
            .forEach(option -> validateDetailProduct(option, getProduct(option)));
    }

    private void validateDetailProduct(MenuDetailOption menuDetailOption, Product product) {
        if (!product.isSatisfiedBy(menuDetailOption)) {
            throw new ProductMismatchException("제품 세부 항목이 변경되었습니다.");
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

    private Product getProduct(MenuDetailOption menuDetailOption) {
        return productRepository.findById(menuDetailOption.getProductId())
            .orElseThrow(() -> new ProductNotFoundException("해당 ID의 제품이 존재하지 않습니다."));
    }

    private OrderTable getOrderTable(Order order) {
        return orderTableRepository.findById(order.getOrderTableId())
            .orElseThrow(() -> new OrderTableNotFoundException("해당 ID 의 주문 테이블이 존재하지 않습니다."));
    }
}

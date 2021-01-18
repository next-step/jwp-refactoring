package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.creator.MenuGroupHelper;
import kitchenpos.application.creator.MenuHelper;
import kitchenpos.application.creator.MenuProductHelper;
import kitchenpos.application.creator.OrderLineItemHelper;
import kitchenpos.application.creator.OrderTableHelper;
import kitchenpos.application.creator.ProductHelper;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuDto;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderDto;
import kitchenpos.dto.OrderLineItemCreateRequest;
import kitchenpos.domain.OrderStatus;
import kitchenpos.repository.MenuGroupDao;
import kitchenpos.repository.OrderTableDao;
import kitchenpos.repository.ProductDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-10
 */
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("주문 생성 테스트")
    @Test
    void orderCreateTest() {
        Long orderTableId = getSavedOrderTable().getId();
        OrderLineItemCreateRequest savedOrderLineItem = getSavedOrderLineItem();

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTableId, Collections.singletonList(savedOrderLineItem));

        OrderDto savedOrder = orderService.create(orderCreateRequest);

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(savedOrder.getOrderTableId()).isEqualTo(orderCreateRequest.getOrderTableId());
        assertThat(savedOrder.getOrderLineItems().get(0).getSeq()).isNotNull();
        assertThat(savedOrder.getOrderLineItems().get(0).getMenuId()).isEqualTo(orderCreateRequest.getOrderLineItems().get(0).getMenuId());
        assertThat(savedOrder.getOrderLineItems().get(0).getQuantity()).isEqualTo(orderCreateRequest.getOrderLineItems().get(0).getQuantity());
        assertThat(savedOrder.getOrderLineItems().get(0).getOrderId()).isEqualTo(savedOrder.getId());
    }

    @DisplayName("주문 생성시 주문항목이 없는 경우")
    @Test
    void orderCreateWithOrderLineItemsTest() {
        OrderTable orderTable = orderTableDao.save(OrderTableHelper.createRequest(false).toEntity());
        OrderCreateRequest order = new OrderCreateRequest(orderTable.getId(), null);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성시 중복되는 주문항목이 있는 경우")
    @Test
    void orderCreateWithDuplicateOrderLineItemsTest() {
        OrderTable orderTable = orderTableDao.save(OrderTableHelper.createRequest(false).toEntity());
        OrderLineItemCreateRequest orderLineItem = getSavedOrderLineItem();
        OrderCreateRequest order = new OrderCreateRequest(orderTable.getId(), Arrays.asList(orderLineItem, orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성시 주문항목이 비어있는 경우")
    @Test
    void orderCreateWithEmptyOrderLineItemsTest() {
        OrderTable orderTable = orderTableDao.save(OrderTableHelper.createRequest(false).toEntity());
        OrderCreateRequest order = new OrderCreateRequest(orderTable.getId(), Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성시 테이블이 공석인 경우")
    @Test
    void orderCreateWithNotEmptyTableTest() {
        OrderTable orderTable = orderTableDao.save(OrderTableHelper.createRequest(true).toEntity());
        OrderCreateRequest order = new OrderCreateRequest(orderTable.getId(), Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성시 테이블이 존재하지 않는 경우")
    @Test
    void orderCreateWithNotRegisterTable() {
        OrderLineItemCreateRequest orderLineItem = getSavedOrderLineItem();
        OrderCreateRequest order = new OrderCreateRequest(9999L, Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 상태 변경")
    @Test
    void orderStateChange() {
        Long orderTableId = getSavedOrderTable().getId();
        OrderLineItemCreateRequest savedOrderLineItem = getSavedOrderLineItem();
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTableId, Collections.singletonList(savedOrderLineItem));
        OrderDto savedOrder = orderService.create(orderCreateRequest);

        OrderDto changedOrder = orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.COMPLETION);

        assertThat(changedOrder.getId()).isEqualTo(savedOrder.getId());
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
        assertThat(changedOrder.getOrderTableId()).isEqualTo(savedOrder.getOrderTableId());
        assertThat(changedOrder.getOrderedTime()).isEqualTo(savedOrder.getOrderedTime());
        assertThat(changedOrder.getOrderLineItems().get(0).getSeq()).isEqualTo(savedOrder.getOrderLineItems().get(0).getSeq());
        assertThat(changedOrder.getOrderLineItems().get(0).getMenuId()).isEqualTo(savedOrder.getOrderLineItems().get(0).getMenuId());
        assertThat(changedOrder.getOrderLineItems().get(0).getOrderId()).isEqualTo(savedOrder.getOrderLineItems().get(0).getOrderId());
        assertThat(changedOrder.getOrderLineItems().get(0).getQuantity()).isEqualTo(savedOrder.getOrderLineItems().get(0).getQuantity());
    }


    @DisplayName("주문 상태 변경시 이미 상태가 COMPLETION 상태인 경우")
    @Test
    void orderStateChangeWithCompletionState() {
        Long orderTableId = getSavedOrderTable().getId();
        OrderLineItemCreateRequest savedOrderLineItem = getSavedOrderLineItem();
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTableId, Collections.singletonList(savedOrderLineItem));
        OrderDto savedOrder = orderService.create(orderCreateRequest);
        orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.COMPLETION);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.COMPLETION))
                .isInstanceOf(IllegalArgumentException.class);
    }


    private OrderTable getSavedOrderTable() {
        return orderTableDao.save(OrderTableHelper.createRequest(false).toEntity());
    }

    private OrderLineItemCreateRequest getSavedOrderLineItem() {
        MenuDto menu = menuService.create(getMenu());
        return OrderLineItemHelper.createRequest(menu, 1);
    }

    private MenuCreateRequest getMenu() {

        Product savedProduct01 = productDao.save(ProductHelper.createRequest("product01", 10_000).toEntity());
        Product savedProduct02 = productDao.save(ProductHelper.createRequest("product02", 20_000).toEntity());

        MenuProductRequest menuProduct01 = MenuProductHelper.createRequest(savedProduct01.getId(), 1);
        MenuProductRequest menuProduct02 = MenuProductHelper.createRequest(savedProduct02.getId(), 2);

        MenuGroup menuGroup = menuGroupDao.save(MenuGroupHelper.createRequest("메뉴 그룹").toEntity());

        List<MenuProductRequest> menuProductRequests = Arrays.asList(menuProduct01, menuProduct02);

        return MenuHelper.createRequest("메뉴", 50_000, menuGroup.getId(), menuProductRequests);
    }

}

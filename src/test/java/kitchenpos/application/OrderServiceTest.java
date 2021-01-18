package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import kitchenpos.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 서비스에 관련한 기능")
@SpringBootTest
class OrderServiceTest {
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private TableService tableService;

    private OrderLineItem orderLineItem;
    private Order order;

    @BeforeEach
    void beforeEach() {
        orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        order = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
    }

    @DisplayName("`주문`을 생성한다.")
    @Test
    void createOrder() {
        // Given
        ProductResponse 짬뽕 = productService.create(new ProductRequest("짬뽕", BigDecimal.valueOf(8_000)));
        ProductResponse 짜장면 = productService.create(new ProductRequest("짜장면", BigDecimal.valueOf(6_000)));
        MenuGroupResponse 신메뉴그룹 = menuGroupService.create(new MenuGroupRequest("신메뉴그룹"));
        Menu 추천메뉴 = menuService.create(new MenuRequest("추천메뉴", BigDecimal.valueOf(14_000), 신메뉴그룹.getId(),
                Arrays.asList(new MenuProductRequest(짬뽕.getId(), 1L), new MenuProductRequest(짜장면.getId(), 1L)))
        );
        OrderTableResponse orderTable = tableService.create(new OrderTableRequest(3, false));
        OrderLineItem menuParams = new OrderLineItem();
        menuParams.setMenuId(추천메뉴.getId());
        menuParams.setQuantity(1);
        Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(Collections.singletonList(menuParams));

        // When
        Order actual = orderService.create(order);

        // Then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderTableId()).isEqualTo(order.getOrderTableId()),
                () -> assertThat(actual.getOrderLineItems()).isNotNull(),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(actual.getOrderedTime()).isNotNull()
        );
    }

    @DisplayName("`주문`에 필요한 주문 메뉴를 담은 `주문 항목`이 없으면 `주문`을 생성할 수 없다.")
    @Test
    void exceptionToCreateOrderWithoutLineItem() {
        // Given
        order.setId(1L);
        order.setOrderTableId(1L);

        // When & then
        assertThatThrownBy(() -> orderService.create(new Order())).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`주문`할 `주문 테이블`이 없으면, `주문`을 생성할 수 없다.")
    @Test
    void exceptionToCreateOrderWithoutTable() {
        // Given
        given(menuDao.countByIdIn(Collections.singletonList(orderLineItem.getMenuId()))).willReturn(1L);
        OrderTable orderTable = new OrderTable();
        orderTable.setId(order.getOrderTableId());
        orderTable.setEmpty(true);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));

        // When & Then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 `주문` 목록을 조회한다.")
    @Test
    void findAllOrders() {
        // Given
        given(orderDao.findAll()).willReturn(Collections.singletonList(order));

        // When
        List<Order> actual = orderService.list();

        // Then
        assertAll(
                () -> assertThat(actual).extracting(Order::getId).containsExactly(order.getId()),
                () -> assertThat(actual).extracting(Order::getOrderTableId).containsExactly(order.getOrderTableId()),
                () -> assertThat(actual).extracting(Order::getOrderStatus).containsExactly(order.getOrderStatus()),
                () -> assertThat(actual).extracting(Order::getOrderedTime).containsExactly(order.getOrderedTime()),
                () -> assertThat(actual.stream().map(Order::getOrderLineItems).collect(Collectors.toList()))
                        .containsExactly(order.getOrderLineItems())
        );
    }

    @DisplayName("`주문`의 `주문 상태`를 변경한다.")
    @Test
    void changeOrderStatus() {
        // Given
        order.setOrderStatus(OrderStatus.COOKING.name());
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));
        Order updateOrder = new Order();
        updateOrder.setOrderStatus(OrderStatus.MEAL.name());

        // When
        Order actual = orderService.changeOrderStatus(order.getId(), updateOrder);

        // Then
        assertEquals(updateOrder.getOrderStatus(), actual.getOrderStatus());
    }

    @DisplayName("`주문 상태`가 'COMPLETION' 인 경우 상태를 변경할 수 없다.")
    @Test
    void exceptionToChangeOrderStatusWithCompletion() {
        // Given
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));
        Order updateOrder = new Order();
        updateOrder.setOrderStatus(OrderStatus.MEAL.name());

        // When & Then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), updateOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

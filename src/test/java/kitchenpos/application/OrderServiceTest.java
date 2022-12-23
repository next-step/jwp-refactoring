package kitchenpos.application;

import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private TableService tableService;

    private Menu 기본피자;
    private OrderLineItem 페페로니주문;

    @BeforeEach
    void setUp() {
        Menu 기본피자 = 기본피자_생성();
        페페로니주문 = new OrderLineItem(1L, 기본피자.getId(), 2);
    }

    @DisplayName("주문 생성")
    @Test
    void create() {
        OrderTable orderTable = 주문_테이블_생성(false);

        Order response = 주문_생성(orderTable, OrderStatus.COMPLETION.name()
                , LocalDateTime.now(), Collections.singletonList(페페로니주문));

        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("주문 항목이 비어있을 수 없다.")
    @Test
    void createOrderEmptyException() {
        OrderTable orderTable = 주문_테이블_생성(false);

        assertThatThrownBy(
                () -> 주문_생성(orderTable, OrderStatus.COMPLETION.name()
                , LocalDateTime.now(), Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목 수와 주문 항목 메뉴의 수는 다를 수 없다.")
    @Test
    void createOrderSizeNotEqualOrderLineSizeException() {
        OrderTable orderTable = 주문_테이블_생성(false);
        OrderLineItem 하와이안피자주문 = new OrderLineItem(1L, 기본피자.getId(), 2);

        assertThatThrownBy(
                () -> 주문_생성(orderTable, OrderStatus.COMPLETION.name()
                , LocalDateTime.now(), Arrays.asList(페페로니주문, 하와이안피자주문)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있을 수 없다.")
    @Test
    void createOrderTableEmptyException() {
        OrderTable orderTable = 주문_테이블_생성(true);

        assertThatThrownBy(
                () -> 주문_생성(orderTable, OrderStatus.COMPLETION.name()
                , LocalDateTime.now(), Collections.singletonList(페페로니주문)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 조회")
    @Test
    void list() {
        OrderTable orderTable = 주문_테이블_생성(false);
        Order order = 주문_생성(orderTable, OrderStatus.COMPLETION.name()
                , LocalDateTime.now(), Collections.singletonList(페페로니주문));

        List<Long> ids = orderService.list().stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        assertThat(ids).contains(order.getId());
    }

    @DisplayName("주문 상태 수정")
    @Test
    void changeOrderStatus() {
        OrderTable orderTable = 주문_테이블_생성(false);
        Order order = 주문_생성(orderTable, OrderStatus.MEAL.name()
                , LocalDateTime.now(), Collections.singletonList(페페로니주문));
        Order updateOrder = new Order(orderTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), null);

        Order response = orderService.changeOrderStatus(order.getId(), updateOrder);

        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("완료된 주문 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusException() {
        OrderTable orderTable = 주문_테이블_생성(false);
        Order order = 주문_생성(orderTable, OrderStatus.COMPLETION.name()
                , LocalDateTime.now(), Collections.singletonList(페페로니주문));
        Order updateOrder = new Order(orderTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), null);
        orderService.changeOrderStatus(order.getId(), updateOrder);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), updateOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Menu 기본피자_생성() {
        기본피자 = new Menu("기본피자", BigDecimal.valueOf(1000)
                , 1L, Collections.singletonList(new MenuProduct(1L, 1)));
        return menuService.create(기본피자);
    }

    private OrderTable 주문_테이블_생성(boolean empty) {
        OrderTable orderTable = new OrderTable(1L, 2, empty);
        return tableService.create(orderTable);
    }

    private Order 주문_생성(OrderTable orderTable, String orderStatus
            , LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        Order order = new Order(orderTable.getId(), orderStatus
                , orderedTime, orderLineItems);
        return orderService.create(order);
    }
}

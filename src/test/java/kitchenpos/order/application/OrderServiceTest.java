package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DisplayName("주문 관련 Service 기능 테스트")
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OrderServiceTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        //given
        orderTableDao.save(new OrderTable(1L,null,3,false));
        OrderLineItem orderLineItem1 = new OrderLineItem(null, null, 1L, 10);
        OrderLineItem orderLineItem2 = new OrderLineItem(null, null, 3L, 5);
        Order request = new Order(null, 1L, null, null, Arrays.asList(orderLineItem1, orderLineItem2));

        //when
        Order result = orderService.create(request);

        //then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(result.getOrderTableId()).isNotNull();
        assertThat(result.getOrderLineItems().get(0).getOrderId()).isNotNull();
        assertThat(result.getOrderLineItems().get(1).getOrderId()).isNotNull();
    }

    @DisplayName("주문 항목이 없거나 null이면 주문을 등록 할 수 없다.")
    @Test
    void create_empty_or_null() {
        //given
        Order emptyRequest = new Order(null, 1L, null, null, Collections.emptyList());
        Order nullRequest = new Order(null, 1L, null, null, null);

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(emptyRequest));
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(nullRequest));
    }

    @DisplayName("등록 되어있지 않은 메뉴가 있는 경우 주문을 등록 할 수 없다.")
    @Test
    void create_not_registered_menu() {
        //given
        OrderLineItem orderLineItem1 = new OrderLineItem(null, null, 1L, 10);
        OrderLineItem orderLineItem2 = new OrderLineItem(null, null, 9999999L, 5);
        Order request = new Order(null, 1L, null, null, Arrays.asList(orderLineItem1, orderLineItem2));

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("빈 테이블인 경우 주문을 등록 할 수 없다.")
    @Test
    void create_empty_table() {
        //given
        OrderLineItem orderLineItem1 = new OrderLineItem(null, null, 1L, 10);
        OrderLineItem orderLineItem2 = new OrderLineItem(null, null, 2L, 5);
        OrderTable emptyTable = orderTableDao.save(new OrderTable(3L, null, 0, true));
        Order request = new Order(null, emptyTable.getId(), null, null, Arrays.asList(orderLineItem1, orderLineItem2));

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("주문 테이블 없는 경우 주문을 등록 할 수 없다.")
    @Test
    void create_not_exist_order_table() {
        //given
        OrderLineItem orderLineItem1 = new OrderLineItem(null, null, 1L, 10);
        OrderLineItem orderLineItem2 = new OrderLineItem(null, null, 2L, 5);
        Order request = new Order(null, 99999L, null, null, Arrays.asList(orderLineItem1, orderLineItem2));

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("주문 상태를 업데이트 한다.")
    @Test
    void changeOrderStatus() {
        //given
        OrderTable orderTable = orderTableDao.save(new OrderTable(2L, null, 3, false));
        OrderLineItem orderLineItem1 = new OrderLineItem(null, null, 1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(null, null, 3L, 1);
        Order order = orderService.create(new Order(null, orderTable.getId(), null, null, Arrays.asList(orderLineItem1, orderLineItem2)));
        Order request = new Order(null, null, OrderStatus.MEAL.name(), null, null);

        //when
        Order result = orderService.changeOrderStatus(order.getId(), request);

        //then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태가 계산완료인 경우 주문 상태를 업데이트 할 수 없다.")
    @Test
    void changeOrderStatus_completion() {
        //given
        OrderTable orderTable = orderTableDao.save(new OrderTable(3L, null, 3, false));
        OrderLineItem orderLineItem1 = new OrderLineItem(null, null, 1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(null, null, 3L, 1);
        Order order = orderService.create(new Order(null, orderTable.getId(), null, null, Arrays.asList(orderLineItem1, orderLineItem2)));
        orderService.changeOrderStatus(order.getId(), new Order(null, null, OrderStatus.COMPLETION.name(), null, null));

        Order request = new Order(null, null, OrderStatus.MEAL.name(), null, null);

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(order.getId(), request));
    }

}

package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 등록한다.")
    @Test
    public void createOrder() {
        //given
        Order order = new Order(new OrderTable(1l,0,false));
        order.addOrderItems(new OrderLineItem(1l, 1l, 1));
        //when
        Order result = orderService.create(order);
        //then
        assertThat(result).isNotNull();
    }


    @DisplayName("주문 항목이 존재하지 않으면 에러")
    @Test
    public void createWithNoExistOrderLineItem() {
        //when
        //then
        assertThatThrownBy(() -> orderService.create(new Order(new OrderTable(1l,0,false)))).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 항목에 중복된 것이 존재하면 에러")
    @Test
    public void createWithDuplicateOrderLineItem() {
        //given
        Order order = new Order(new OrderTable(1l,0,false));
        order.addOrderItems(new OrderLineItem(1l, 1l, 1));
        order.addOrderItems(new OrderLineItem(1l, 1l, 1));
        //when
        //then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 존재하지 않으면 에러")
    @Test
    public void createWithNoExistOrderTable() {
        //given
        Order order = new Order(new OrderTable(9999l,0,false));
        order.addOrderItems(new OrderLineItem(1l, 1l, 1));
        //when
        //then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 빈 값이면 에러")
    @Test
    public void createWithEmptyOrderTable() {
        //given
        Order order = new Order(new OrderTable(2l,0,false));
        order.addOrderItems(new OrderLineItem(1l, 1l, 1));
        order.changeOrderTable(new OrderTable(9999l,0,false));
        //when
        //then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 전체를 조회한다.")
    @Test
    public void getOrders() {
        //given
        Order order = new Order(new OrderTable(1l,0,false));
        order.addOrderItems(new OrderLineItem(1l, 1l, 1));
        orderService.create(order);
        //when
        List<Order> result = orderService.list();
        //then
        assertThat(result).isNotNull();
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    public void updateOrderStatus() {
        //given
        Order order = new Order(new OrderTable(1l,0,false));
        order.addOrderItems(new OrderLineItem(1l, 1l, 1));
        orderService.create(order);

        Order changeStatusOrder = new Order(new OrderTable(2l,0,false));
        changeStatusOrder.addOrderItems(new OrderLineItem(1l, 1l, 1));
        changeStatusOrder.changeOrderStatus(OrderStatus.MEAL);
        orderService.create(changeStatusOrder);
        //when
        Order result = orderService.changeOrderStatus(order.getId(), changeStatusOrder);
        //then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }


    @DisplayName("주문 상태가 완료이면 변경할 수 없다.")
    @Test
    public void updateWithCompleteStatus() {
        //given
        Order order = new Order(new OrderTable(1l,0,false));
        order.addOrderItems(new OrderLineItem(1l, 1l, 1));
        order.changeOrderStatus(OrderStatus.COMPLETION);
        orderService.create(order);

        Order changeStatusOrder = new Order(new OrderTable(2l,0,false));
        changeStatusOrder.addOrderItems(new OrderLineItem(1l, 1l, 1));
        orderService.create(changeStatusOrder);
        //when
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeStatusOrder)).isInstanceOf(IllegalArgumentException.class);
    }



}

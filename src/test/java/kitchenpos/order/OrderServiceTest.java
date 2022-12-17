package kitchenpos.order;

import static kitchenpos.order.OrderFixture.주문;
import static kitchenpos.order.OrderFixture.주문항목;
import static kitchenpos.table.TableFixture.일번테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문 생성")
    void createOrder() {
        //given
        when(menuDao.countByIdIn(any()))
            .thenReturn(1L);
        when(orderTableDao.findById(any()))
            .thenReturn(Optional.of(일번테이블));
        when(orderDao.save(any()))
            .thenReturn(주문);
        when(orderLineItemDao.save(any()))
            .thenReturn(주문항목);

        //when
        OrderResponse orderResponse = orderService.create(주문);

        //then
        assertThat(orderResponse).isEqualTo(OrderResponse.from(주문));
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(orderResponse.getOrderedTime()).isNotNull();
    }

    @Test
    void 주문_항목이_없으면_주문중_에러_발생() {
        //when & then
        Assertions.assertThatThrownBy(() -> orderService.create(new Order()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목중_메뉴에_없는항목이_있으면_에러_발생() {
        //given
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(new OrderLineItem()));
        when(menuDao.countByIdIn(any())).thenReturn(0L);

        //when & then
        Assertions.assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_없으면_에러_발생() {
        //given
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(new OrderLineItem()));
        when(menuDao.countByIdIn(any())).thenReturn(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        //when & then
        Assertions.assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_빈_테이블이면_에러_발생() {
        //given
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(new OrderLineItem()));
        when(menuDao.countByIdIn(any())).thenReturn(1L);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));

        //when & then
        Assertions.assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
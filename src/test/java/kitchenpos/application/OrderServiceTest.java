package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.Assertions;
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
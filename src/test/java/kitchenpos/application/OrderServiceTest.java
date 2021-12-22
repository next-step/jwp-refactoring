package kitchenpos.application;

import static common.OrderFixture.주문_첫번째;
import static common.OrderFixture.주문_첫번째_완료;
import static common.OrderTableFixture.첫번째_주문테이블;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.menu.domain.MenuDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

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
    void 주문_생성() {
        // given
        OrderTable 첫번째_주문테이블 = 첫번째_주문테이블();
        Order 주문_첫번째 = 주문_첫번째();

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);

        // mocking
        when(menuDao.countByIdIn(any(List.class))).thenReturn(1L);

        when(orderTableDao.findById(any(Long.class))).thenReturn(Optional.of(첫번째_주문테이블));
        when(orderDao.save(주문_첫번째)).thenReturn(주문_첫번째);
        when(orderLineItemDao.save(any(OrderLineItem.class))).thenReturn(
            orderLineItem);

        // when
        Order 저장된주문 = orderService.create(주문_첫번째);

        // then
        assertThat(저장된주문).isEqualTo(주문_첫번째);
    }


    @Test
    void 주문상태변경시_이미완료상태라면_예외() {
        // given
        Order 주문_첫번째_완료 = 주문_첫번째_완료();
        // mocking
        when(orderDao.findById(anyLong())).thenReturn(Optional.of(주문_첫번째_완료));

        // then
        Assertions.assertThatThrownBy(() -> {
            orderService.changeOrderStatus(1L, 주문_첫번째_완료);
        }).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void 주문상태변경() {
        // given
        Order 주문_첫번째 = 주문_첫번째();
        Order 주문_첫번째_완료 = 주문_첫번째_완료();

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);

        // mocking
        when(orderDao.findById(anyLong())).thenReturn(Optional.of(주문_첫번째));
        when(orderDao.save(주문_첫번째)).thenReturn(주문_첫번째);
        when(orderLineItemDao.findAllByOrderId(anyLong())).thenReturn(asList(
            orderLineItem));
        // when
        Order order = orderService.changeOrderStatus(1L, 주문_첫번째_완료);

        // then
        assertThat(order.getId()).isEqualTo(주문_첫번째_완료.getId());
        assertThat(order.getOrderStatus()).isEqualTo(주문_첫번째_완료.getOrderStatus());
        assertThat(order.getOrderTableId()).isEqualTo(주문_첫번째_완료.getOrderTableId());
    }

}

package kitchenpos.order.application;

import static common.OrderFixture.주문_첫번째;
import static common.OrderFixture.주문_첫번째_완료;
import static common.OrderTableFixture.첫번째_주문테이블;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import common.MenuFixture;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemDao;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderLineRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
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
        when(orderDao.save(any(Order.class))).thenReturn(주문_첫번째);
        lenient().when(orderLineItemDao.save(any(OrderLineItem.class))).thenReturn(orderLineItem);

        // when
        OrderResponse 저장된주문 = orderService.create(
            new OrderRequest(첫번째_주문테이블.getId(), asList(new OrderLineRequest(MenuFixture.메뉴_양념치킨()
                .getId(), 1L))));

        // then
        verify(orderDao, atMostOnce()).save(any());
        verify(orderLineItemDao, atMostOnce()).save(any());
    }


    @Test
    void 주문상태변경시_이미완료상태라면_예외() {
        // given
        Order 주문_첫번째_완료 = 주문_첫번째_완료();
        // mocking
        when(orderDao.findById(anyLong())).thenReturn(Optional.of(주문_첫번째_완료));

        // then
        Assertions.assertThatThrownBy(() -> {
            orderService.changeOrderStatus(1L, new ChangeOrderStatusRequest("COMPLETION"));
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
        OrderResponse orderResponse = orderService.changeOrderStatus(1L, new ChangeOrderStatusRequest("COMPLETION"));

        // then
        verify(orderDao, atMostOnce()).save(any());
    }

}

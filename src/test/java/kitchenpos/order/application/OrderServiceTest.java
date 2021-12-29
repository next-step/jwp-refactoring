package kitchenpos.order.application;

import static common.MenuFixture.메뉴_양념치킨;
import static common.OrderFixture.계산_완료;
import static common.OrderFixture.주문;
import static common.OrderTableFixture.첫번째_주문테이블;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kitchenpos.common.exception.Message;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidation;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderLineRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    @Mock
    OrderValidation orderValidation;

    @Mock
    OrderDao orderDao;

    @Test
    void 주문_생성() {
        // given
        OrderTable 첫번째_주문테이블 = 첫번째_주문테이블();
        Menu 메뉴_양념치킨 = 메뉴_양념치킨();
        Order 주문 = 주문();

        // mocking
        when(orderDao.save(any())).thenReturn(주문);

        // when
        orderService.create(
            new OrderRequest(첫번째_주문테이블.getId(),
                asList(new OrderLineRequest(메뉴_양념치킨.getId(), 1L))));

        // then
        verify(orderDao, atMostOnce()).save(any());
    }


    @Test
    void 주문상태변경시_이미계산완료_상태라면_예외() {
        // then
        assertThatThrownBy(() -> {
            Order 계산_완료 = 계산_완료();
            when(orderDao.findById(anyLong())).thenReturn(Optional.of(계산_완료));
            orderService.changeOrderStatus(1L,
                new ChangeOrderStatusRequest(OrderStatus.COMPLETION.name()));
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.ORDER_STATUS_IS_NOT_COMPLETION.getMessage());
    }


    @Test
    void 주문상태변경() {
        // given
        Order 주문 = 주문();

        // mocking
        when(orderDao.findById(anyLong())).thenReturn(Optional.of(주문));
        when(orderDao.save(주문)).thenReturn(주문);

        // when
        orderService.changeOrderStatus(1L,
            new ChangeOrderStatusRequest(OrderStatus.COMPLETION.name()));

        // then
        verify(orderDao, atMostOnce()).save(any());
    }

}

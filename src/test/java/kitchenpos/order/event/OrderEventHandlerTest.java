package kitchenpos.order.event;

import kitchenpos.exception.OrderException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.table.application.OrderTableService;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("주문 이벤트 핸들러 테스트")
@ExtendWith(MockitoExtension.class)
class OrderEventHandlerTest {

    @Mock
    private MenuService menuService;

    @Mock
    private OrderTableService orderTableService;

    @InjectMocks
    private OrderEventHandler orderEventHandler;

    @Test
    void 주문한_아이템의_갯수와_실제_조회한_아이템의_갯수가_일치하지않을때_에러발생() {
        List<Long> menuIds = Arrays.asList(1L);
        when(menuService.countByMenuId(menuIds)).thenReturn(0L);
        OrderValidEvent orderValidEvent = new OrderValidEvent(menuIds, 1L);
        assertThatThrownBy(() -> orderEventHandler.createOrderValidEvent(orderValidEvent)).isInstanceOf(OrderException.class);
    }

    @Test
    void 주문_테이블이_비어있는_상태이면_에러발생() {
        OrderTable 주문_테이블 = new OrderTable(1L, 4, true);
        List<Long> menuIds = Arrays.asList(1L);
        when(menuService.countByMenuId(menuIds)).thenReturn(1L);
        when(orderTableService.findOrderTable(1L)).thenReturn(주문_테이블);
        OrderValidEvent orderValidEvent = new OrderValidEvent(menuIds, 1L);
        assertThatThrownBy(() -> orderEventHandler.createOrderValidEvent(orderValidEvent)).isInstanceOf(OrderException.class);
    }
}

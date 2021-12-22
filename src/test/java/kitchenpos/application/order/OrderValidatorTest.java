package kitchenpos.application.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import kitchenpos.application.menu.MenuService;
import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.event.orders.ValidateEmptyTableEvent;
import kitchenpos.exception.order.EmptyOrderLineItemOrderException;
import kitchenpos.exception.order.NotRegistedMenuOrderException;
import kitchenpos.vo.MenuId;
import kitchenpos.vo.OrderTableId;

@ExtendWith(MockitoExtension.class)
public class OrderValidatorTest {
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private MenuService menuService;

    @InjectMocks
    private OrdersValidator ordersValidator;
    
    @Captor
    private ArgumentCaptor<ValidateEmptyTableEvent> captor;
    
    @DisplayName("주문에속하는 수량있는 메뉴가 없는 주문은 예외가 발생된다.")
    @Test
    void exception_createOrder_emptyOrderedMenu() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(10, false);
        Orders 치킨주문 = Orders.of(OrderTableId.of(치킨_주문_단체테이블), OrderStatus.COOKING);

        // when
        // then
        Assertions.assertThatExceptionOfType(EmptyOrderLineItemOrderException.class)
                    .isThrownBy(() -> ordersValidator.validateForCreate(치킨주문));
    }

    @DisplayName("미등록된 메뉴에대한 오더시 예외가 발생된다.")
    @Test
    void exception_createOrder_notExistedOrderTable() {
        // given
        Menu 뿌링클콤보 = Menu.of(1L, "뿌링클콤보", Price.of(18_000));
        OrderLineItem 치킨_주문항목 = OrderLineItem.of(MenuId.of(뿌링클콤보), 1L);

        OrderTable 치킨_주문_단체테이블 = OrderTable.of(10, false);
        Orders 치킨주문 = Orders.of(OrderTableId.of(치킨_주문_단체테이블), OrderStatus.COOKING);
        치킨_주문항목.acceptOrder(치킨주문);

        when(menuService.findAllByIdIn(anyList())).thenReturn(List.of());

        // when
        // then
        Assertions.assertThatExceptionOfType(NotRegistedMenuOrderException.class)
                    .isThrownBy(() -> ordersValidator.validateForCreate(치킨주문));
    }

    @DisplayName("주문테이블이 빈테이블인지 확인하는 이벤트가 발생된다.")
    @Test
    void exception_createOrder_emptyOrderTable() {
        // given
        Menu 뿌링클콤보 = Menu.of(1L, "뿌링클콤보", Price.of(18_000));
        OrderLineItem 치킨_주문항목 = OrderLineItem.of(MenuId.of(뿌링클콤보), 1L);

        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        Orders 치킨주문 = Orders.of(OrderTableId.of(치킨_주문_단체테이블), OrderStatus.COOKING);
        치킨_주문항목.acceptOrder(치킨주문);

        when(menuService.findAllByIdIn(anyList())).thenReturn(List.of(뿌링클콤보));

        ordersValidator.validateForCreate(치킨주문);

        // when
        // then
        verify(eventPublisher).publishEvent(any(ValidateEmptyTableEvent.class));
    }
}

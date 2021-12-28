package kitchenpos.order.domain;

import static org.mockito.ArgumentMatchers.anyList;
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

import kitchenpos.common.domain.Price;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.exception.NotRegistedMenuOrderException;
import kitchenpos.order.exception.EmptyOrderLineItemOrderException;
import kitchenpos.order.exception.NotChangableOrderStatusException;
import kitchenpos.order.event.ValidateEmptyTableEvent;

@ExtendWith(MockitoExtension.class)
public class OrderValidatorTest {

    @Mock
    private MenuService menuService;
    
    @Mock
    private OrdersRepository ordersRepository;

    @InjectMocks
    private OrdersValidator ordersValidator;

    @Captor
    private ArgumentCaptor<ValidateEmptyTableEvent> captor;

    @DisplayName("주문에속하는 수량있는 메뉴가 없는 주문은 예외가 발생된다.")
    @Test
    void exception_createOrder_emptyOrderedMenu() {
        // when
        // then
        Assertions.assertThatExceptionOfType(EmptyOrderLineItemOrderException.class)
                    .isThrownBy(() -> ordersValidator.checkEmptyOfOrderLineItems(OrderLineItems.of()));
    }

    @DisplayName("미등록된 메뉴에대한 오더시 예외가 발생된다.")
    @Test
    void exception_createOrder_notExistedOrderTable() {
        // given
        Menu 뿌링클콤보 = Menu.of(1L, "뿌링클콤보", Price.of(18_000));
        OrderLineItem 치킨_주문항목 = OrderLineItem.of(MenuId.of(뿌링클콤보.getId()), 1L);

        Orders 치킨주문 = Orders.of(OrderTableId.of(1L), OrderStatus.COOKING);
        치킨_주문항목.acceptOrder(치킨주문);

        when(menuService.findAllByIdIn(anyList())).thenReturn(List.of());

        // when
        // then
        Assertions.assertThatExceptionOfType(NotRegistedMenuOrderException.class)
                    .isThrownBy(() -> ordersValidator.checkExistOfMenu(OrderLineItems.of(List.of(치킨_주문항목))));
    }

    @DisplayName("계산완료된 주문의 상태를 변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderStatus_() {
        // given
        Menu 뿌링클콤보 = Menu.of(1L, "뿌링클콤보", Price.of(18_000));
        OrderLineItem 치킨_주문항목 = OrderLineItem.of(MenuId.of(뿌링클콤보.getId()), 1L);

        Orders 치킨주문 = Orders.of(OrderTableId.of(1L), OrderStatus.COMPLETION);
        치킨_주문항목.acceptOrder(치킨주문);

        // when
        // then
        Assertions.assertThatExceptionOfType(NotChangableOrderStatusException.class)
                    .isThrownBy(() -> ordersValidator.validateionOfChageOrderStatus(치킨주문));
    }
}

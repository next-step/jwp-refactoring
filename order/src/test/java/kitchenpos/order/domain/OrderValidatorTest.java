package kitchenpos.order.domain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

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

import kitchenpos.menu.application.MenuService;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.dto.OrderDto;
import kitchenpos.order.event.ValidateEmptyTableEvent;
import kitchenpos.order.exception.EmptyOrderLineItemOrderException;
import kitchenpos.order.exception.NotChangableOrderStatusException;
import kitchenpos.menu.vo.MenuId;
import kitchenpos.menugroup.exception.NotRegistedMenuOrderException;
import kitchenpos.table.vo.OrderTableId;
import kitchenpos.validation.OrdersValidator;

@ExtendWith(MockitoExtension.class)
public class OrderValidatorTest {
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private MenuService menuService;
    
    @Mock
    private OrdersRepository ordersRepository;

    @InjectMocks
    private OrdersValidator ordersValidator;

    @Captor
    private ArgumentCaptor<ValidateEmptyTableEvent> captor;
    
    @DisplayName("주문 유효성검사자는 주문생성시 유효성여부를 확인 후 정합시 주문이 생성된다.")
    @Test
    void generate_order_forCreate() {
        // given
        Menu 뿌링클콤보 = Menu.of(1L, "뿌링클콤보", Price.of(18_000));

        OrderTable 치킨_주문_단체테이블 = OrderTable.of(10, false);
        OrderLineItems 주문항목들 = OrderLineItems.of(OrderLineItem.of(MenuId.of(뿌링클콤보.getId()), 1L));
        Orders 치킨주문 = Orders.of(OrderTableId.of(치킨_주문_단체테이블), OrderStatus.COOKING, 주문항목들);

        when(menuService.findAllByIdIn(anyList())).thenReturn(List.of(뿌링클콤보));

        // when
        Orders order = ordersValidator.getValidatedOrdersForCreate(OrderDto.of(치킨주문));

        // then
        Assertions.assertThat(order).isEqualTo(치킨주문);
    }

    @DisplayName("주문에속하는 수량있는 메뉴가 없는 주문은 예외가 발생된다.")
    @Test
    void exception_createOrder_emptyOrderedMenu() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(10, false);
        Orders 치킨주문 = Orders.of(OrderTableId.of(치킨_주문_단체테이블), OrderStatus.COOKING);

        // when
        // then
        Assertions.assertThatExceptionOfType(EmptyOrderLineItemOrderException.class)
                    .isThrownBy(() -> ordersValidator.getValidatedOrdersForCreate(OrderDto.of(치킨주문)));
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
                    .isThrownBy(() -> ordersValidator.getValidatedOrdersForCreate(OrderDto.of(치킨주문)));
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

        ordersValidator.getValidatedOrdersForCreate(OrderDto.of(치킨주문));

        // when
        // then
        verify(eventPublisher).publishEvent(any(ValidateEmptyTableEvent.class));
    }

    @DisplayName("주문 유효성검사자는 주문 상태변경시 유효성여부를 확인 후 정합시 주문이 생성된다.")
    @Test
    void generate_order_forChangeOrderStatus() {
        // given
        Menu 뿌링클콤보 = Menu.of(1L, "뿌링클콤보", Price.of(18_000));
        OrderLineItem 치킨_주문항목 = OrderLineItem.of(MenuId.of(뿌링클콤보), 1L);

        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        Orders 치킨주문 = Orders.of(OrderTableId.of(치킨_주문_단체테이블), OrderStatus.COOKING);
        치킨_주문항목.acceptOrder(치킨주문);

        when(ordersRepository.findById(nullable(Long.class))).thenReturn(Optional.of(치킨주문));
        
        // when
        Orders validatedOrder = ordersValidator.getValidatedOrdersForChangeOrderStatus(치킨주문.getId());

        // then
        Assertions.assertThat(validatedOrder).isEqualTo(치킨주문);
    }

    @DisplayName("계산완료된 주문의 상태를 변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderStatus_() {
        // given
        Menu 뿌링클콤보 = Menu.of(1L, "뿌링클콤보", Price.of(18_000));
        OrderLineItem 치킨_주문항목 = OrderLineItem.of(MenuId.of(뿌링클콤보), 1L);

        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        Orders 치킨주문 = Orders.of(OrderTableId.of(치킨_주문_단체테이블), OrderStatus.COMPLETION);
        치킨_주문항목.acceptOrder(치킨주문);

        when(ordersRepository.findById(nullable(Long.class))).thenReturn(Optional.of(치킨주문));

        // when
        // then
        Assertions.assertThatExceptionOfType(NotChangableOrderStatusException.class)
                    .isThrownBy(() -> ordersValidator.getValidatedOrdersForChangeOrderStatus(치킨주문.getId()));
    }
}

package kitchenpos.application.order;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.menu.MenuService;
import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.order.OrderDto;
import kitchenpos.dto.order.OrderLineItemDto;
import kitchenpos.exception.order.NotChangableOrderStatusException;
import kitchenpos.vo.MenuId;
import kitchenpos.vo.OrderTableId;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrdersRepository;
import kitchenpos.domain.order.OrdersValidator;
import kitchenpos.domain.order.OrderStatus;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private MenuService menuService;

    @Mock
    private OrdersRepository orderRepository;

    @Mock
    private OrdersValidator ordersValidator;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문이 저장된다.")
    @Test
    void create_order() {
        // given
        Product 뿌링클치킨 = Product.of("뿌링클치킨", Price.of(15_000));
        Product 치킨무 = Product.of("치킨무", Price.of(1_000));
        Product 코카콜라 = Product.of("코카콜라", Price.of(3_000));

        MenuProduct 뿌링클콤보_뿌링클치킨 = MenuProduct.of(뿌링클치킨, 1L);
        MenuProduct 뿌링클콤보_치킨무 = MenuProduct.of(치킨무, 1L);
        MenuProduct 뿌링클콤보_코카콜라 = MenuProduct.of(코카콜라, 1L);

        Menu 뿌링클콤보 = Menu.of(1L, "뿌링클콤보", Price.of(18_000));

        뿌링클콤보_뿌링클치킨.acceptMenu(뿌링클콤보);
        뿌링클콤보_치킨무.acceptMenu(뿌링클콤보);
        뿌링클콤보_코카콜라.acceptMenu(뿌링클콤보);

        OrderTable 치킨_주문_단체테이블 = OrderTable.of(10, false);

        OrderLineItem 치킨_주문항목 = OrderLineItem.of(MenuId.of(뿌링클콤보), 1L);
        Orders 치킨주문 = Orders.of(OrderTableId.of(치킨_주문_단체테이블), OrderStatus.COOKING);
        치킨_주문항목.acceptOrder(치킨주문);

        when(orderRepository.save(any(Orders.class))).thenReturn(치킨주문);

        OrderDto 주문_요청전문 = OrderDto.of(1L, List.of(OrderLineItemDto.of(1L, 1L)));

        // when
        OrderDto savedOrder = orderService.create(주문_요청전문);

        // then
        assertAll(
            () -> Assertions.assertThat(savedOrder.getOrderLineItems().size()).isEqualTo(1),
            () -> Assertions.assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @DisplayName("주문이 조회된다.")
    @Test
    void search_order() {
        // given
        Menu 뿌링클콤보 = Menu.of("뿌링클콤보", Price.of(18_000));

        OrderTable 치킨_주문_단체테이블 = OrderTable.of(10, false);

        OrderLineItem 치킨_주문항목 = OrderLineItem.of(MenuId.of(뿌링클콤보), 1L);
        Orders 치킨주문 = Orders.of(OrderTableId.of(치킨_주문_단체테이블), OrderStatus.MEAL);
        치킨_주문항목.acceptOrder(치킨주문);

        when(orderRepository.findAll()).thenReturn(List.of(치킨주문));

        // when
        List<OrderDto> orders = orderService.list();

        // then
        Assertions.assertThat(orders).isEqualTo(List.of(OrderDto.of(치킨주문)));
    }

    @DisplayName("주문의 상태가 변경된다.")
    @Test
    void update_orderStatus() {
        // given
        Menu 뿌링클콤보 = Menu.of("뿌링클콤보", Price.of(18_000));

        OrderTable 치킨_주문_단체테이블 = OrderTable.of(10, false);

        OrderLineItem 치킨_주문항목 = OrderLineItem.of(MenuId.of(뿌링클콤보), 1L);
        Orders 치킨주문 = Orders.of(OrderTableId.of(치킨_주문_단체테이블), OrderStatus.COOKING);
        치킨_주문항목.acceptOrder(치킨주문);

        when(orderRepository.findById(치킨주문.getId())).thenReturn(Optional.of(치킨주문));

        OrderDto 상태변경_요청전문 = OrderDto.of(OrderStatus.MEAL.name());

        // when
        Orders chagedOrder = orderService.changeOrderStatus(치킨주문.getId(), 상태변경_요청전문);

        // then
        assertAll(
            () -> Assertions.assertThat(chagedOrder).isEqualTo(치킨주문),
            () -> Assertions.assertThat(chagedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL)
        );
    }

    @DisplayName("계산이 완료된 주문의 상태를 변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderStatus_afterOrderStatusCompletion() {
        // given
        Menu 뿌링클콤보 = Menu.of("뿌링클콤보", Price.of(18_000));

        OrderTable 치킨_주문_단체테이블 = OrderTable.of(10, false);

        OrderLineItem 치킨_주문항목 = OrderLineItem.of(MenuId.of(뿌링클콤보), 1L);
        Orders 치킨주문 = Orders.of(OrderTableId.of(치킨_주문_단체테이블), OrderStatus.COMPLETION);
        치킨_주문항목.acceptOrder(치킨주문);

        when(orderRepository.findById(치킨주문.getId())).thenReturn(Optional.of(치킨주문));

        // when
        // then
        Assertions.assertThatExceptionOfType(NotChangableOrderStatusException.class)
                    .isThrownBy(() -> orderService.changeOrderStatus(치킨주문.getId(), OrderDto.of(치킨주문)));
    }
}

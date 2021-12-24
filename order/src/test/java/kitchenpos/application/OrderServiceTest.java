package kitchenpos.order.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.dto.OrderDto;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.menu.vo.MenuId;
import kitchenpos.table.vo.OrderTableId;
import kitchenpos.validation.OrdersValidator;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.order.domain.OrderStatus;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
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

        when(ordersValidator.getValidatedOrdersForCreate(any(OrderDto.class))).thenReturn(치킨주문);
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

        when(ordersValidator.getValidatedOrdersForChangeOrderStatus(치킨주문.getId())).thenReturn(치킨주문);
        
        OrderDto 상태변경_요청전문 = OrderDto.of(OrderStatus.MEAL.name());

        // when
        OrderDto chagedOrder = orderService.changeOrderStatus(치킨주문.getId(), 상태변경_요청전문);

        // then
        assertAll(
            () -> Assertions.assertThat(chagedOrder).isEqualTo(OrderDto.of(치킨주문)),
            () -> Assertions.assertThat(chagedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name())
        );
    }
}

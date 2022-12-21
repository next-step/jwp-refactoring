package kitchenpos.order.application;

import static kitchenpos.exception.ErrorCode.ALREADY_COMPLETION_STATUS;
import static kitchenpos.exception.ErrorCode.CAN_NOT_ORDER;
import static kitchenpos.exception.ErrorCode.NOT_EXISTS_ORDER_LINE_ITEMS;
import static kitchenpos.exception.ErrorCode.NOT_SAME_BETWEEN_ORDER_LINE_ITEMS_AND_MENU_COUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.dto.request.OrderRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.dto.request.OrderStatusRequest;
import kitchenpos.exception.ErrorCode;
import kitchenpos.exception.KitchenposException;
import kitchenpos.order.domain.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderValidator orderValidator;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderService orderService;
    private MenuProduct 메뉴_항목;
    private Menu 메뉴;
    private OrderRequest 주문;
    private Order 주문_완료_상태;
    private OrderStatusRequest 주문_상태_변경;
    private OrderLineItem 주문_항목;
    private OrderTable 좌석;

    @BeforeEach
    void setUp() {
        메뉴_항목 = new MenuProduct(1L, 1);
        메뉴 = new Menu("후라이드치킨", BigDecimal.valueOf(16000), null, Arrays.asList(메뉴_항목));
        주문_항목 = new OrderLineItem(1L, null, 메뉴.getId(), 1L);
        좌석 = new OrderTable(1L, null, 1, false);
        주문 = new OrderRequest(좌석.getId(), Arrays.asList(주문_항목));
        주문_완료_상태 = Order.of(좌석.getId(), OrderStatus.COMPLETION, Arrays.asList(주문_항목));
        주문_상태_변경 = new OrderStatusRequest(OrderStatus.COMPLETION);
    }

    @Test
    void 생성() {
        doNothing().when(orderValidator).validateCreate(any());
        given(orderRepository.save(any())).willReturn(주문.toEntity());

        OrderResponse response = orderService.create(주문);

        assertThat(response.getOrderTableId()).isEqualTo(좌석.getId());
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(response.getOrderLineItems()).containsExactly(주문_항목);
    }

    @Test
    void 주문_항목이_empty인_경우() {
        주문 = new OrderRequest(좌석.getId(), Collections.emptyList());
        doThrow(new KitchenposException(NOT_EXISTS_ORDER_LINE_ITEMS)).when(orderValidator).validateCreate(any());

        assertThatThrownBy(
                () -> orderService.create(주문)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(NOT_EXISTS_ORDER_LINE_ITEMS.getDetail());
    }

    @Test
    void 주문_항목의_수와_등록된_메뉴의_수가_같지_않은_경우() {
        doThrow(new KitchenposException(NOT_SAME_BETWEEN_ORDER_LINE_ITEMS_AND_MENU_COUNT))
                .when(orderValidator).validateCreate(any());

        assertThatThrownBy(
                () -> orderService.create(주문)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(NOT_SAME_BETWEEN_ORDER_LINE_ITEMS_AND_MENU_COUNT.getDetail());
    }

    @Test
    void 좌석이_공석인_경우() {
        좌석 = new OrderTable(1L, null, 1, true);
        doThrow(new KitchenposException(CAN_NOT_ORDER)).when(orderValidator).validateCreate(any());

        assertThatThrownBy(
                () -> orderService.create(주문)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(CAN_NOT_ORDER.getDetail());
    }

    @Test
    void 조회() {
        given(orderRepository.findAll()).willReturn(Arrays.asList(주문.toEntity()));

        List<OrderResponse> orders = orderService.list();

        assertAll(
                () -> assertThat(orders.size()).isEqualTo(1),
                () -> assertThat(orders.get(0).getOrderLineItems()).containsExactly(주문_항목)
        );
    }

    @Test
    void 주문_상태_변경() {
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(주문.toEntity()));

        OrderResponse response = orderService.changeOrderStatus(anyLong(), 주문_상태_변경.getOrderStatus());

        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
        assertThat(response.getOrderLineItems()).containsExactly(주문_항목);
    }

    @Test
    void 주문이_등록되어_있지_않은_경우() {
        given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(anyLong(), 주문_상태_변경.getOrderStatus())
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(ErrorCode.NOT_FOUND_ORDER.getDetail());
    }

    @Test
    void 주문_상태가_완료인_경우() {
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(주문_완료_상태));

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(anyLong(), 주문_상태_변경.getOrderStatus())
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(ALREADY_COMPLETION_STATUS.getDetail());
    }
}

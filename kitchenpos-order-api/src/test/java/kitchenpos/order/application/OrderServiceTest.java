package kitchenpos.order.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.EntityNotFoundExceptionCode;
/*import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;*/
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.OrderExceptionCode;
import kitchenpos.tablegroup.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@Disabled
@DisplayName("OrderService 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    /*@Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    private OrderService orderService;

    private OrderTable 주문테이블;
    private Order 주문;
    private MenuGroup 양식;
    private Menu 양식_세트1;
    private Menu 양식_세트2;
    private OrderLineItem 주문_메뉴1;
    private OrderLineItem 주문_메뉴2;
    private List<OrderLineItem> 주문_메뉴_목록;

    @BeforeEach
    void setUp() {
        주문테이블 = new OrderTable(2, false);

        ReflectionTestUtils.setField(주문테이블, "id", 1L);

        주문 = new Order(주문테이블.getId(), OrderStatus.COOKING);

        양식 = new MenuGroup("양식");
        양식_세트1 = new Menu("양식 세트1", new BigDecimal(43000), 양식);
        양식_세트2 = new Menu("양식 세트2", new BigDecimal(50000), 양식);

        ReflectionTestUtils.setField(주문, "id", 1L);
        ReflectionTestUtils.setField(양식, "id", 1L);
        ReflectionTestUtils.setField(양식_세트1, "id", 1L);
        ReflectionTestUtils.setField(양식_세트2, "id", 2L);

        주문_메뉴1 = new OrderLineItem(주문, 양식_세트1.getId(), 1L);
        주문_메뉴2 = new OrderLineItem(주문, 양식_세트2.getId(), 1L);

        주문_메뉴_목록 = Arrays.asList(주문_메뉴1, 주문_메뉴2);
        주문.order(주문_메뉴_목록);
    }

    @Test
    void 주문_생성() {
        OrderRequest request = new OrderRequest(주문테이블.getId(), OrderStatus.COOKING,
                OrderLineItemRequest.list(Arrays.asList(주문_메뉴1, 주문_메뉴2)));

        willDoNothing().given(orderValidator).validateToCreateOrder(anyLong(), anyList());
        given(orderRepository.save(any(Order.class))).willReturn(주문);

        OrderResponse orderResponse = orderService.create(request);

        assertThat(orderResponse).satisfies(res -> {
            assertEquals(주문.getId(), res.getId());
            assertEquals(주문테이블.getId(), res.getOrderTableId());
            assertEquals(OrderStatus.COOKING, res.getOrderStatus());
            assertEquals(request.getOrderLineItems().size(), res.getOrderLineItems().size());
        });
    }

    @Test
    void 주문_목록_조회() {
        given(orderRepository.findAll()).willReturn(Arrays.asList(주문));

        List<OrderResponse> orders = orderService.list();

        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.stream().map(OrderResponse::getId).collect(toList()))
                        .containsExactly(주문.getId())
        );
    }

    @Test
    void 등록된_주문이_아니면_주문_상태를_변경할_수_없음() {
        given(orderRepository.findById(주문.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(주문.getId(), OrderStatus.COOKING);
        }).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(EntityNotFoundExceptionCode.NOT_FOUND_BY_ID.getMessage());
    }

    @Test
    void 이미_완료된_주문이면_주문_상태를_변경할_수_없음() {
        주문.changeOrderStatus(OrderStatus.COMPLETION);
        given(orderRepository.findById(주문.getId())).willReturn(Optional.of(주문));

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(주문.getId(), OrderStatus.COMPLETION);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderExceptionCode.CANNOT_CHANGE_COMPLETION_ORDER.getMessage());
    }

    @Test
    void 주문_상태_변경() {
        given(orderRepository.findById(주문.getId())).willReturn(Optional.of(주문));
        given(orderRepository.save(주문)).willReturn(주문);

        orderService.changeOrderStatus(주문.getId(), OrderStatus.COMPLETION);

        assertEquals(OrderStatus.COMPLETION, 주문.getOrderStatus());
    }*/
}
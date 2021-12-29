package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.KitchenposErrorCode;
import kitchenpos.common.exception.KitchenposException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    private OrderService orderService;

    private OrderTable orderTable;
    private OrderLineItem orderLineItem;
    private OrderLineItemRequest orderLineItemRequest;
    private Order order;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(1L, 1L, 4, false);
        orderLineItemRequest = new OrderLineItemRequest(1L, 1);
        orderLineItem = new OrderLineItem(1L, 1);
        order = new Order(1L, orderTable, OrderStatus.COOKING, LocalDateTime.now(),
            new OrderLineItems(Collections.singletonList(orderLineItem)));
    }

    @DisplayName("주문 생성")
    @Test
    void create() {
        // given
        조회한_주문_테이블_반환(orderTable);
        유효성_확인시_에러_던지지_않음();
        주문_저장(order);

        OrderRequest request = new OrderRequest(1L, Collections.singletonList(orderLineItemRequest));

        // when
        OrderResponse actual = orderService.create(request);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getOrderStatus()).isEqualTo("COOKING"),
            () -> assertThat(actual.getOrderTableId()).isEqualTo(1L),
            () -> assertThat(actual.getOrderLineItems()).isEqualTo(Collections.singletonList(
                OrderLineItemResponse.from(orderLineItem)))
        );
    }

    @DisplayName("주문 항목 없을 시 주문 불가능")
    @Test
    void createOrderFailWhenNoItem() {
        // given
        OrderRequest request = new OrderRequest(1L, Collections.emptyList());

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> orderService.create(request))
            .withMessage("주문 항목이 비어있습니다.");
    }

    @DisplayName("주문 항목과 메뉴 숫자가 다른 경우(메뉴에 없는 주문 항목) 주문 불가능")
    @Test
    void createOrderFailWhenItemNotExists() {
        // given
        유효성_주문_항목_개수_다름_에러();

        OrderRequest request = new OrderRequest(1L, Collections.singletonList(orderLineItemRequest));

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> orderService.create(request))
            .withMessage("주문 항목의 개수가 다릅니다.");
    }

    @DisplayName("주문 테이블이 빈 테이블인 경우 주문 불가능")
    @Test
    void createOrderFailWhenTableNotExists() {
        // given

        OrderTable orderTable = new OrderTable(1L, 1L, 4, true);
        조회한_주문_테이블_반환(orderTable);

        OrderRequest request = new OrderRequest(1L, Collections.singletonList(orderLineItemRequest));

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> orderService.create(request))
            .withMessage("주문 테이블이 비어있습니다.");
    }

    @DisplayName("주문 조회")
    @Test
    void list() {
        // given
        주문_전쳬_조회();

        // when
        List<OrderResponse> actual = orderService.list().getOrderResponses();

        // then
        assertAll(
            () -> assertThat(actual).hasSize(1),
            () -> assertThat(actual.get(0).getId()).isEqualTo(1L),
            () -> assertThat(actual.get(0).getOrderTableId()).isEqualTo(1L),
            () -> assertThat(actual.get(0).getOrderStatus()).isEqualTo("COOKING"),
            () -> assertThat(actual.get(0).getOrderLineItems()).isEqualTo(Collections.singletonList(
                OrderLineItemResponse.from(orderLineItem)))
        );
    }

    @DisplayName("주문 상태변경")
    @Test
    void changeOrderStatus() {
        // given
        ID로_주문_조회(order);

        OrderRequest request = new OrderRequest(1L, "MEAL");

        // when
        OrderResponse actual = orderService.changeOrderStatus(1L, request);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo("MEAL");
        Mockito.verify(orderRepository).save(Mockito.any());
    }

    @DisplayName("이미 완료상태이면 변경 불가능")
    @Test
    void changeOrderStatusFailWhenAlreadyCompleted() {
        // given
        Order order = new Order(1L, orderTable, OrderStatus.COMPLETION, LocalDateTime.now(), new OrderLineItems());
        ID로_주문_조회(order);

        OrderRequest request = new OrderRequest(1L, "MEAL");

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> orderService.changeOrderStatus(1L, request))
            .withMessage("완료된 주문의 상태를 바꿀 수 없습니다.");
    }

    private void ID로_주문_조회(Order order) {
        Mockito.when(orderRepository.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(order));
    }

    private void 주문_저장(Order order) {
        Mockito.when(orderRepository.save(Mockito.any()))
            .thenReturn(order);
    }

    private void 유효성_확인시_에러_던지지_않음() {
        Mockito.doNothing()
            .when(orderValidator).validateMenu(Mockito.any());
    }

    private void 유효성_주문_항목_개수_다름_에러() {
        Mockito.doThrow(new KitchenposException(KitchenposErrorCode.INVALID_ORDER_LINE_ITEM_SIZE))
            .when(orderValidator).validateMenu(Mockito.any());
    }

    private void 조회한_주문_테이블_반환(OrderTable orderTable) {
        Mockito.when(orderTableRepository.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(orderTable));
    }

    private void 주문_전쳬_조회() {
        Mockito.when(orderRepository.findAll())
            .thenReturn(Collections.singletonList(order));
    }
}
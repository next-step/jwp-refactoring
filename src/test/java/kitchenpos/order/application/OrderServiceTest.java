package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuService menuService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderRequest orderRequest;
    private OrderLineItemRequest orderLineItemRequest;

    @BeforeEach
    void setUp() {
        orderLineItemRequest = new OrderLineItemRequest(1l,1l,1l,11l);
        orderRequest = new OrderRequest(1l, "COOKING", Arrays.asList(orderLineItemRequest));
    }

    @DisplayName("주문 항목이 없으면 주문할 수 없다.")
    @Test
    void createFailBecauseOfWrongProductTest() {
        //given
        orderRequest = new OrderRequest(1l, "COOKING", new ArrayList<>());

        //when && then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 항목이 비어있습니다.");

    }


    @DisplayName("등록된 메뉴만 주문 할 수 있다.")
    @Test
    void createFailBecauseOfNotExistMenuTest() {
        //given
        doThrow(new IllegalArgumentException("등록된 메뉴가 아닙니다.")).when(menuService).findById(any());

        //when && then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록된 메뉴가 아닙니다.");
    }

    @DisplayName("주문 테이블이 존재해야 한다.")
    @Test
    void createFailBecauseOfNotExistTableTest() {
        //given
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());
        Menu menu = new Menu();
        given(menuService.findById(any())).willReturn(menu);

        //when && then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록되지 않은 주문 테이블입니다.");

    }
//
    @DisplayName("주문 테이블은 비어있지 않아야 한다.")
    @Test
    void createFailBecauseOfEmptyTableTest() {
        //given
        given(menuService.findById(any())).willReturn(new Menu());
        OrderTable givenOrderTable = new OrderTable(1L, null, 0, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(givenOrderTable));

        //when && then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 테이블은 주문 할 수 없습니다.");

    }

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void createTest() {
        //given
        given(menuService.findById(any())).willReturn(new Menu());
        OrderTable givenOrderTable = new OrderTable(1L, null, 0, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(givenOrderTable));
        Order order = new Order(givenOrderTable, OrderStatus.COOKING, LocalDateTime.now(), new OrderLineItems(Arrays.asList(new OrderLineItem())));
        given(orderRepository.save(any())).willReturn(order);


        //when
        OrderResponse orderResponse = orderService.create(orderRequest);

        //then
        assertThat(orderRequest.getOrderTableId()).isEqualTo(orderResponse.getOrderTableId());
    }


    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        OrderTable givenOrderTable = new OrderTable(1L, null, 0, false);
        Order order = new Order(givenOrderTable, OrderStatus.COOKING, LocalDateTime.now(), new OrderLineItems(Arrays.asList(new OrderLineItem())));
        List<Order> expect = Arrays.asList(order);
        given(orderRepository.findAll())
                .willReturn(expect);

        //when
        List<OrderResponse> result = orderService.list();

        //then
        assertThat(result.size()).isEqualTo(expect.size());
        List<Long> orderTalbeIds = result.stream().map(OrderResponse::getOrderTableId).collect(Collectors.toList());
        assertThat(orderTalbeIds).contains(givenOrderTable.getId());
    }

    @DisplayName("등록되지 않은 주문은 변경할 수 없다.")
    @Test
    void changeOrderStatusFailBecauseOfNotExistOrderTest() {
        //given
        given(orderRepository.findById(any())).willReturn(Optional.empty());

        //when && then
        assertThatThrownBy(() -> orderService.changeOrderStatus(any(), orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록되지 않은 주문입니다.");

    }

    @DisplayName("완료된 주문은 변경할 수 없다")
    @Test
    void changeOrderStatusFailBecauseOfOrderStatusTest() {
        //given
        OrderTable givenOrderTable = new OrderTable(1L, null, 0, false);
        Order order = new Order(givenOrderTable, OrderStatus.COMPLETION, LocalDateTime.now(), new OrderLineItems(Arrays.asList(new OrderLineItem())));
        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        //when && then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1l, orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 완료된 주문입니다.");

    }

    @DisplayName("주문 상태를 변경할 수 있다")
    @Test
    void changeOrderStatusTest() {
        //given
        OrderTable givenOrderTable = new OrderTable(1L, null, 0, false);
        Order order = new Order(givenOrderTable, OrderStatus.MEAL, LocalDateTime.now(), new OrderLineItems(Arrays.asList(new OrderLineItem())));
        given(orderRepository.findById(any())).willReturn(Optional.of(order));
        OrderRequest changStatus = new OrderRequest(1l, "COOKING", Arrays.asList(orderLineItemRequest));

        //when
        OrderResponse result = orderService.changeOrderStatus(1l, changStatus);

        //
        assertThat(result.getOrderStatus()).isEqualTo(changStatus.getOrderStatus());

    }

}
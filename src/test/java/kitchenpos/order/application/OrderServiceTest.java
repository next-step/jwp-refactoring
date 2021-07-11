package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.MenuEntity;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.application.OrderTableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableEntity;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuService menuService;

    @Mock
    private OrderTableService orderTableService;

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
        assertThatThrownBy(() -> orderService.createTemp(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 항목이 비어있습니다.");

    }


    @DisplayName("등록된 메뉴만 주문 할 수 있다.")
    @Test
    void createFailBecauseOfNotExistMenuTest() {
        //given
        given(menuService.countByIdIn(any())).willReturn(0);

        //when && then
        assertThatThrownBy(() -> orderService.createTemp(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록되지 않은 메뉴가 있습니다.");
    }

    @DisplayName("주문 테이블이 존재해야 한다.")
    @Test
    void createFailBecauseOfNotExistTableTest() {
        //given
        given(menuService.countByIdIn(any())).willReturn(1);
        doThrow(new IllegalArgumentException("등록되지 않은 주문 테이블입니다.")).when(orderTableService).findById(any());

        //when && then
        assertThatThrownBy(() -> orderService.createTemp(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록되지 않은 주문 테이블입니다.");

    }
//
    @DisplayName("주문 테이블은 비어있지 않아야 한다.")
    @Test
    void createFailBecauseOfEmptyTableTest() {
        //given
        given(menuService.countByIdIn(any())).willReturn(1);
        OrderTableEntity givenOrderTable = new OrderTableEntity(1L, null, 0, true);
        given(orderTableService.findById(any())).willReturn(givenOrderTable);

        //when && then
        assertThatThrownBy(() -> orderService.createTemp(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 테이블은 주문 할 수 없습니다.");

    }

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void createTest() {
        //given
        given(menuService.findById(any())).willReturn(new MenuEntity());
        OrderTableEntity givenOrderTable = new OrderTableEntity(1L, null, 0, false);
        given(orderTableService.findById(any())).willReturn(givenOrderTable);
        OrderEntity orderEntity = new OrderEntity(givenOrderTable, OrderStatus.COOKING, LocalDateTime.now(), new OrderLineItems(Arrays.asList(new OrderLineItemEntity())));
        given(orderRepository.save(any())).willReturn(orderEntity);


        //when
        OrderResponse orderResponse = orderService.createTemp(orderRequest);

        //then
        assertThat(orderRequest.getOrderTableId()).isEqualTo(orderResponse.getOrderTableId());
    }

}
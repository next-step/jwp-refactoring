package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.domain.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("주문 서비스")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderService orderService;

    private OrderTable 주문테이블;
    private Order 주문;
    private OrderLineItem 주문항목;
    private Menu 매콤치킨단품;

    @BeforeEach
    void setUp() {
        Product 매콤치킨 = new Product(1L, "매콤치킨", BigDecimal.valueOf(13000));
        MenuGroup 인기메뉴그룹 = new MenuGroup(1L, "인기메뉴");
        MenuProduct 매콤치킨구성 = new MenuProduct(매콤치킨, 1L);
        매콤치킨단품 = Menu.of("매콤치킨단품", BigDecimal.valueOf(13000), 인기메뉴그룹, Collections.singletonList(매콤치킨구성));

        주문테이블 = new OrderTable(1L, 2, new TableState(false));
        주문항목 = new OrderLineItem(매콤치킨단품, 1L);
        주문 = new Order(주문테이블, COOKING, Collections.singletonList(주문항목));
    }

    @Test
    @DisplayName("주문을 등록한다.")
    void create() {
        OrderLineItemRequest 주문항목 = OrderLineItemRequest.of(1L, 1L);
        OrderRequest 주문요청 = new OrderRequest(주문테이블.getId(), Collections.singletonList(주문항목));

        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(매콤치킨단품));
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(주문테이블));
        when(orderRepository.save(any())).thenReturn(주문);

        OrderResponse response = orderService.create(주문요청);

        verify(menuRepository, times(1)).findById(anyLong());
        verify(orderTableRepository, times(1)).findById(anyLong());
        verify(orderRepository, times(1)).save(any(Order.class));
        assertThat(response)
                .extracting("orderTableId", "orderStatus")
                .containsExactly(주문테이블.getId(), COOKING.name());
    }

    @Test
    @DisplayName("주문 항목의 목록이 비어있는 경우 예외가 발생한다.")
    void validateOrderLineItemsEmpty() {
        OrderRequest 주문요청 = new OrderRequest(주문테이블.getId(), Collections.emptyList());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문요청));
    }

    @Test
    @DisplayName("주문 항목의 메뉴가 기존에 등록된 메뉴가 아닌 경우 예외가 발생한다.")
    void validateOrderLineItems() {
        OrderLineItemRequest 주문항목 = OrderLineItemRequest.of(1L, 1L);
        OrderRequest 주문요청 = new OrderRequest(주문테이블.getId(), Collections.singletonList(주문항목));

        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(매콤치킨단품));
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문요청));
        verify(menuRepository, times(1)).findById(anyLong());
        verify(orderTableRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블인 경우 예외가 발생한다.")
    void validateOrderTableEmpty() {
        OrderTable 빈테이블 = new OrderTable(1L, 0, new TableState(true));
        OrderLineItemRequest 주문항목 = OrderLineItemRequest.of(1L, 1L);
        OrderRequest 주문요청 = OrderRequest.of(빈테이블.getId(), Collections.singletonList(주문항목));

        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(매콤치킨단품));
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(빈테이블));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문요청));
        verify(menuRepository, times(1)).findById(anyLong());
        verify(orderTableRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void list() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(주문));

        List<OrderResponse> orders = orderService.list();

        verify(orderRepository, times(1)).findAll();
        assertThat(orders).hasSize(1);
        assertThat(orders)
                .extracting( "orderTableId", "orderStatus", "orderedTime")
                .containsExactly(
                        tuple(주문테이블.getId(), 주문.getOrderStatus(), 주문.getOrderedTime())
                );
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        OrderStatusRequest 주문상태 = new OrderStatusRequest("MEAL");
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(주문));

        OrderResponse response = orderService.changeOrderStatus(1L, 주문상태);

        verify(orderRepository, times(1)).findById(anyLong());
        assertThat(response.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @Test
    @DisplayName("주문 번호를 조회할 수 없는 경우 예외가 발생한다.")
    void validateOrderId() {
        OrderStatusRequest 주문상태 = new OrderStatusRequest("MEAL");
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(1L, 주문상태));
        verify(orderRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("주문 상태가 완료인 경우 예외가 발생한다.")
    void validateOrderStatus() {
        OrderStatusRequest 주문상태 = new OrderStatusRequest("COMPLETION");
        주문.setOrderStatus(COMPLETION);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(주문));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(1L, 주문상태));
        verify(orderRepository, times(1)).findById(anyLong());
    }
}
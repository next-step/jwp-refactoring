package kitchenpos.ordering.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.ordering.domain.*;
import kitchenpos.ordering.dto.OrderRequest;
import kitchenpos.ordering.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 서비스 관련 테스트")
public class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderLineItemRepository orderLineItemRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    private Long order1Id = 1L;
    private Long order1OrderTableId = 1L;
    private OrderStatus order1OrderStatus = OrderStatus.COOKING;
    private LocalDateTime order1OrderTime = LocalDateTime.now();
    private OrderLineItem orderLineItem = new OrderLineItem(1L, null, 1L, 1);
    private List<OrderLineItem> order1OrderLineItems = Arrays.asList(orderLineItem);
    private OrderTable orderTable = new OrderTable(1L, 5, false);

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuRepository, orderRepository, orderLineItemRepository, orderTableRepository);
    }

    @DisplayName("주문을 등록할 수 있다")
    @Test
    void create() {
        OrderRequest orderRequest1 = new OrderRequest(order1OrderTableId, order1OrderLineItems);
        Ordering order1 = orderRequest1.toEntity();

        when(menuRepository.countByIdIn(any())).thenReturn(1L);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderRepository.save(any())).thenReturn(order1);

        OrderResponse orderResponse = orderService.create(orderRequest1);

        assertThat(orderResponse.getId()).isEqualTo(order1.getId());
        assertThat(orderResponse.getOrderTableId()).isEqualTo(order1.getOrderTableId());
    }

    @DisplayName("주문에 주문항목이 없으면 등록할 수 없다.")
    @Test
    void 주문의_주문항목이_올바르지_않으면_등록할_수_없다_1() {
        List<OrderLineItem> emptyOrderLineItems = Arrays.asList();
        OrderRequest orderRequest1 = new OrderRequest(order1OrderTableId, OrderStatus.MEAL, emptyOrderLineItems);

        assertThatThrownBy(() -> {
            orderService.create(orderRequest1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문에 있는 주문항목들은 모두 등록되어 있어야 한다.")
    @Test
    void 주문의_주문항목이_올바르지_않으면_등록할_수_없다_2() {
        long falseCount = 2;
        OrderRequest orderRequest1 = new OrderRequest(order1OrderTableId, OrderStatus.MEAL, order1OrderLineItems);

        when(menuRepository.countByIdIn(any())).thenReturn(falseCount);

        assertThatThrownBy(() -> {
            orderService.create(orderRequest1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 주문테이블이 주문테이블로 등록 안되어 있으면 등록할 수 없다.")
    @Test
    void 주문의_주문테이블이_올바르지_않으면_등록할_수_없다_1() {
        OrderRequest orderRequest1 = new OrderRequest(order1OrderTableId, order1OrderLineItems);

        when(menuRepository.countByIdIn(any())).thenReturn(1L);
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            orderService.create(orderRequest1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 주문테이블이 비어있으면 주문할 수 없다.")
    @Test
    void 주문의_주문테이블이_올바르지_않으면_등록할_수_없다_2() {
        OrderTable falseOrderTable = new OrderTable(1L, 0, true);
        OrderRequest orderRequest1 = new OrderRequest(order1OrderTableId, OrderStatus.MEAL, order1OrderLineItems);

        when(menuRepository.countByIdIn(any())).thenReturn(1L);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(falseOrderTable));

        assertThatThrownBy(() -> {
            orderService.create(orderRequest1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 전체를 조회할 수 있다")
    @Test
    void 주문_전체_조회한다() {
        Ordering order1 = new Ordering(order1Id, order1OrderTableId, order1OrderStatus, order1OrderTime, order1OrderLineItems);

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1));

        assertThat(orderService.list().stream()
        .map(orderResponse -> orderResponse.getId())
        .collect(Collectors.toList())).contains(order1.getId());

//        assertThat(orderService.list()).contains(order1);
//        assertThat(order1.getOrderLineItems()).contains(orderLineItem);
    }

    @DisplayName("주문 상태를 변경할 수 있다")
    @Test
    void 주문_상태_변경한다() {
        Ordering order1 = new Ordering(order1Id, order1OrderTableId, OrderStatus.MEAL, order1OrderTime, order1OrderLineItems);
        OrderRequest orderRequest = new OrderRequest(order1OrderTableId, OrderStatus.MEAL, order1OrderLineItems);

        when(orderRepository.findById(any())).thenReturn(Optional.of(order1));

        OrderResponse orderResponse = orderService.changeOrderStatus(order1.getId(), orderRequest.getOrderStatus());
        assertThat(orderResponse.getId()).isEqualTo(order1.getId());
        assertThat(orderResponse.getOrderStatus()).isEqualTo(order1.getOrderStatus());
    }

    @DisplayName("상태 변경하려는 주문이 없으면 변경할 수 없다.")
    @Test
    void 주문상태가_올바르지_않으면_상태를_변경할_수_없다_1() {
        Ordering order1 = new Ordering(order1Id, order1OrderTableId, OrderStatus.MEAL, order1OrderTime, order1OrderLineItems);
        OrderRequest orderRequest = new OrderRequest(order1OrderTableId, OrderStatus.COMPLETION, order1OrderLineItems);

        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(order1.getId(), orderRequest.getOrderStatus());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상태 변경하려는 주문이 이미 완료된 상태이면 변경할 수 없다.")
    @Test
    void 주문상태가_올바르지_않으면_상태를_변경할_수_없다_2() {
        Ordering order1 = new Ordering(order1Id, order1OrderTableId, OrderStatus.COMPLETION, order1OrderTime, order1OrderLineItems);
        OrderRequest orderRequest = new OrderRequest(order1OrderTableId, OrderStatus.MEAL, order1OrderLineItems);

        when(orderRepository.findById(any())).thenReturn(Optional.of(order1));

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(order1.getId(), orderRequest.getOrderStatus());
        }).isInstanceOf(IllegalArgumentException.class);
    }

}

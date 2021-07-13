package kitchenpos.ordering.application;

import kitchenpos.ordering.domain.*;
import kitchenpos.ordering.dto.OrderRequest;
import kitchenpos.ordering.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.api.Assertions;
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
    private OrderRepository orderRepository;
    @Mock
    private OrderValidator orderValidator;

    private Long order1Id = 1L;
    private Long order1OrderTableId = 1L;
    private OrderStatus order1OrderStatus = OrderStatus.COOKING;
    private LocalDateTime order1OrderTime = LocalDateTime.now();
    private OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1);
    private List<OrderLineItem> order1OrderLineItems = Arrays.asList(orderLineItem);
    private OrderTable orderTable = new OrderTable(1L, 5, false);

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, orderValidator);
    }

    @DisplayName("주문을 등록할 수 있다")
    @Test
    void create() {
        OrderRequest orderRequest1 = new OrderRequest(order1OrderTableId, order1OrderLineItems);
        Ordering order1 = orderRequest1.toEntity();

        when(orderRepository.save(any())).thenReturn(order1);

        OrderResponse orderResponse = orderService.create(orderRequest1);

        Assertions.assertThat(orderResponse.getId()).isEqualTo(order1.getId());
        Assertions.assertThat(orderResponse.getOrderTableId()).isEqualTo(order1.getOrderTableId());
    }

    @DisplayName("주문 전체를 조회할 수 있다")
    @Test
    void 주문_전체_조회한다() {
        Ordering order1 = new Ordering(order1Id, order1OrderTableId, order1OrderStatus, order1OrderTime, order1OrderLineItems);

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1));

        Assertions.assertThat(orderService.list().stream()
                .map(orderResponse -> orderResponse.getId())
                .collect(Collectors.toList())).contains(order1.getId());
    }

    @DisplayName("주문 상태를 변경할 수 있다")
    @Test
    void 주문_상태_변경한다() {
        Ordering order1 = new Ordering(order1Id, order1OrderTableId, OrderStatus.MEAL, order1OrderTime, order1OrderLineItems);
        OrderRequest orderRequest = new OrderRequest(order1OrderTableId, OrderStatus.MEAL, order1OrderLineItems);

        when(orderRepository.findById(any())).thenReturn(Optional.of(order1));

        OrderResponse orderResponse = orderService.changeOrderStatus(order1.getId(), orderRequest.getOrderStatus());
        Assertions.assertThat(orderResponse.getId()).isEqualTo(order1.getId());
        Assertions.assertThat(orderResponse.getOrderStatus()).isEqualTo(order1.getOrderStatus());
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

package kitchenpos.order.application;

import static kitchenpos.order.__fixture__.OrderLineItemTestFixture.주문_항목_생성;
import static kitchenpos.order.__fixture__.OrderLineItemTestFixture.주문_항목_요청_생성;
import static kitchenpos.order.__fixture__.OrderTestFixture.빈_주문_요청_생성;
import static kitchenpos.order.__fixture__.OrderTestFixture.주문_생성;
import static kitchenpos.order.__fixture__.OrderTestFixture.주문_요청_생성;
import static kitchenpos.table.__fixture__.OrderTableTestFixture.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.infra.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.infra.OrderRepository;
import kitchenpos.order.infra.OrderTableRepository;
import kitchenpos.order.request.OrderLineItemRequest;
import kitchenpos.order.request.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("OrderService 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderService orderService;
    private OrderLineItemRequest 주문_항목_요청;
    private OrderLineItem 주문_항목;
    private OrderRequest 주문_요청;
    private Order 주문;

    @BeforeEach
    void setUp() {
        주문_항목 = 주문_항목_생성(1L, "후라이드치킨", BigDecimal.valueOf(16_000), 1L);
        주문_항목_요청 = 주문_항목_요청_생성(1L, "후라이드치킨", BigDecimal.valueOf(16_000), 1L);
        주문_요청 = 주문_요청_생성(1L, Arrays.asList(주문_항목_요청));
        주문 = 주문_생성(1L, OrderStatus.COOKING, LocalDateTime.now(), Arrays.asList(주문_항목));
    }

    @Test
    @DisplayName("주문 시 주문 항목이 비어있으면 Exception")
    public void createEmptyException() {
        final OrderRequest 빈_주문_요청 = 빈_주문_요청_생성(2L);

        assertThatThrownBy(() -> orderService.create(빈_주문_요청)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 시 주문 항목이 메뉴에 존재하지 않으면 Exception")
    public void createOrderLineItemsNotExistsException() {
        given(menuRepository.countByIdIn(any(List.class))).willReturn(2L);
        assertThatThrownBy(() -> orderService.create(주문_요청)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않을 경우 Exception")
    public void createTableNotExistsException() {
        given(menuRepository.countByIdIn(any(List.class))).willReturn(1L);
        given(orderTableRepository.findById(주문_요청.getOrderTableId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(주문_요청)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 비어있을 경우 Exception")
    public void createEmptyTableException() {
        final OrderTable 주문_테이블 = 주문_테이블_생성(1L, null, 4, true);

        given(menuRepository.countByIdIn(any(List.class))).willReturn(1L);
        given(orderTableRepository.findById(주문_요청.getOrderTableId())).willReturn(Optional.of(주문_테이블));

        assertThatThrownBy(() -> orderService.create(주문_요청)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 생성")
    public void create() {
        final OrderTable 주문_테이블 = 주문_테이블_생성(1L, null, 4, false);

        given(menuRepository.countByIdIn(any(List.class))).willReturn(1L);
        given(orderTableRepository.findById(주문_요청.getOrderTableId())).willReturn(Optional.of(주문_테이블));
        given(orderRepository.save(any())).willReturn(주문);

        final Order 생성된_주문 = orderService.create(주문_요청);
        assertThat(생성된_주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    @DisplayName("주문 조회")
    public void list() {
        given(orderRepository.findAll()).willReturn(Arrays.asList(주문));

        assertThat(orderService.list()).contains(주문);
    }

    @Test
    @DisplayName("주문 변경 시 존재하지 않는 주문이면 Exception")
    public void changeOrderStatusNotExistsException() {
        final Order 변경된_주문 = 주문_생성(1L, OrderStatus.MEAL, LocalDateTime.now(), Arrays.asList(주문_항목));

        given(orderRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, 변경된_주문)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 변경 시 완료상태일 경우 Exception")
    public void changeOrderStatusCompletionException() {
        final Order 완료된_주문 = 주문_생성(1L, OrderStatus.COMPLETION, LocalDateTime.now(), Arrays.asList(주문_항목));
        final Order 변경된_주문 = 주문_생성(1L, OrderStatus.MEAL, LocalDateTime.now(), Arrays.asList(주문_항목));

        given(orderRepository.findById(완료된_주문.getId())).willReturn(Optional.of(완료된_주문));

        assertThatThrownBy(() -> orderService.changeOrderStatus(완료된_주문.getId(), 변경된_주문)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 변경")
    public void changeOrderStatus() {
        final Order 변경된_주문 = 주문_생성(1L, OrderStatus.MEAL, LocalDateTime.now(), Arrays.asList(주문_항목));

        given(orderRepository.findById(주문.getId())).willReturn(Optional.of(주문));

        assertThat(orderService.changeOrderStatus(주문.getId(), 변경된_주문).getOrderStatus()).isEqualTo(
                변경된_주문.getOrderStatus());
    }
}

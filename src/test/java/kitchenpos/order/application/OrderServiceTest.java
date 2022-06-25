package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.utils.DomainFixtureFactory.createOrder;
import static kitchenpos.utils.DomainFixtureFactory.createOrderLineItem;
import static kitchenpos.utils.DomainFixtureFactory.createOrderRequest;
import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderLineItemRepository orderLineItemRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderService orderService;

    private OrderTable 주문테이블;
    private OrderLineItem 주문항목;
    private Order 주문;

    @BeforeEach
    void setUp() {
        주문테이블 = createOrderTable(1L, 2, false);
        주문항목 = createOrderLineItem(1L, null, null, 2L);
        주문 = createOrder(1L, 주문테이블, null);
    }

    @DisplayName("주문 생성 테스트")
    @Test
    void create() {
        OrderRequest orderRequest = createOrderRequest(주문테이블.id(), null,
                Lists.newArrayList(new OrderLineItemRequest(주문항목.menu()
                        .id(), 주문항목.quantity().value())));
        주문항목.setOrder(주문);
        given(menuRepository.countByIdIn(Lists.newArrayList(주문항목.menu().id()))).willReturn(주문항목.menu().id());
        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.ofNullable(주문테이블));
        given(orderRepository.save(주문)).willReturn(주문);
        given(orderLineItemRepository.save(주문항목)).willReturn(주문항목);
        OrderResponse orderResponse = orderService.create(orderRequest);
        assertAll(
                () -> assertThat(orderResponse.getOrderTableId()).isEqualTo(주문테이블.id()),
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(COOKING),
                () -> assertThat(orderResponse.getOrderLineItems()).containsExactlyElementsOf(
                        Lists.newArrayList(OrderLineItemResponse.from(주문항목)))
        );
    }

    @DisplayName("주문 생성시 주문항목이 없는 경우 테스트")
    @Test
    void createWithOrderLineItemsEmpty() {
        OrderRequest orderRequest = createOrderRequest(주문테이블.id(), null, new ArrayList<>());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(orderRequest));
    }

    @DisplayName("주문 생성시 주문에 속하는 주문항목 수와 등록된 메뉴들 중 주문에 속한 주문항목의 메뉴들을 실제 조회했을 때 수가 불일치하는 경우 테스트")
    @Test
    void createNotEqualOrderLineItemsSize() {
        OrderRequest orderRequest = createOrderRequest(주문테이블.id(), null, Lists.newArrayList(new OrderLineItemRequest(주문항목.menu()
                .id(), 주문항목.quantity().value())));
        given(menuRepository.countByIdIn(Lists.newArrayList(주문항목.menu().id()))).willReturn(0L);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(orderRequest));
    }

    @DisplayName("주문 생성시 주문테이블이 등록이 안된 경우 테스트")
    @Test
    void createNotFoundOrderTable() {
        OrderRequest orderRequest = createOrderRequest(주문테이블.id(), null, Lists.newArrayList(new OrderLineItemRequest(주문항목.menu()
                .id(), 주문항목.quantity().value())));
        given(menuRepository.countByIdIn(Lists.newArrayList(주문항목.menu().id()))).willReturn(주문항목.menu().id());
        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(orderRequest));
    }

    @DisplayName("주문 생성시 주문테이블이 비어있는 경우 테스트")
    @Test
    void createWithEmptyOrderTable() {
        OrderRequest orderRequest = createOrderRequest(주문테이블.id(), null, Lists.newArrayList(new OrderLineItemRequest(주문항목.menu()
                .id(), 주문항목.quantity().value())));
        주문항목.setOrder(주문);
        주문테이블.changeEmpty(true);
        given(menuRepository.countByIdIn(Lists.newArrayList(주문항목.menu().id()))).willReturn(주문항목.menu().id());
        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.ofNullable(주문테이블));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(orderRequest));
    }

    @DisplayName("주문 목록 조회 테스트")
    @Test
    void list() {
        given(orderRepository.findAll()).willReturn(Lists.newArrayList(주문));
        given(orderLineItemRepository.findAllByOrderId(주문.id())).willReturn(Lists.newArrayList(주문항목));
        List<OrderResponse> orders = orderService.list();
        assertThat(orders).containsExactlyElementsOf(Lists.newArrayList(OrderResponse.from(주문)));
    }

    @DisplayName("주문 상태 변경 테스트")
    @Test
    void changeOrderStatus() {
        OrderRequest orderRequest = createOrderRequest(null, COMPLETION, null);
        given(orderRepository.findById(주문.id())).willReturn(Optional.ofNullable(주문));
        given(orderRepository.save(주문)).willReturn(주문);
        OrderResponse savedOrder = orderService.changeOrderStatus(주문.id(), orderRequest);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(orderRequest.getOrderStatus());
    }

    @DisplayName("주문 상태 변경시 주문 조회안되는 경우 테스트")
    @Test
    void changeOrderStatusWithNotFoundOrder() {
        OrderRequest orderRequest = createOrderRequest(null, COMPLETION, null);
        given(orderRepository.findById(주문.id())).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(주문.id(), orderRequest));
    }

    @DisplayName("주문 상태 변경시 이미 완료인 경우 테스트")
    @Test
    void changeOrderStatusButAlreadyComplete() {
        주문.setOrderStatus(COMPLETION);
        OrderRequest orderRequest = createOrderRequest(null, COOKING, null);
        given(orderRepository.findById(주문.id())).willReturn(Optional.ofNullable(주문));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(주문.id(), orderRequest));
    }

    @DisplayName("주문상태가 조리, 식사인 것이 있는 주문테이블의 비어있는지 여부 변경 테스트")
    @Test
    void validateComplete() {
        given(orderRepository.existsByOrderTableAndOrderStatusIn(주문테이블, Arrays.asList(COOKING, MEAL))).willReturn(true);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.validateComplete(주문테이블))
                .withMessage("주문테이블의 주문이 완료상태가 아닙니다.");
    }
}

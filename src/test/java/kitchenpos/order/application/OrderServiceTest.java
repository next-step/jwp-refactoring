package kitchenpos.order.application;


import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.common.domain.OrderStatus;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.menu.application.MenuGroupServiceTest.메뉴_그룹_등록;
import static kitchenpos.menu.application.MenuServiceTest.메뉴_등록;
import static kitchenpos.menu.application.MenuServiceTest.메뉴_상품_등록;
import static kitchenpos.product.application.ProductServiceTest.상품_등록;
import static kitchenpos.table.application.TableServiceTest.테이블_등록;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 관련 기능")
public class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    private Product 강정치킨;
    private MenuGroup 치킨메뉴;
    private Menu 추천메뉴;
    private OrderTable 빈테이블;
    private OrderTable 테이블;
    private List<OrderLineItem> 주문항목;

    @BeforeEach
    void setUp() {
        강정치킨 = 상품_등록(1L, "강정치킨", 17000);
        치킨메뉴 = 메뉴_그룹_등록(1L, "치킨메뉴");
        추천메뉴 = 메뉴_등록(1L, "추천메뉴", 강정치킨.getPrice(), 치킨메뉴.getId(),
                Arrays.asList(메뉴_상품_등록(1L, 강정치킨.getId(), 1L)));

        빈테이블 = 테이블_등록(1L, 4, true);
        테이블 = 테이블_등록(1L, 4, false);
        주문항목 = Arrays.asList(주문_항목_등록(1L, 추천메뉴.getId(), 1L));
    }

    @Test
    @DisplayName("주문을 등록할 때 주문 항목이 비어있으면 실패한다.")
    void createWithNoOrder() {
        // given
        Order order = 주문_등록(1L, 빈테이블.getId(), Lists.emptyList());

        // when-then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 등록할 때 메뉴가 등록 되어있지 않으면 실패한다.")
    void createWithInvalidMenuId() {
        // given
        Order order = 주문_등록(1L, 빈테이블.getId(), 주문항목);

        // when-then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 등록할 때 주문 테이블 정보가 등록 되어있지 않으면 실패한다.")
    void createWithInvalidOrderTable() {
        // given
        Order order = 주문_등록(1L, 빈테이블.getId(), 주문항목);
        given(menuRepository.countByIdIn(any())).willReturn(1);

        // when-then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 등록할 때 주문 테이블이 비어있으면 실패한다.")
    void createWithEmptyOrderTable() {
        // given
        Order order = 주문_등록(1L, 빈테이블.getId(), 주문항목);
        given(menuRepository.countByIdIn(any())).willReturn(1);
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(빈테이블));

        // when-then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 등록한다.")
    void createOrder() {
        // given
        Order order = 주문_등록(1L, 테이블.getId(), 주문항목);

        given(menuRepository.countByIdIn(any())).willReturn(1);
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(테이블));
        given(orderRepository.save(any())).willReturn(order);

        // when-then
        assertThat(orderService.create(order).getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    @Test
    @DisplayName("주문을 조회한다.")
    void list() {
        // given
        Order order = 주문_등록(1L, 빈테이블.getId(), 주문항목);
        given(orderRepository.findAll()).willReturn(Arrays.asList(order));

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getOrderLineItems()).hasSize(1);
    }

    @Test
    @DisplayName("주문 상태가 완료이면 상태 변경에 실패한다.")
    void changeOrderStatusOfCompleted() {
        // given
        Order order = 주문_등록(1L, 빈테이블.getId(), 주문항목);
        order.changeOrderStatus(OrderStatus.COMPLETION);
        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        // when-then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), OrderStatusRequest.of(OrderStatus.COMPLETION)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        Order order = 주문_등록(1L, 빈테이블.getId(), 주문항목);
        order.changeOrderStatus(OrderStatus.COOKING);

        Order newOrder = 주문_등록(1L, 빈테이블.getId(), 주문항목);
        newOrder.changeOrderStatus(OrderStatus.MEAL);

        given(orderRepository.findById(any())).willReturn(Optional.of(order));
        given(orderRepository.save(any())).willReturn(newOrder);

        // when-then
        assertThat(orderService.changeOrderStatus(order.getId(), OrderStatusRequest.of(OrderStatus.MEAL)).getOrderStatus())
                .isEqualTo(OrderStatus.MEAL);
    }

    private OrderLineItem 주문_항목_등록(Long seq, Long menuId, long quantity) {
        return OrderLineItem.of(menuId, quantity);
    }

    private Order 주문_등록(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        return Order.of(orderTableId, orderLineItems);
    }
}

package kitchenpos.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Product;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@DisplayName("주문 서비스 테스트")
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

    private Product 아메리카노;
    private MenuGroup 메뉴그룹;
    private MenuProduct 기본메뉴_아메리카노;
    private Menu 메뉴;
    private Order 주문;
    private OrderTable 주문테이블;
    private OrderLineItem 아메리카노주문;

    private OrderRequest 주문요청;

    @BeforeEach
    void setUp() {

        주문테이블 = new OrderTable(1, false);
        주문 = new Order(주문테이블);

        아메리카노 = new Product("아메리카노", BigDecimal.valueOf(1000L));
        기본메뉴_아메리카노 = new MenuProduct(아메리카노, 2L);
        메뉴그룹 = new MenuGroup("메뉴그룹");
        메뉴 = new Menu("메뉴", BigDecimal.valueOf(1000), 메뉴그룹, Arrays.asList(기본메뉴_아메리카노));

        OrderLineItemRequest 주문_항목_요청 = new OrderLineItemRequest(1L, 1L);
        주문요청 = new OrderRequest(1L, Arrays.asList(주문_항목_요청));

    }

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        주문.addOrderLineItem(메뉴, 1);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(주문테이블));
        when(menuRepository.findById(any())).thenReturn(Optional.of(메뉴));
        when(menuRepository.countByIdIn(any())).thenReturn(주문.getOrderLineItems().size());
        when(orderRepository.save(any())).thenReturn(주문);

        // when
        OrderResponse 주문_등록 = orderService.create(주문요청);

        // then
        assertThat(주문_등록.getOrderLineItems()).hasSize(주문.getOrderLineItems().size());
    }

    @DisplayName("주문메뉴가 비어있으면 예외가 발생한다.")
    @Test
    void emptyOrderLineItemsException() {
        // given && when && then
        assertThatThrownBy(() -> orderService.create(주문요청))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 메뉴의 개수가 일치하지 않으면 주문 할 수 없다.")
    @Test
    void duplicatedException() {
        // given
        when(menuRepository.countByIdIn(any())).thenReturn(100);

        // when && then
        assertThatThrownBy(() -> orderService.create(주문요청))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void notExistOrderTableException() {
        // given
        when(menuRepository.countByIdIn(any())).thenReturn(1);
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> orderService.create(주문요청))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있다면 예외가 발생한다.")
    @Test
    void emptyOrderTableException() {
        // given
        when(menuRepository.countByIdIn(any())).thenReturn(1);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(주문테이블));
        주문테이블.changeEmptyStatus(true);

        // when && then
        assertThatThrownBy(() -> orderService.create(주문요청))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 조회할 수 있다.")
    @Test
    void findAllOrder() {
       when(orderRepository.findAll()).thenReturn(Arrays.asList(주문));

        List<OrderResponse> 주문_목록_조회_결과 = orderService.list();

        assertAll(
                () -> assertThat(주문_목록_조회_결과).hasSize(1),
                () -> assertThat(주문_목록_조회_결과.get(0).getOrderStatus()).isEqualTo(주문.getOrderStatus().name())
        );
    }


    @DisplayName("등록되지 않은 주문 상태를 수정하면 예외가 발생한다.")
    @Test
    void notExistOrderUpdateStatusException() {
       // given
        OrderStatusRequest 주문상태_식사중_변경 = new OrderStatusRequest(OrderStatus.MEAL);

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문상태_식사중_변경))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("계산 완료된 주문 상태를 수정하면 예외가 발생한다.")
    @Test
    void updateCompletionOrderStatusException() {
            // given
        OrderStatusRequest 주문상태_식사중_변경 = new OrderStatusRequest(OrderStatus.MEAL);
        주문.changeOrderStatus(OrderStatus.COMPLETION);

        when(orderRepository.findById(any())).thenReturn(Optional.of(주문));

        // when && then
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문상태_식사중_변경))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

package kitchenpos.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    private MenuGroup 뼈치킨;
    private Menu 뿌링클_세트1;
    private Menu 뿌링클_세트2;
    private OrderLineItem 주문_메뉴1;
    private OrderLineItem 주문_메뉴2;
    private List<OrderLineItem> 주문_메뉴_목록;

    @BeforeEach
    void setUp() {
        주문테이블 = new OrderTable(2, false);
        주문 = new Order(주문테이블, OrderStatus.COOKING);

        뼈치킨 = new MenuGroup("뼈치킨");
        뿌링클_세트1 = new Menu("뼈치킨 세트1", BigDecimal.valueOf(43000), 뼈치킨);
        뿌링클_세트2 = new Menu("뼈치킨 세트2", BigDecimal.valueOf(50000), 뼈치킨);

        ReflectionTestUtils.setField(주문테이블, "id", 1L);
        ReflectionTestUtils.setField(주문, "id", 1L);
        ReflectionTestUtils.setField(뼈치킨, "id", 1L);
        ReflectionTestUtils.setField(뿌링클_세트1, "id", 1L);
        ReflectionTestUtils.setField(뿌링클_세트2, "id", 2L);

        주문_메뉴1 = new OrderLineItem(주문, 뿌링클_세트1, 1L);
        주문_메뉴2 = new OrderLineItem(주문, 뿌링클_세트2, 1L);

        주문_메뉴_목록 = Arrays.asList(주문_메뉴1, 주문_메뉴2);
        주문.order(주문_메뉴_목록);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void 주문_생성() {
        OrderRequest request = new OrderRequest(주문테이블.getId(), OrderStatus.COOKING,
                OrderLineItemRequest.list(Arrays.asList(주문_메뉴1, 주문_메뉴2)));

        when(orderTableRepository.findById(주문테이블.getId())).thenReturn(Optional.of(주문테이블));
        when(menuRepository.findAllById(request.findAllMenuIds()))
                .thenReturn(Arrays.asList(뿌링클_세트1, 뿌링클_세트2));
        when(orderRepository.save(any(Order.class))).thenReturn(주문);

        OrderResponse orderResponse = orderService.create(request);

        assertThat(orderResponse).satisfies(res -> {
            assertEquals(주문.getId(), res.getId());
            assertEquals(주문테이블.getId(), res.getOrderTableId());
            assertEquals(OrderStatus.COOKING, res.getOrderStatus());
            assertEquals(request.getOrderLineItems().size(), res.getOrderLineItems().size());
        });
    }

    @DisplayName("등록되지 않은 메뉴로 주문을 생성할 수 없다.")
    @Test
    void 등록되지않은_메뉴_주문_생성() {
        OrderRequest request = new OrderRequest(주문테이블.getId(), OrderStatus.COOKING,
                OrderLineItemRequest.list(Arrays.asList(주문_메뉴1, 주문_메뉴2)));

        when(orderTableRepository.findById(주문테이블.getId())).thenReturn(Optional.of(주문테이블));
        when(menuRepository.findAllById(request.findAllMenuIds())).thenReturn(Arrays.asList(뿌링클_세트1));

        assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("등록되지 않은 테이블은 주문을 생성할 수 없다.")
    @Test
    void 등록되지않은_테이블_주문_생성() {
        when(orderTableRepository.findById(주문테이블.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> orderService.create(new OrderRequest(주문테이블.getId(), OrderStatus.COOKING,
                        OrderLineItemRequest.list(Arrays.asList(주문_메뉴1, 주문_메뉴2))))
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("빈 테이블은 주문을 생성할 수 없다.")
    @Test
    void 빈_테이블_주문_생성() {
        // given
        주문테이블.changeEmpty(true, Collections.emptyList());
        OrderRequest request = new OrderRequest(주문테이블.getId(), OrderStatus.COOKING,
                OrderLineItemRequest.list(Arrays.asList(주문_메뉴1, 주문_메뉴2)));

        when(orderTableRepository.findById(주문테이블.getId())).thenReturn(Optional.of(주문테이블));
        when(menuRepository.findAllById(request.findAllMenuIds()))
                .thenReturn(Arrays.asList(뿌링클_세트1, 뿌링클_세트2));

        assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void 주문_목록_조회() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(주문));

        List<OrderResponse> orders = orderService.list();

        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.stream().map(OrderResponse::getId).collect(toList()))
                        .containsExactly(주문.getId())
        );
    }

    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void 주문_상태_변경() {
        when(orderRepository.findById(주문.getId())).thenReturn(Optional.of(주문));
        when(orderRepository.save(주문)).thenReturn(주문);

        orderService.changeOrderStatus(주문.getId(), OrderStatus.COMPLETION);

        assertEquals(OrderStatus.COMPLETION, 주문.getOrderStatus());
    }

    @DisplayName("기등록된 주문이 아니면 주문의 상태를 변경할 수 없다.")
    @Test
    void 등록되지않은_주문_상태_변경() {
        when(orderRepository.findById(주문.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), OrderStatus.COOKING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("완료 상태 주문은 상태를 변경할 수 없다.")
    @Test
    void 완료_주문_상태_변경() {
        주문.changeOrderStatus(OrderStatus.COMPLETION);
        when(orderRepository.findById(주문.getId())).thenReturn(Optional.of(주문));

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), OrderStatus.COMPLETION))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

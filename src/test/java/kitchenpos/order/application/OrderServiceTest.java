package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@DisplayName("주문 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderValidator orderValidator;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderTable 주문테이블_1;
    private Order 주문;
    private MenuGroup 양식;
    private Menu 양식_세트_1;
    private Menu 양식_세트_2;
    private OrderLineItem 주문_메뉴_1;
    private OrderLineItem 주문_메뉴_2;
    private List<OrderLineItem> 주문_메뉴_목록;

    @BeforeEach
    void setUp() {
        주문테이블_1 = new OrderTable(2, false);
        ReflectionTestUtils.setField(주문테이블_1, "id", 1L);
        주문 = new Order(주문테이블_1.getId(), OrderStatus.COOKING);

        양식 = new MenuGroup("양식");
        양식_세트_1 = new Menu("양식 세트1", 43000, 양식);
        양식_세트_2 = new Menu("양식 세트2", 50000, 양식);

        ReflectionTestUtils.setField(주문, "id", 1L);
        ReflectionTestUtils.setField(양식, "id", 1L);
        ReflectionTestUtils.setField(양식_세트_1, "id", 1L);
        ReflectionTestUtils.setField(양식_세트_2, "id", 2L);

        주문_메뉴_1 = new OrderLineItem(주문, 양식_세트_1.getId(), 1L);
        주문_메뉴_2 = new OrderLineItem(주문, 양식_세트_2.getId(), 1L);

        주문_메뉴_목록 = Arrays.asList(주문_메뉴_1, 주문_메뉴_2);
        주문.order(주문_메뉴_목록);
    }

//    @DisplayName("주문 항목은 최소 1개 이상 있어야 한다.")
//    @Test
//    void 주문_항목은_최소_1개_이상_있어야_한다() {
//        // given
//        when(orderTableRepository.findById(주문테이블_1.getId())).thenReturn(Optional.empty());
//        OrderRequest orderRequest = new OrderRequest(주문테이블_1.getId(), OrderStatus.COOKING, OrderLineItemRequest.from(Arrays.asList(주문_메뉴_1, 주문_메뉴_2)));
//
//        // when, then
//        assertThatIllegalArgumentException()
//                .isThrownBy(() -> orderService.create(orderRequest));
//    }
//
//    @DisplayName("주문 항목 속 메뉴들은 모두 등록된 메뉴여야 한다.")
//    @Test
//    void 주문_항목_속_메뉴들은_모두_등록된_메뉴여야_한다() {
//        // given
//        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(주문테이블_1));
//        when(menuRepository.findAllById(anyList())).thenReturn(Arrays.asList(양식_세트_1));
//
//        OrderRequest orderRequest = new OrderRequest(주문테이블_1.getId(), OrderStatus.COOKING, OrderLineItemRequest.from(Arrays.asList(주문_메뉴_1, 주문_메뉴_2)));
//
//        // when, then
//        assertThatIllegalArgumentException()
//                .isThrownBy(() -> orderService.create(orderRequest));
//    }
//
//    @DisplayName("주문 테이블은 등록된 테이블이어야 한다.")
//    @Test
//    void 주문_테이블은_등록된_테이블이어야_한다() {
//        // given
//        when(orderTableRepository.findById(주문테이블_1.getId())).thenReturn(Optional.empty());
//        OrderRequest orderRequest = new OrderRequest(주문테이블_1.getId(), OrderStatus.COOKING, OrderLineItemRequest.from(Arrays.asList(주문_메뉴_1, 주문_메뉴_2)));
//
//        // when, then
//        assertThatIllegalArgumentException()
//                .isThrownBy(() -> orderService.create(orderRequest));
//    }
//
//    @DisplayName("주문 테이블이 빈 테이블이 아니어야 한다.")
//    @Test
//    void 주문_테이블이_빈_테이블이_아니어야_한다() {
//        // given
//        주문테이블_1.changeEmpty(true, Collections.emptyList());
//
//        when(orderTableRepository.findById(주문테이블_1.getId())).thenReturn(Optional.of(주문테이블_1));
//        when(menuRepository.findAllById(anyList())).thenReturn(Arrays.asList(양식_세트_1, 양식_세트_2));
//
//        OrderRequest orderRequest = new OrderRequest(주문테이블_1.getId(), OrderStatus.COOKING, OrderLineItemRequest.from(Arrays.asList(주문_메뉴_1, 주문_메뉴_2)));
//
//        // when, then
//        assertThatIllegalArgumentException()
//                .isThrownBy(() -> orderService.create(orderRequest));
//    }
//
    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void 주문을_생성할_수_있다() {
        // given
        OrderRequest orderRequest = new OrderRequest(주문테이블_1.getId(), OrderStatus.COOKING, OrderLineItemRequest.from(Arrays.asList(주문_메뉴_1, 주문_메뉴_2)));

        doNothing().when(orderValidator).validateCreateOrder(anyLong(), anyList());
        when(orderRepository.save(any(Order.class))).thenReturn(주문);

        // when
        OrderResponse orderResponse = orderService.create(orderRequest);

        // then
        assertThat(orderResponse).satisfies(response -> {
            assertEquals(response.getId(), 주문.getId());
            assertEquals(response.getOrderTableId(), 주문테이블_1.getId());
            assertEquals(response.getOrderStatus(), OrderStatus.COOKING);
            assertEquals(response.getOrderLineItems().size(), orderRequest.getOrderLineItems().size());
        });
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void 주문_목록을_조회할_수_있다() {
        // given
        when(orderRepository.findAll()).thenReturn(Arrays.asList(주문));

        // when
        List<OrderResponse> 조회된_주문_목록 = orderService.list();

        // then
        assertAll(() -> {
            assertThat(조회된_주문_목록).hasSize(1);
            assertThat(조회된_주문_목록.stream().map(OrderResponse::getId).collect(toList())).containsExactly(주문.getId());
        });
    }

    @DisplayName("주문은 등록된 주문이어야 한다.")
    @Test
    void 주문은_등록된_주문이어야_한다() {
        // given
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(주문.getId(), OrderStatus.COOKING));
    }

    @DisplayName("이미 완료된 주문은 상태를 수정할 수 없다.")
    @Test
    void 이미_완료된_주문은_상태를_수정할_수_없다() {
        // given
        주문.updateOrderStatus(OrderStatus.COMPLETION);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(주문));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(주문.getId(), OrderStatus.COMPLETION));
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void 주문_상태를_변경할_수_있다() {
        // given
        주문.updateOrderStatus(OrderStatus.MEAL);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(주문));
        when(orderRepository.save(any(Order.class))).thenReturn(주문);

        // when
        orderService.changeOrderStatus(주문.getId(), OrderStatus.COMPLETION);

        // then
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }
}

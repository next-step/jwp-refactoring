package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private TableService tableService;
    @Mock
    private MenuService menuService;
    @InjectMocks
    private OrderService orderService;

    private Menu 메뉴;
    private MenuGroup 메뉴그룹;
    private Order 주문;
    private List<Order> 주문_목록;
    private OrderTable 주문테이블;
    private OrderLineItem 주문상품;

    private OrderLineItemRequest 주문상품_요청;
    private OrderRequest 주문_요청;
    private OrderRequest 주문_완료_요청;

    @BeforeEach
    void setUp() {
        메뉴그룹 = MenuGroup.of(1L, "메뉴그룹");
        메뉴 = Menu.of(1L, "메뉴", 15000, 메뉴그룹);
        주문테이블 = OrderTable.of(1L, 1, false);
        주문상품 = OrderLineItem.of(메뉴, 1L);
        주문 = Order.of(1L, 주문테이블, OrderStatus.COOKING);
        주문.addOrderLineItems(Collections.singletonList(주문상품));
        주문_목록 = Collections.singletonList(주문);

        주문상품_요청 = OrderLineItemRequest.of(1L, 1L);
        주문_요청 = OrderRequest.of(1L, Collections.singletonList(주문상품_요청));
        주문_완료_요청 = OrderRequest.from(OrderStatus.COMPLETION);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void saveOrder() {
        given(tableService.findOrderTableById(anyLong())).willReturn(주문테이블);
        given(menuService.getMenuById(anyLong())).willReturn(메뉴);
        given(orderRepository.save(any())).willReturn(주문);

        final OrderResponse actual = orderService.create(주문_요청);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isEqualTo(주문.getId()),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(주문.getOrderStatus()),
                () -> assertThat(actual.getOrderLineItems()).hasSize(주문.getOrderLineItems().toList().size()),
                () -> assertThat(actual.getOrderTableResponse().getId()).isEqualTo(주문.getOrderTable().getId()),
                () -> assertThat(actual.getOrderTableResponse().getNumberOfGuests()).isEqualTo(주문.getOrderTable().getNumberOfGuests().toInt())

        );
    }

    @DisplayName("등록된 주문을 조회한다.")
    @Test
    void findOrders() {
        given(orderRepository.findAll()).willReturn(주문_목록);

        final List<OrderResponse> actual = orderService.list();

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.get(0).getId()).isEqualTo(주문_목록.get(0).getId()),
                () -> assertThat(actual.get(0).getOrderStatus()).isEqualTo(주문_목록.get(0).getOrderStatus()),
                () -> assertThat(actual.get(0).getOrderLineItems()).hasSize(주문_목록.get(0).getOrderLineItems().toList().size()),
                () -> assertThat(actual.get(0).getOrderTableResponse().getId()).isEqualTo(주문_목록.get(0).getOrderTable().getId()),
                () -> assertThat(actual.get(0).getOrderTableResponse().getNumberOfGuests()).isEqualTo(주문_목록.get(0).getOrderTable().getNumberOfGuests().toInt())
        );
    }

    @DisplayName("주문 상태를 완료로 변경한다.")
    @Test
    void changeOrderStatus() {
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(주문));

        final OrderResponse actual = orderService.changeOrderStatus(1L, 주문_완료_요청);

        assertThat(actual.getOrderStatus()).isEqualTo(주문_완료_요청.getOrderStatus());
    }
}

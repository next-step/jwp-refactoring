package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderLineItemTestFixture.*;
import static kitchenpos.table.domain.OrderTableTestFixture.orderTable;
import static kitchenpos.order.domain.OrderTestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 비즈니스 로직 테스트")
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

    @BeforeEach
    void setUp() {
        주문테이블 = orderTable(1L, null, 3, false);
    }

    @Test
    @DisplayName("주문 등록시 주문 항목은 필수이다.")
    void createOrderByOrderLineItemIsNull() {
        // given
        OrderRequest orderRequest = orderRequest(1L, Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(orderRequest));
    }

    @Test
    @DisplayName("주문 등록시 주문 항목은 모두 등록된 메뉴여야 한다.")
    void createOrderByCreatedMenu() {
        // given
        OrderLineItemRequest 짜장_탕수육_주문_항목_요청 = orderLineItemRequest(1L, 1L);
        OrderLineItemRequest 짬뽕2_탕수육_주문_항목_요청 = orderLineItemRequest(2L, 1L);
        OrderLineItemRequest 탕수육_1그릇_요청 = orderLineItemRequest(null, 1L);
        OrderRequest orderRequest = orderRequest(1L, Arrays.asList(짜장_탕수육_주문_항목_요청, 짬뽕2_탕수육_주문_항목_요청, 탕수육_1그릇_요청));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(orderRequest));
    }

//    @Test
//    @DisplayName("주문 등록시 주문 테이블은 등록된 테이블이어야 한다.")
//    void createOrderByCreatedOrderTable() {
//        // given
//        given(menuRepository.countByIdIn(Arrays.asList(짜장_탕수육_주문_항목.menuId(), 짬뽕2_탕수육_주문_항목.menuId())))
//                .willReturn(2L);
//        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.empty());
//        OrderRequest orderRequest = orderRequest(1L, Arrays.asList(짜장_탕수육_주문_항목, 짬뽕2_탕수육_주문_항목));
//
//        // when & then
//        assertThatIllegalArgumentException()
//                .isThrownBy(() -> orderService.create(order));
//    }
//
//    @Test
//    @DisplayName("주문 등록시 주문 테이블은 비어있는 테이블일 수 없다.")
//    void createOrderByEmptyOrderTable() {
//        // given
//        OrderTable emptyTable = orderTable(2L, null, 0, true);
//        given(menuRepository.countByIdIn(Arrays.asList(짜장_탕수육_주문_항목.menuId(), 짬뽕2_탕수육_주문_항목.menuId())))
//                .willReturn(2L);
//        given(orderTableRepository.findById(emptyTable.id())).willReturn(Optional.of(emptyTable));
//        OrderRequest orderRequest = orderRequest(1L, emptyTable.id(), Arrays.asList(짜장_탕수육_주문_항목, 짬뽕2_탕수육_주문_항목));
//
//        // when & then
//        assertThatIllegalArgumentException()
//                .isThrownBy(() -> orderService.create(order));
//    }
//
//    @Test
//    @DisplayName("주문을 등록한다.")
//    void createOrder() {
//        // given
//        given(menuRepository.countByIdIn(Arrays.asList(짜장_탕수육_주문_항목.menuId(), 짬뽕2_탕수육_주문_항목.menuId())))
//                .willReturn(2L);
//        given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.of(주문테이블));
//        OrderRequest orderRequest = orderRequest(1L, Arrays.asList(짜장_탕수육_주문_항목, 짬뽕2_탕수육_주문_항목));
//        given(orderRepository.save(order)).willReturn(order);
//
//        // when
//        Order actual = orderService.create(order);
//
//        // then
//        assertAll(
//                () -> assertThat(actual).isNotNull(),
//                () -> assertThat(actual).isInstanceOf(Order.class)
//        );
//    }
//
//    @Test
//    @DisplayName("주문 상태를 변경하려면 주문이 등록되어야 한다.")
//    void updateOrderStatusByNoneOrdered() {
//        // given
//        OrderStatusRequest orderStatusRequest = changeOrderStatusRequest(OrderStatus.MEAL.name());
//        given(orderRepository.findById(주문테이블.id())).willReturn(Optional.empty());
//
//        // when & then
//        assertThatIllegalArgumentException()
//                .isThrownBy(() -> orderService.changeOrderStatus(주문테이블.id(), orderStatusRequest));
//    }
//
//    @Test
//    @DisplayName("주문 상태를 변경하려면 주문상태가 완료가 아니어야 한다.")
//    void updateOrderStatusByOrderStatusIsNotEqualToCompleted() {
//        // given
//        OrderStatusRequest orderStatusRequest = changeOrderStatusRequest(OrderStatus.MEAL.name());
//        OrderRequest orderRequest = orderRequest(2L, Arrays.asList(짜장_탕수육_주문_항목, 짬뽕2_탕수육_주문_항목));
//        given(orderRepository.findById(order.id())).willReturn(Optional.of(order));
//
//        // when & then
//        assertThatIllegalArgumentException()
//                .isThrownBy(() -> orderService.changeOrderStatus(order.id(), orderStatusRequest));
//    }
//
//    @Test
//    @DisplayName("주문 상태를 변경한다.")
//    void updateOrderStatus() {
//        // given
//        OrderStatusRequest orderStatusRequest = changeOrderStatusRequest(OrderStatus.MEAL.name());
//        OrderRequest orderRequest = orderRequest(1L, Arrays.asList(짜장_탕수육_주문_항목, 짬뽕2_탕수육_주문_항목));
//        given(orderRepository.findById(order.id())).willReturn(Optional.of(order));
//
//        // when
//        OrderResponse actual = orderService.changeOrderStatus(order.id(), orderStatusRequest);
//
//        // then
//        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
//    }
//
//    @Test
//    @DisplayName("주문 목록을 조회하면 주문 목록이 반환된다.")
//    void test() {
//
//    }
}

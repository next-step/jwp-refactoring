package kitchenpos.order.application;

import kitchenpos.common.exception.EmptyOrderTableException;
import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.common.exception.OrderStatusCompletedException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("주문 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderValidatorTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableService orderTableService;

    private OrderValidator orderValidator;

    private Order 주문;
    private OrderRequest 주문_요청;
    private OrderLineItem 주문_항목;
    private OrderLineItem 주문_항목2;
    private OrderTable 주문_테이블_1번;
    private OrderTable 주문_테이블_2번;

    @BeforeEach
    void setUp() {
        orderValidator = new OrderValidator(orderTableService);

        주문_테이블_1번 = new OrderTable(1L, null, 3);
        주문_테이블_2번 = new OrderTable(1L, null, 0);

        주문_항목 = new OrderLineItem();
        주문_항목2 = new OrderLineItem();
        주문 = new Order(주문_테이블_1번, Lists.newArrayList(주문_항목, 주문_항목2));

        주문_요청 = new OrderRequest(주문_테이블_1번.getId(), Lists.newArrayList(주문_항목, 주문_항목2));
    }

    @DisplayName("주문 테이블이 존재해야 한다. (주문 테이블이 존재해야 한다)")
    @Test
    void createOrderExistOrderTableExceptionTest() {
        assertThatThrownBy(() -> {
            // given
            final OrderRequest emptyOrderTableItem = new OrderRequest(null, Lists.newArrayList(주문_항목, 주문_항목2));

            // when
            OrderTable createdOrderTable = 주문_요청을_확인한다(emptyOrderTableItem);

            // then
        }).isInstanceOf(NotFoundEntityException.class);
    }

    @DisplayName("주문 테이블이 존재해야 한다. (주문 테이블이 빈 상태가 아니어야 한다)")
    @Test
    void createOrderNotEmptyOrderTableExceptionTest() {
        assertThatThrownBy(() -> {
            when(orderTableService.findOrderTableById(anyLong())).thenReturn(주문_테이블_2번);

            // given
            final OrderRequest emptyOrderTableItem = new OrderRequest(주문_테이블_2번.getId(), Lists.newArrayList(주문_항목, 주문_항목2));

            // when
            OrderTable createdOrderTable = 주문_요청을_확인한다(emptyOrderTableItem);

            // then
        }).isInstanceOf(EmptyOrderTableException.class);
    }

    @DisplayName("주문 상태는 완료가 아니어야 한다.")
    @Test
    void changeOrderNotCompleteStatusExceptionTest() {
        assertThatThrownBy(() -> {
            // given
            주문.changeOrderStatus(OrderStatus.COMPLETION);

            // when
            주문상태_변경을_확인한다(주문);

            // then
        }).isInstanceOf(OrderStatusCompletedException.class);
    }

    private OrderTable 주문_요청을_확인한다(OrderRequest orderRequest) {
        return orderValidator.validateCreateOrder(orderRequest);
    }

    private void 주문상태_변경을_확인한다(Order order) {
        orderValidator.validateChangeOrderStatus(order);
    }
}

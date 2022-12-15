package kitchenpos.order.validator;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.fixture.TestMenuFactory;
import kitchenpos.fixture.TestOrderFactory;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.domain.NumberOfGuests;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("주문 Validator 테스트")
@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    @InjectMocks
    private OrderValidator orderValidator;

    @Mock
    private OrderTableRepository orderTableRepository;

    @DisplayName("주문 생성시 정상적으로 유효성 검사가 성공한다")
    @Test
    void validateCreateOrder() {
        // given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(4), false);
        Menu menu = TestMenuFactory.create("불고기", BigDecimal.valueOf(10_000), 1L, new ArrayList<>());
        OrderRequest request = OrderRequest.of(
                orderTable.getId(),
                Arrays.asList(OrderLineItemRequest.of(1L, 1L))
        );

        when(orderTableRepository.findById(request.getOrderTableId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertDoesNotThrow(() -> orderValidator.validateCreateOrder(request, Arrays.asList(menu)));
    }

    @DisplayName("주문 생성시 주문 테이블 정보가 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void validateNotExistOrderTable() {
        // given
        Menu menu = TestMenuFactory.create("불고기", BigDecimal.valueOf(10_000), 1L, new ArrayList<>());
        OrderRequest request = OrderRequest.of(
                1L,
                Arrays.asList(OrderLineItemRequest.of(1L, 1L))
        );

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderValidator.validateCreateOrder(request, Arrays.asList(menu)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_TABLE_IS_NOT_EXIST.getMessage());
    }

    @DisplayName("주문 생성시 주문 테이블이 빈 상태라면 예외가 발생한다.")
    @Test
    void validateEmptyOrderTable() {
        // given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(4), true);
        Menu menu = TestMenuFactory.create("불고기", BigDecimal.valueOf(10_000), 1L, new ArrayList<>());
        OrderRequest request = OrderRequest.of(
                orderTable.getId(),
                Arrays.asList(OrderLineItemRequest.of(1L, 1L))
        );

        when(orderTableRepository.findById(request.getOrderTableId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> orderValidator.validateCreateOrder(request, Arrays.asList(menu)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_TABLE_IS_EMPTY_STATUS.getMessage());
    }

    @DisplayName("주문 생성시 주문항목이 비어있다면 예외가 발생한다.")
    @Test
    void validateEmptyOrderLineItems() {
        // given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(4), false);
        Menu menu = TestMenuFactory.create("불고기", BigDecimal.valueOf(10_000), 1L, new ArrayList<>());
        OrderRequest request = OrderRequest.of(orderTable.getId(), new ArrayList<>());

        when(orderTableRepository.findById(request.getOrderTableId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> orderValidator.validateCreateOrder(request, Arrays.asList(menu)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_LINE_ITEMS_IS_EMPTY.getMessage());
    }

    @DisplayName("주문상태를 변경할 수 있도록 유효성 검증이 성공한다.")
    @Test
    void validateUpdateOrderStatus() {
        // given
        Order order = TestOrderFactory.createMealOrder();

        // when & then
        assertDoesNotThrow(() -> orderValidator.validateUpdateOrderStatus(order));
    }

    @DisplayName("결제완료 상태의 주문은 상태를 변경할 경우 예외가 발생한다.")
    @Test
    void validateCompleteOrderUpdateStatus() {
        // given
        Order order = TestOrderFactory.createCompleteOrder();

        // when & then
        assertThatThrownBy(() -> orderValidator.validateUpdateOrderStatus(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_STATUS_COMPLETE.getMessage());
    }
}

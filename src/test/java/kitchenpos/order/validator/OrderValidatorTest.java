package kitchenpos.order.validator;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.fixture.TestOrderFactory;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.validator.TableGroupValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("주문 Validator 테스트")
@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    @InjectMocks
    private OrderValidator orderValidator;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @DisplayName("주문 생성시 정상적으로 유효성 검사가 성공 된다")
    @Test
    void validateCreateOrder() {
        // given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(4), false);
        OrderRequest request = OrderRequest.of(
                orderTable.getId(),
                Arrays.asList(OrderLineItemRequest.of(1L, 1L))
        );

        when(orderTableRepository.findById(request.getOrderTableId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertDoesNotThrow(() -> orderValidator.validateCreateOrder(request));
    }

    @DisplayName("주문 생성시 주문 테이블 정보가 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void validateNotExistOrderTable() {
        // given
        OrderRequest request = OrderRequest.of(
                1L,
                Arrays.asList(OrderLineItemRequest.of(1L, 1L))
        );

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderValidator.validateCreateOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_TABLE_IS_NOT_EXIST.getMessage());
    }

    @DisplayName("주문 생성시 주문 테이블이 빈 상태라면 예외가 발생한다.")
    @Test
    void validateEmptyOrderTable() {
        // given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(4), true);
        OrderRequest request = OrderRequest.of(
                orderTable.getId(),
                Arrays.asList(OrderLineItemRequest.of(1L, 1L))
        );

        when(orderTableRepository.findById(request.getOrderTableId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> orderValidator.validateCreateOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_TABLE_IS_EMPTY.getMessage());
    }

    @DisplayName("주문 생성시 주문항목이 비어있다면 예외가 발생한다.")
    @Test
    void validateEmptyOrderLineItems() {
        // given
        OrderTable orderTable = new OrderTable(1L, new NumberOfGuests(4), false);
        OrderRequest request = OrderRequest.of(orderTable.getId(), new ArrayList<>());

        when(orderTableRepository.findById(request.getOrderTableId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> orderValidator.validateCreateOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_LINE_ITEMS_IS_EMPTY.getMessage());
    }
}

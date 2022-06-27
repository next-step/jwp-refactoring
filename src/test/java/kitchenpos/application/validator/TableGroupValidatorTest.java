package kitchenpos.application.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.core.exception.BadRequestException;
import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.CannotUpdateException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 그룹 벨리데이터에 대한 단위 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupValidatorTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private TableGroupValidator tableGroupValidator;

    private OrderTableRequest 주문_테이블_request;
    private OrderTableRequest 주문_테이블2_request;
    private List<Long> 요청_주문_테이블_ids;

    private OrderTable 주문_테이블;
    private OrderTable 주문_테이블2;
    private List<OrderTable> 주문_테이블_목록;

    @BeforeEach
    void setUp() {
        주문_테이블_request = new OrderTableRequest(1L, null, 3, true);
        주문_테이블2_request = new OrderTableRequest(2L, null, 5, true);
        요청_주문_테이블_ids = Arrays.asList(주문_테이블_request.getId(), 주문_테이블2_request.getId());

        주문_테이블 = OrderTable.of(1L, null, 3, true);
        주문_테이블2 = OrderTable.of(2L, null, 5, true);
        주문_테이블_목록 = Arrays.asList(주문_테이블, 주문_테이블2);
    }

    @DisplayName("단체지정할 테이블이 2개 미만이면 예외가 발생한다")
    @Test
    void table_group_exception_test() {
        // then
        assertThatThrownBy(() -> {
            tableGroupValidator.validate(Arrays.asList(주문_테이블_request.getId()), null);
        }).isInstanceOf(CannotCreateException.class)
            .hasMessageContaining(ExceptionType.ORDER_TABLE_AT_LEAST_TWO.getMessage());
    }

    @DisplayName("단체지정할 테이블이 없으면 미만이면 예외가 발생한다")
    @Test
    void table_group_exception_test2() {
        // then
        assertThatThrownBy(() -> {
            tableGroupValidator.validate(Collections.emptyList(), null);
        }).isInstanceOf(CannotCreateException.class)
            .hasMessageContaining(ExceptionType.ORDER_TABLE_AT_LEAST_TWO.getMessage());
    }

    @DisplayName("단체지정할 테이블이 모두 존재하지 않으면 예외가 발생한다")
    @Test
    void table_group_exception_test3() {
        // given
        주문_테이블_목록 = Arrays.asList(주문_테이블);

        // then
        assertThatThrownBy(() -> {
            tableGroupValidator.validate(요청_주문_테이블_ids, 주문_테이블_목록);
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.CONTAINS_NOT_EXIST_ORDER_TABLE.getMessage());
    }

    @DisplayName("단체지정할 테이블이 비어있지 않으면 예외가 발생한다")
    @Test
    void table_group_exception_test4() {
        // given
        주문_테이블 = OrderTable.of(1L, null, 3, false);
        주문_테이블_목록 = Arrays.asList(주문_테이블, 주문_테이블2);

        // then
        assertThatThrownBy(() -> {
            tableGroupValidator.validate(요청_주문_테이블_ids, 주문_테이블_목록);
        }).isInstanceOf(CannotCreateException.class)
            .hasMessageContaining(ExceptionType.MUST_NOT_BE_EMPTY_OR_GROUPED_TABLE.getMessage());
    }

    @DisplayName("단체지정할 테이블이 비어있지 않으면 예외가 발생한다")
    @Test
    void table_group_exception_test5() {
        // given
        TableGroup tableGroup = mock(TableGroup.class);
        when(tableGroup.getOrderTables())
            .thenReturn(주문_테이블_목록);
        when(orderRepository.existsByOrderTableInAndOrderStatusIn(
            주문_테이블_목록, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
            .thenReturn(true);

        // then
        assertThatThrownBy(() -> {
            tableGroupValidator.validateOrderTablesStatus(tableGroup);
        }).isInstanceOf(CannotUpdateException.class)
            .hasMessageContaining(ExceptionType.CAN_NOT_UPDATE_TABLE_IN_COOKING_AND_MEAL_STATUS.getMessage());
    }
}

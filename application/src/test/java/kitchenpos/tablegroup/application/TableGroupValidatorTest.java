package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.exception.TableGroupException;

@DisplayName("테이블 그룹 : Validator 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupValidatorTest {

	@Mock
	OrderTableRepository orderTableRepository;

	@Mock
	OrderRepository orderRepository;

	@Mock
	OrderTable orderTable;

	@Mock
	Order order;

	@InjectMocks
	private TableGroupValidator tableGroupValidator;

	@DisplayName("주문 테이블이 없는 경우 예외처리 테스트")
	@Test
	void validateOrderTablesIsNullTest() {
		// given
		List<Long> ids = Arrays.asList(1L, 2L);

		// when
		when(orderTableRepository.findAllByIdIn(anyList())).thenReturn(Collections.emptyList());

		// then
		assertThatThrownBy(() -> {
			tableGroupValidator.findValidatedOrderTables(ids);
		}).isInstanceOf(TableGroupException.class)
			.hasMessageContaining(ErrorCode.ORDER_TABLE_IS_NULL.getMessage());
	}

	@DisplayName("주문 테이블이 하나인 경우 예외처리 테스트")
	@Test
	void validateOrderTablesIsOneTableTest() {
		// given
		List<Long> ids = Collections.singletonList(1L);

		// when
		when(orderTableRepository.findAllByIdIn(anyList())).thenReturn(Collections.singletonList(any()));

		// then
		assertThatThrownBy(() -> {
			tableGroupValidator.findValidatedOrderTables(ids);
		}).isInstanceOf(TableGroupException.class)
			.hasMessageContaining(ErrorCode.NEED_MORE_ORDER_TABLES.getMessage());
	}

	@DisplayName("비어있는 주문 테이블이 있는 경우 예외처리 테스트")
	@Test
	void validateOrderTablesFindAnyNotEmptyTableTest() {
		// given
		List<Long> ids = Arrays.asList(1L, 2L);

		// when
		when(orderTableRepository.findAllByIdIn(ids)).thenReturn(Arrays.asList(orderTable, orderTable));

		// then
		assertThatThrownBy(() -> {
			tableGroupValidator.findValidatedOrderTables(ids);
		}).isInstanceOf(TableGroupException.class)
			.hasMessageContaining(ErrorCode.ORDER_TABLE_IS_EMPTY.getMessage());
	}

	@DisplayName("완료되지 않은 주문이 있는 경우 예외처리 테스트")
	@Test
	void validateCompletionOrderTablesTest() {
		// given
		given(orderTableRepository.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(orderTable, orderTable));
		List<Long> ids = Arrays.asList(0L, 0L);

		// when
		when(orderRepository.findAllByOrderTableIdIn(ids)).thenReturn(
			Arrays.asList(order, order));
		doThrow(TableGroupException.class)
			.when(order)
			.isNotCompletion();

		// then
		assertThatThrownBy(() -> {
			tableGroupValidator.findCompletionOrderTables(anyLong());
		}).isInstanceOf(TableGroupException.class);
	}
}

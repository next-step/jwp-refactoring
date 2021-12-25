package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.order.application.OrderService;
import kitchenpos.ordertable.exception.CanNotEditOrderTableEmptyByGroupException;
import kitchenpos.ordertable.exception.CanNotEditOrderTableNumberOfGuestsByEmptyException;
import kitchenpos.tablegroup.domain.TableGroup;

class OrderTableTest {

	@DisplayName("주문테이블 비어있음 유무 수정: 단체 지정된 테이블이면 예외발생")
	@Test
	void changeEmptyIfNotTableGroup_having_table_group() {
		final OrderTableExternalValidator validator = new OrderTableExternalValidator(mock(OrderService.class));
		final OrderTable 단체주문테이블 = OrderTable.of(1L, null, 2, true);

		TableGroup.of(2L, Arrays.asList(
			단체주문테이블,
			OrderTable.of(2L, null, 3, true)
		));

		assertThatExceptionOfType(CanNotEditOrderTableEmptyByGroupException.class)
			.isThrownBy(() -> 단체주문테이블.changeEmpty(validator,false));
	}

	@DisplayName("주문테이블의 손님 수 수정: 테이블이 비어있으면 예외발생")
	@Test
	void changeNumberOfGuestsIfNotEmpty_empty_order_table() {
		final OrderTable 빈_테이블 = OrderTable.of(1L, null, 0, true);

		assertThatExceptionOfType(CanNotEditOrderTableNumberOfGuestsByEmptyException.class)
			.isThrownBy(() -> 빈_테이블.changeNumberOfGuests(4));
	}
}

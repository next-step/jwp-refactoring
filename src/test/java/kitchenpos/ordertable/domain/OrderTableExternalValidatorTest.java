package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.application.OrderService;
import kitchenpos.ordertable.exception.CanNotEditOrderTableEmptyByStatusException;

@ExtendWith(MockitoExtension.class)
class OrderTableExternalValidatorTest {

	@InjectMocks
	private OrderTableExternalValidator orderTableExternalValidator;

	@Mock
	private OrderService orderService;

	@DisplayName("주문테이블 비어있음 유무 수정시 external 검증: 주문이 `조리` 혹은 `식사` 상태면 예외발생")
	@Test
	void changeEmpty_order_table_status_cooking_or_meal() {
		final OrderTable 비어있지않은_테이블 = OrderTable.of(1L, null, 2, false);
		given(orderService.existsOrderStatusCookingOrMeal(비어있지않은_테이블.getId())).willReturn(true);

		assertThatExceptionOfType(CanNotEditOrderTableEmptyByStatusException.class)
			.isThrownBy(() -> orderTableExternalValidator.changeEmpty(비어있지않은_테이블.getId()));
	}
}

package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.order.exception.OrderException;
import kitchenpos.tablegroup.exception.TableGroupException;

@DisplayName("주문 테이블 : 단위 테스트")
class OrderTableTest {

	@DisplayName("주문 테이블을 비울 때 테이블 그룹이 있는 경우 예외처리 테스트")
	@Test
	void changeEmptyNullTableGroup() {
		// given
		OrderTable orderTable = OrderTable.of(100, 1L, false);

		// when // then
		assertThatThrownBy(() -> {
			orderTable.empty(true);
		}).isInstanceOf(TableGroupException.class)
			.hasMessageContaining(ErrorCode.ALREADY_HAS_TABLE_GROUP.getMessage());
	}

	@DisplayName("주문 테이블의 테이블 그룹을 변경하는 메소드 테스트")
	@Test
	void changeTableGroup() {
		// given
		OrderTable orderTable = OrderTable.of(10, false);

		// when
		orderTable.changeTableGroup(2L);

		// then
		assertThat(orderTable.getTableGroupId()).isEqualTo(2L);
	}

	@DisplayName("주문 테이블의 인원을 변경할 때 음수의 인원일 경우 예외처리 테스트")
	@Test
	void changeNumberOfGuestsUnderZeroGuest() {
		// given
		OrderTable orderTable = OrderTable.of(100, false);

		// when // then
		assertThatThrownBy(() -> {
			orderTable.changeNumberOfGuests(-1);
		}).isInstanceOf(OrderException.class);
	}

	@DisplayName("주문 테이블의 인원을 변경할 때 주문 테이블이 비어있는 경우 예외처리 테스트")
	@Test
	void changeNumberOfGuestsEmptyTable() {
		// given
		OrderTable orderTable = OrderTable.of(100, true);

		// when // then
		assertThatThrownBy(() -> {
			orderTable.changeNumberOfGuests(10);
		}).isInstanceOf(OrderException.class);
	}

	@DisplayName("주문 테이블의 인원을 변경하는 테스트")
	@Test
	void changeNumberOfGuests() {
		// given
		OrderTable orderTable = OrderTable.of(100, false);

		// when
		orderTable.changeNumberOfGuests(10);

		// then
		assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
	}

	@DisplayName("주문 테이블의 테이블 그룹을 해제하는 메소드 테스트")
	@Test
	void unGroup() {
		// given
		OrderTable orderTable1 = OrderTable.of(1000, true);
		orderTable1.changeTableGroup(3L);

		// when
		orderTable1.unGroup();

		// then
		assertThat(orderTable1.getTableGroupId()).isNull();
	}
}

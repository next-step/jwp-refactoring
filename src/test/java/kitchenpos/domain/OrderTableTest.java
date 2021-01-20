package kitchenpos.domain;

import kitchenpos.common.NumberOfGuests;
import kitchenpos.common.TableValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

	private OrderTable orderTable;
	private TableGroup tableGroup;

	@BeforeEach
	void setUp() {
		orderTable = new OrderTable(20, true);
		tableGroup = new TableGroup();
	}

	@DisplayName("주문테이블의 공석 상태를 변경한다.")
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void changeEmpty(boolean isEmpty) {
		// when
		orderTable.changeEmpty(isEmpty);

		// then
		assertThat(orderTable.isEmpty()).isEqualTo(isEmpty);
	}

	@DisplayName("단체지정된 주문테이블의 공석상태를 변경하려 할 때 예외 발생.")
	@Test
	void changeEmpty_ExceptionAlreadyGroup() {
		orderTable.putIntoGroup(tableGroup);

		assertThatThrownBy(() -> orderTable.changeEmpty(true))
				.isInstanceOf(TableValidationException.class)
				.hasMessageMatching(OrderTable.MSG_CANNOT_CHANGE_EMPTY_ALREADY_GROUP);
	}

	@DisplayName("진행중인 주문이 있는 주문테이블의 공석상태를 변경하려 할 때 예외 발생.")
	@Test
	void changeEmpty_ExceptionWhileOngoingOrder() {
		// TODO: 2021-01-21 테이블에 주문 넣기

		assertThatThrownBy(() -> orderTable.changeEmpty(true))
				.isInstanceOf(TableValidationException.class)
				.hasMessageMatching(OrderTable.MSG_CANNOT_CHANGE_EMPTY_ONGOING_ORDER);
	}

	@DisplayName("주문테이블의 인원수를 변경한다.")
	@ParameterizedTest
	@ValueSource(ints = {0, 1, 99})
	void changeNumberOfGuests(int numberOfGuests) {
		orderTable.changeEmpty(false);

		orderTable.changeNumberOfGuests(numberOfGuests);

		assertThat(orderTable.getNumberOfGuests()).isEqualTo(new NumberOfGuests(numberOfGuests));
	}

	@DisplayName("공석상태인 주문테이블의 인원수를 변경하려 할 때 예외 발생.")
	@Test
	void changeNumberOfGuests_ExceptionNotEmpty() {
		orderTable.changeEmpty(true);

		assertThatThrownBy(() -> orderTable.changeNumberOfGuests(5))
				.isInstanceOf(TableValidationException.class)
				.hasMessageMatching(OrderTable.MSG_CANNOT_CHANGE_GUEST_WHILE_EMPTY);
	}

	@DisplayName("주문테이블을 테이블 그룹에 속하게 한다.")
	@Test
	void putIntoGroup() {
		// when
		orderTable.putIntoGroup(tableGroup);

		// then
		assertThat(orderTable.isEmpty()).isFalse();
		assertThat(orderTable.getTableGroup()).isEqualTo(tableGroup);
	}

	@DisplayName("주문테이블 단체지정시 null 을 입력하면 예외 발생.")
	@Test
	void putIntoGroup_ExceptionGroupNull() {
		assertThatThrownBy(() -> orderTable.putIntoGroup(null))
				.isInstanceOf(TableValidationException.class)
				.hasMessageMatching(OrderTable.MSG_TABLE_GROUP_NOT_NULL);
	}

	@DisplayName("이미 테이블이 단체지정 되있을때 다른 그룹에 속하게 하면 예외 발생.")
	@Test
	void putIntoGroup_ExceptionAlreadyGroup() {
		TableGroup beforeTableGroup = new TableGroup();
		orderTable.putIntoGroup(beforeTableGroup);

		assertThatThrownBy(() -> orderTable.putIntoGroup(tableGroup))
				.isInstanceOf(TableValidationException.class)
				.hasMessageMatching(OrderTable.MSG_ORDER_TABLE_ALREADY_GROUP);
	}

	@DisplayName("테이블이 비어있지 않을때 단체지정시 예외 발생.")
	@Test
	void putIntoGroup_ExceptionNotEmpty() {
		orderTable.changeEmpty(false);

		assertThatThrownBy(() -> orderTable.putIntoGroup(tableGroup))
				.isInstanceOf(TableValidationException.class)
				.hasMessageMatching(OrderTable.MSG_ORDER_TABLE_EMPTY);
	}

	@DisplayName("테이블의 단체지정을 해제한다.")
	@Test
	void ungroup() {
		orderTable.putIntoGroup(tableGroup);

		orderTable.ungroup();

		assertThat(orderTable.getTableGroup()).isNull();
	}

	@DisplayName("단체지정을 해제할 때 진행중인 주문이 있을경우 예외 발생.")
	@Test
	void ungroup_OngoingOrder() {
		// TODO: 2021-01-21 단체지정하기
		// TODO: 2021-01-21 테이블에 주문 넣기

		assertThatThrownBy(() -> orderTable.ungroup())
				.isInstanceOf(TableValidationException.class)
				.hasMessageMatching(OrderTable.MSG_ORDER_TABLE_ONGOING);
	}
}

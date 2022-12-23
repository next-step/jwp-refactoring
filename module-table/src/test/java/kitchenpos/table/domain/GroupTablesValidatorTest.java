package kitchenpos.table.domain;

import static kitchenpos.table.exception.CannotCreateGroupTableException.TYPE.HAS_GROUP_TABLE;
import static kitchenpos.table.exception.CannotCreateGroupTableException.TYPE.INVALID_TABLE_COUNT;
import static kitchenpos.table.exception.CannotCreateGroupTableException.TYPE.NOT_EMPTY_ORDER_TABLE;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.TableGroupFixtures;
import kitchenpos.table.exception.CannotCreateGroupTableException;

class GroupTablesValidatorTest {

	GroupTablesValidator groupTablesValidator;

	@BeforeEach
	void setUp() {
		groupTablesValidator = new GroupTablesValidator();
	}

	@Test
	@DisplayName("테이블 그룹화 성공")
	void testValidateTableGroup() {
		OrderTables orderTables = TableGroupFixtures.anEmptyOrderTables(2);

		Assertions.assertThatCode(() -> groupTablesValidator.validate(orderTables))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("두 개 미만의 주문 테이블로 테이블 그룹화 실패")
	void orderTableSizeBelowThanTwo() {
		OrderTables orderTables = TableGroupFixtures.anEmptyOrderTables(1);

		Assertions.assertThatThrownBy(() -> groupTablesValidator.validate(orderTables))
			.isInstanceOf(CannotCreateGroupTableException.class)
			.hasMessage(INVALID_TABLE_COUNT.message);
	}

	@Test
	@DisplayName("비어있지 않은 주문 테이블로 테이블 그룹화 실패")
	void orderTableIsNotEmpty() {
		OrderTables orderTables = TableGroupFixtures.anOccupiedOrderTables(2);

		Assertions.assertThatThrownBy(() -> groupTablesValidator.validate(orderTables))
			.isInstanceOf(CannotCreateGroupTableException.class)
			.hasMessage(NOT_EMPTY_ORDER_TABLE.message);
	}

	@Test
	@DisplayName("이미 테이블 그룹이 존재하는 테이블로 테이블 그룹화 실패")
	void orderTableInAnotherGroup() {
		OrderTables orderTables = TableGroupFixtures.aGroupedOrderTables();

		Assertions.assertThatThrownBy(() -> groupTablesValidator.validate(orderTables))
			.isInstanceOf(CannotCreateGroupTableException.class)
			.hasMessage(HAS_GROUP_TABLE.message);
	}

}

package kitchenpos.table.domain;

import static kitchenpos.table.TableGroupFixtures.aGroupedOrderTables;
import static kitchenpos.table.TableGroupFixtures.anEmptyOrderTables;
import static kitchenpos.table.TableGroupFixtures.anOccupiedOrderTables;
import static kitchenpos.table.exception.CannotCreateGroupTableException.TYPE.HAS_GROUP_TABLE;
import static kitchenpos.table.exception.CannotCreateGroupTableException.TYPE.INVALID_TABLE_COUNT;
import static kitchenpos.table.exception.CannotCreateGroupTableException.TYPE.NOT_EMPTY_ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
		OrderTables orderTables = anEmptyOrderTables(2);

		assertThatCode(() -> groupTablesValidator.validate(orderTables))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("두 개 미만의 주문 테이블로 테이블 그룹화 실패")
	void orderTableSizeBelowThanTwo() {
		OrderTables orderTables = anEmptyOrderTables(1);

		assertThatThrownBy(() -> groupTablesValidator.validate(orderTables))
			.isInstanceOf(CannotCreateGroupTableException.class)
			.hasMessage(INVALID_TABLE_COUNT.message);
	}

	@Test
	@DisplayName("비어있지 않은 주문 테이블로 테이블 그룹화 실패")
	void orderTableIsNotEmpty() {
		OrderTables orderTables = anOccupiedOrderTables(2);

		assertThatThrownBy(() -> groupTablesValidator.validate(orderTables))
			.isInstanceOf(CannotCreateGroupTableException.class)
			.hasMessage(NOT_EMPTY_ORDER_TABLE.message);
	}

	@Test
	@DisplayName("이미 테이블 그룹이 존재하는 테이블로 테이블 그룹화 실패")
	void orderTableInAnotherGroup() {
		OrderTables orderTables = aGroupedOrderTables();

		assertThatThrownBy(() -> groupTablesValidator.validate(orderTables))
			.isInstanceOf(CannotCreateGroupTableException.class)
			.hasMessage(HAS_GROUP_TABLE.message);
	}

}

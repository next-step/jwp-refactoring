package kitchenpos.table.domain;

import static kitchenpos.generator.OrderTableGenerator.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.order.domain.OrderTable;

@DisplayName("단체 지정 테스트")
class TableGroupTest {

	@Test
	@DisplayName("단체 지정 생성")
	void createTableGroupTest() {
		assertThatNoException()
			.isThrownBy(() -> TableGroup.from(
				OrderTables.from(Arrays.asList(비어있는_다섯명_테이블(), 비어있는_두명_테이블()))));
	}

	@Test
	@DisplayName("단체 지정 생성 - 주문 테이블이 null이면 예외 발생")
	void createTableGroupWithNullOrderTableTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> TableGroup.from((OrderTables)null))
			.withMessageEndingWith("필수입니다.");
	}

	@Test
	@DisplayName("단체 지정 생성 - 주문 테이블이 2개보다 적으면 예외 발생")
	void createTableGroupWithLessThanTwoOrderTableTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> TableGroup.from(
				OrderTables.from(Collections.singletonList(비어있는_다섯명_테이블()))
			))
			.withMessageEndingWith("주문 테이블은 2개 이상이어야 합니다.");
	}

	@Test
	@DisplayName("단체 지정 생성 - 주문 테이블이 비어있지 않으면 예외 발생")
	void createTableGroupWithNotEmptyOrderTableTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> TableGroup.from(
				OrderTables.from(Arrays.asList(비어있지_않은_5명_테이블(), 비어있는_두명_테이블()))
			))
			.withMessageEndingWith("주문 테이블들은 비어있고, 그룹이 지정되지 않아야 합니다.");
	}

	@Test
	@DisplayName("단체 지정 생성 - 주문 테이블이 다른 단체 지정에 속해있으면 예외 발생")
	void createTableGroupWithOtherTableGroupOrderTableTest() {
		// given
		OrderTable 비어있는_다섯명_테이블 = 비어있는_다섯명_테이블();
		OrderTable 비어있는_두명_테이블 = 비어있는_두명_테이블();

		// when
		TableGroup.from(OrderTables.from(Arrays.asList(비어있는_다섯명_테이블, 비어있는_두명_테이블)));

		// then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> TableGroup.from(
				OrderTables.from(Arrays.asList(비어있는_다섯명_테이블, 비어있는_두명_테이블))
			))
			.withMessageEndingWith("주문 테이블들은 비어있고, 그룹이 지정되지 않아야 합니다.");
	}

	@Test
	@DisplayName("그룹 지정 해제 가능")
	void ungroupTest() {
		// given
		OrderTable 비어있는_다섯명_테이블 = 비어있는_다섯명_테이블();
		OrderTable 비어있는_두명_테이블 = 비어있는_두명_테이블();
		TableGroup tableGroup = TableGroup.from(OrderTables.from(Arrays.asList(비어있는_다섯명_테이블, 비어있는_두명_테이블)));

		// when
		tableGroup.ungroup();

		// then
		assertThat(비어있는_다섯명_테이블.hasTableGroup()).isFalse();
		assertThat(비어있는_두명_테이블.hasTableGroup()).isFalse();
	}
}

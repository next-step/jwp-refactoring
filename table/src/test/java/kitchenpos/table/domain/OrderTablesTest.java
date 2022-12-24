package kitchenpos.table.domain;

import java.util.Arrays;
import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.generator.OrderTableGenerator;
import kitchenpos.table.table.domain.OrderTables;

@DisplayName("주문 테이블 목록 테스트")
class OrderTablesTest {

	@Test
	@DisplayName("주문 테이블 목록 생성")
	void createOrderTablesTest() {
		Assertions.assertThatNoException()
			.isThrownBy(() -> OrderTables.from(
				Collections.singletonList(OrderTableGenerator.비어있지_않은_5명_테이블())
			));
	}

	@Test
	@DisplayName("주문 테이블 목록 생성 - 주문 테이블이 null이면 예외 발생")
	void createOrderTablesWithNullOrderTableTest() {
		Assertions.assertThatIllegalArgumentException()
			.isThrownBy(() -> OrderTables.from(
				null
			))
			.withMessageEndingWith("필수입니다.");
	}

	@Test
	@DisplayName("주문 테이블 목록 생성 - 주문 테이블이 항목이 null 이면 예외 발생")
	void createOrderTablesWithNullOrderTableItemTest() {
		Assertions.assertThatIllegalArgumentException()
			.isThrownBy(() -> OrderTables.from(
				Collections.singletonList(null)
			))
			.withMessageEndingWith("주문 테이블 리스트에 null이 포함될 수 없습니다.");
	}

	@Test
	@DisplayName("그룹이 없고 빈 상태 체크 - true")
	void isEmptyAndNotGroupTest() {
		OrderTables orderTables = OrderTables.from(
			Collections.singletonList(OrderTableGenerator.비어있는_다섯명_테이블())
		);

		assertThat(orderTables.emptyAndNoGroup()).isTrue();
	}

	@Test
	@DisplayName("그룹이 있고 빈 상태 체크 - false")
	void isEmptyAndGroupTest() {
		OrderTables orderTables = OrderTables.from(
			Collections.singletonList(OrderTableGenerator.비어있지_않은_5명_테이블())
		);

		assertThat(orderTables.emptyAndNoGroup()).isFalse();
	}

	@Test
	@DisplayName("그룹 해제 가능")
	void ungroupTest() {
		OrderTables orderTables = OrderTables.from(
			Arrays.asList(OrderTableGenerator.비어있지_않은_5명_테이블(), OrderTableGenerator.비어있지_않은_2명_테이블())
		);

		orderTables.ungroup();

		assertThat(orderTables.list()).allMatch(orderTable -> !orderTable.hasTableGroup());
	}

}

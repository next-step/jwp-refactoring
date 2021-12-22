package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.tablegroup.domain.TableGroup;

@DisplayName("주문 테이블 일급 컬렉션 : 단위 테스트")
class OrderTablesTest {

	private TableGroup tableGroup;
	private OrderTables orderTables;

	@BeforeEach
	void setup() {
		orderTables = OrderTables.from(Arrays.asList(OrderTable.of(10, tableGroup, true),
			OrderTable.of(10, tableGroup, true)));
		tableGroup = TableGroup.from(orderTables);

	}

	@DisplayName("테이블 그룹 변경 테스트")
	@Test
	void changeTableGroup() {
		// when
		orderTables.changeTableGroup(tableGroup);

		// then
		assertThat(orderTables.getOrderTables().get(0).getTableGroup()).isEqualTo(tableGroup);
	}

	@DisplayName("주문 테이블 일급 콜렉션의 비어있는 상태 반환 테스트")
	@Test
	void isEmpty() {
		// given
		orderTables = OrderTables.from(Collections.emptyList());

		// when
		boolean result = orderTables.isEmpty();

		// then
		assertThat(result).isTrue();
	}

	@DisplayName("주문 테이블 중 비어있는 테이블이 있는지 확인하는 메소드 테스트")
	@Test
	void findAnyEmptyTable() {
		// given
		OrderTable orderTable1 = OrderTable.of(10, true);
		OrderTable orderTable2 = OrderTable.of(10, false);
		orderTables = OrderTables.from(Arrays.asList(orderTable1, orderTable2));

		// when
		boolean result = orderTables.findAnyNotEmptyTable();

		// then
		assertThat(result).isTrue();
	}

	@DisplayName("주문 테이블 일급 컬렉션의 주문 테이블들의 테이블 그룹을 해제하는 메소드 테스트")
	@Test
	void unGroupOrderTables() {
		// given
		OrderTable orderTable1 = OrderTable.of(10, true);
		OrderTable orderTable2 = OrderTable.of(10, true);
		TableGroup tableGroup = TableGroup.from(Arrays.asList(orderTable1, orderTable2));
		Order order1 = Order.of(orderTable1, OrderStatus.COMPLETION);
		Order order2 = Order.of(orderTable2, OrderStatus.COMPLETION);
		orderTable1.addOrder(order1);
		orderTable2.addOrder(order2);
		OrderTables orderTables1 = OrderTables.from(Arrays.asList(orderTable1, orderTable2));
		orderTables1.changeTableGroup(tableGroup);

		// when
		orderTables1.unGroupOrderTables();

		// then
		assertThat(orderTables1.getOrderTables().get(0).getTableGroup()).isNull();
		assertThat(orderTables1.getOrderTables().get(1).getTableGroup()).isNull();
	}
}

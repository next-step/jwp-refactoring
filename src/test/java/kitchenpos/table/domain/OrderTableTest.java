package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.order.domain.NumberOfGuests;

class OrderTableTest {

	private OrderTable 일번테이블;
	private OrderTable 이번테이블;
	private TableGroup 단체지정;

	@BeforeEach
	void setUp() {
		LocalDateTime createdDate = LocalDateTime.of(2021, 7, 4, 0, 0, 0);
		일번테이블 = new OrderTable(null, new NumberOfGuests(3), true);
		이번테이블 = new OrderTable(null, new NumberOfGuests(3), true);
		List<OrderTable> orderTableList = new ArrayList<>();
		orderTableList.add(일번테이블);
		orderTableList.add(이번테이블);
		단체지정 = new TableGroup(createdDate, orderTableList);
	}

	@DisplayName("빈 테이블의 방문 고객수를 변경하면 오류 발생")
	@Test
	void testChangeNumberOfGuestsError() {
		assertThatThrownBy(() -> {
			일번테이블.changeNumberOfGuests(new NumberOfGuests(4));
		}).isInstanceOf(
			IllegalArgumentException.class)
			.hasMessageContaining("비어있는 테이블입니다.");
	}

	@DisplayName("방문 고객수 변경 확인")
	@Test
	void testChangeNumberOfGuests() {
		OrderTable orderTable = new OrderTable(null, new NumberOfGuests(3), false);
		orderTable.changeNumberOfGuests(new NumberOfGuests(4));

		assertThat(orderTable.getNumberOfGuests()).isEqualTo(new NumberOfGuests(4));
	}

	@DisplayName("주문 테이블의 비어있는 상태 변경 테스트")
	@Test
	void testChangeEmpty() {
		OrderTable orderTable = new OrderTable(null, new NumberOfGuests(3), false);
		orderTable.changeEmpty(true);
		assertThat(orderTable.isEmpty()).isTrue();
	}

	@DisplayName("단체 지정 테이블의 경우 비어있는 상태 변경시 오류발생")
	@Test
	void testChangeEmptyError() {
		Assertions.assertThatThrownBy(() -> {
			이번테이블.changeEmpty(true);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("단체 지정되어있는 테이블은 변경할 수 없습니다.");
	}

	@DisplayName("주문상태가 완료가 아닐때 그룹해제시 오류 발생")
	@Test
	void testUnGroup() {
		// OrderTable orderTable = new OrderTable(null, new NumberOfGuests(3), false);
		// List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(null, null, 3));
		// Order order = new Order(orderTable, OrderStatus.MEAL, null, orderLineItems);
		//
		// assertThatThrownBy(() -> {
		// 	orderTable.unGroup();
		// }).isInstanceOf(IllegalArgumentException.class)
		// 	.hasMessageContaining("주문 상태가 완료되어야 단체지정이 해제가능합니다.");

	}
}
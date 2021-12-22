package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.ErrorCode;
import kitchenpos.order.exception.OrderException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.exception.TableException;

@DisplayName("주문 테이블 : 단위 테스트")
class OrderTableTest {

	private TableGroup tableGroup;

	@BeforeEach
	void setup() {
		tableGroup = TableGroup.from(Arrays.asList(OrderTable.of(10, tableGroup, true),
			OrderTable.of(10, tableGroup, true)));
	}

	@DisplayName("주문 테이블을 비울 때 테이블 그룹이 있는 경우 예외처리 테스트")
	@Test
	void changeEmptyNullTableGroup() {
		// given
		OrderTable orderTable = OrderTable.of(100, tableGroup, false);

		// when // then
		assertThatThrownBy(() -> {
			orderTable.empty(true);
		}).isInstanceOf(TableException.class)
			.hasMessageContaining(ErrorCode.ALREADY_HAS_TABLE_GROUP.getMessage());
	}

	@DisplayName("주문 테이블의 테이블 그룹을 변경하는 메소드 테스트")
	@Test
	void changeTableGroup() {
		// given
		OrderTable orderTable = OrderTable.of(10, false);

		// when
		orderTable.changeTableGroup(tableGroup);

		// then
		assertThat(orderTable.getTableGroup()).isEqualTo(tableGroup);
	}

	@DisplayName("주문 테이블에 주문을 추가하는 메소드 테스트")
	@Test
	void addOrder() {
		// given
		OrderTable orderTable = OrderTable.of(100, tableGroup, false);
		Order order = Order.of(orderTable, OrderStatus.COOKING);

		// when
		orderTable.addOrder(order);

		// then
		assertThat(orderTable.getOrders().getOrders()).contains(order);
	}

	@DisplayName("주문 테이블을 비울 때 주문이 완료되지 않은 경우 예외처리 테스트")
	@Test
	void changeEmptyIsNotCompletion() {
		// given
		OrderTable orderTable = OrderTable.of(100, false);
		orderTable.addOrder(Order.of(orderTable, OrderStatus.COOKING));

		// when // then
		assertThatThrownBy(() -> {
			orderTable.empty(true);
		}).isInstanceOf(OrderException.class);
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

	@DisplayName("주문 테이블의 테이블 그룹을 해제할 때 주문이 완료되지 않은 경우 예외처리 테스트")
	@Test
	void unGroupIsNotCompletionStatus() {
		// given
		OrderTable orderTable = OrderTable.of(100, tableGroup, false);
		orderTable.addOrder(Order.of(orderTable, OrderStatus.COMPLETION));
		orderTable.addOrder(Order.of(orderTable, OrderStatus.COOKING));

		// when // then
		assertThatThrownBy(() -> {
			orderTable.unGroup();
		}).isInstanceOf(OrderException.class);
	}

	@DisplayName("주문 테이블의 테이블 그룹을 해제하는 메소드 테스트")
	@Test
	void unGroup() {
		// given
		OrderTable orderTable1 = OrderTable.of(1000, true);
		TableGroup tableGroup1 = TableGroup.from(Arrays.asList(orderTable1, orderTable1));
		orderTable1.changeTableGroup(tableGroup1);
		orderTable1.addOrder(Order.of(orderTable1, OrderStatus.COMPLETION));
		orderTable1.addOrder(Order.of(orderTable1, OrderStatus.COMPLETION));

		orderTable1.getOrders().getOrders()
			.forEach(it -> System.out.println(it.getOrderStatus()));

		// when
		orderTable1.unGroup();

		// then
		assertThat(orderTable1.getTableGroup()).isNull();
	}
}

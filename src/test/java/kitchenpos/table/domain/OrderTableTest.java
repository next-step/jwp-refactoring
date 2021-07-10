package kitchenpos.table.domain;

import static java.time.LocalDateTime.*;
import static kitchenpos.order.domain.OrderMenuTest.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTableId;

public class OrderTableTest {

	@DisplayName("주문테이블을 생성시 그룹은 설정 안되어 있다.")
	@Test
	void createTest() {
		// given
		OrderTable orderTable = new OrderTable(1, true);

		// when
		// than
		assertThat(orderTable.isGrouped()).isFalse();
	}

	@DisplayName("그룹 설정이 되어 있는 주문테이블은 비우기 설정을 할 수 없다.")
	@Test
	void emptyGroupedTableTest() {
		// given
		OrderTable groupedTable = createOrderTable(1L, 1L, 1, false);
		ChangeEmptyExternalValidator externalValidator = (orderTableId) -> {};

		// when
		// than
		assertThatThrownBy(() -> groupedTable.changeEmpty(true, externalValidator))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("그룹 설정이 되어 있는 테이블은 주문 등록 불가 상태로 바꿀 수 없습니다.");

		assertThatThrownBy(() -> groupedTable.changeEmpty(false, externalValidator))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("그룹 설정이 되어 있는 테이블은 주문 등록 불가 상태로 바꿀 수 없습니다.");
	}

	@DisplayName("주문테이블의 주문이 조리 상태이거나 식사 상태이면 주문테이블 상태를 바꿀 수 없다.")
	@Test
	void changeEmptyWithCookingOrderTest() {
		// given
		OrderTable orderTable = new OrderTable(1, false);
		ChangeEmptyExternalValidator externalValidator = (orderTableId) -> {
			throw new IllegalArgumentException("조리상태이거나 식사상태주문의 주문테이블은 상태를 변경할 수 없습니다.");
		};

		// when
		// than
		assertThatThrownBy(() -> orderTable.changeEmpty(true, externalValidator))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("조리상태이거나 식사상태주문의 주문테이블은 상태를 변경할 수 없습니다.");

		assertThatThrownBy(() -> orderTable.changeEmpty(false, externalValidator))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("조리상태이거나 식사상태주문의 주문테이블은 상태를 변경할 수 없습니다.");
	}

	@DisplayName("주문테이블은 비우기 설정을 할 수 있다.")
	@Test
	void emptyTest() {
		// given
		OrderTable orderTable = new OrderTable(1, false);
		ChangeEmptyExternalValidator externalValidator = (orderTableId) -> {};
		// when
		orderTable.changeEmpty(true, externalValidator);

		// than
		assertThat(orderTable.isEmpty()).isTrue();

		// when
		orderTable.changeEmpty(false, externalValidator);

		// than
		assertThat(orderTable.isEmpty()).isFalse();
	}

	@DisplayName("방문 손님 수를 음수로 수정할 수 없다.")
	@Test
	void changeNumberOfGuestsNegativeNumberTest() {
		OrderTable orderTable = new OrderTable(1, false);

		assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("방문 손님 수는 음수일 수 없습니다.");
	}

	@DisplayName("빈 테이블의 방문 손님 수는 수정할 수 없다.")
	@Test
	void changeNumberOfGuestsEmptyOrderTableTest() {
		// given
		OrderTable orderTable = new OrderTable(1, true);

		// when
		// than
		assertThatThrownBy(() -> orderTable.changeNumberOfGuests(2))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("빈 테이블은 방문 손님 수를 수정할 수 없습니다.");
	}

	@DisplayName("주문테이블의 방문 손님 수를 수정할 수 있다.")
	@Test
	void changeNumberOfGuests() {
		// given
		OrderTable orderTable = new OrderTable(1, false);

		//when
		orderTable.changeNumberOfGuests(2);

		// than
		assertThat(orderTable.getNumberOfGuests()).isEqualTo(2);
	}

	@DisplayName("주문테이블에서 주문을 생성할 수 있다.")
	@Test
	void createOrderTest() {
		// given
		OrderLineItem orderLineItem = new OrderLineItem(ORDER_MENU, 1);
		OrderTable orderTable = OrderTableTest.createOrderTable(1L, 1L, 1, false);
		LocalDateTime orderedTime = now();

		// when
		Order order = Order.create(orderTable, OrderLineItems.of(orderLineItem), orderedTime);

		// then
		assertThat(order.isCreatedFrom(new OrderTableId(1L))).isTrue();
		assertThat(order.getOrderedTime()).isEqualTo(orderedTime);
		assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
	}

	@DisplayName("빈 주문테이블에서 주문을 생성할 수 없다.")
	@Test
	void createOrderWithEmptyOrderTableTest() {
		// given
		OrderLineItem orderLineItem = new OrderLineItem(ORDER_MENU, 1);
		OrderTable orderTable = new OrderTable(1, true);

		// when
		// than
		assertThatThrownBy(() ->  Order.create(orderTable, OrderLineItems.of(orderLineItem), now()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("빈테이블에서 주문할 수 없습니다.");
	}

	public static OrderTable createOrderTable(Long id, Long groupId, int numberOfGuests, boolean empty) {
		return new OrderTable(id, new TableGroupId(groupId), NumberOfGuests.valueOf(numberOfGuests), empty);
	}
}

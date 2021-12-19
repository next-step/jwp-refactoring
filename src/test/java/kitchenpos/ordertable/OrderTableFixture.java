package kitchenpos.ordertable;

import kitchenpos.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;

public class OrderTableFixture {
	public static OrderTable 빈_주문_테이블() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setTableGroupId(null);
		orderTable.setNumberOfGuests(4);
		orderTable.setEmpty(true);
		return orderTable;
	}

	public static OrderTable 빈_주문_테이블_채워짐() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(빈_주문_테이블().getId());
		orderTable.setTableGroupId(빈_주문_테이블().getTableGroupId());
		orderTable.setNumberOfGuests(빈_주문_테이블().getNumberOfGuests());
		orderTable.setEmpty(false);
		return orderTable;
	}

	public static OrderTable 비어있지않은_주문_테이블() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(2L);
		orderTable.setTableGroupId(null);
		orderTable.setNumberOfGuests(4);
		orderTable.setEmpty(false);
		return orderTable;
	}

	public static OrderTable 비어있지않은_주문_테이블_손님_수_변경됨(int numberOfGuests) {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(2L);
		orderTable.setTableGroupId(null);
		orderTable.setNumberOfGuests(numberOfGuests);
		orderTable.setEmpty(false);
		return orderTable;
	}

	public static OrderTable 주문_테이블_그룹에_속한_주문_테이블() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(3L);
		orderTable.setTableGroupId(1L);
		orderTable.setNumberOfGuests(4);
		orderTable.setEmpty(false);
		return orderTable;
	}

	public static OrderTableRequest 빈_주문_테이블_요청() {
		return new OrderTableRequest(4, true);
	}

	public static OrderTableRequest 비어있지않은_주문_테이블_요청() {
		return new OrderTableRequest(4, false);
	}

	public static OrderTableRequest 비우기_요청() {
		return new OrderTableRequest(true);
	}

	public static OrderTableRequest 채우기_요청() {
		return new OrderTableRequest(false);
	}

	public static OrderTableRequest 손님_수_변경_요청(int numberOfGuests) {
		return new OrderTableRequest(numberOfGuests);
	}
}

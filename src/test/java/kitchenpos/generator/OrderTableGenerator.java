package kitchenpos.generator;

import static org.mockito.Mockito.*;

import kitchenpos.order.domain.NumberOfGuests;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableEmpty;

public class OrderTableGenerator {

	private OrderTableGenerator() {
	}

	public static OrderTable 주문테이블(int numberOfGuests, boolean empty) {
		OrderTable spy = spy(OrderTable.of(NumberOfGuests.from(numberOfGuests), TableEmpty.from(empty)));
		lenient().when(spy.getId()).thenReturn(1L);
		return spy;
	}

	public static OrderTable 비어있지_않은_5명_테이블() {
		return 주문테이블(5, false);
	}

	public static OrderTable 비어있는_테이블() {
		return 주문테이블(0, true);
	}

	public static OrderTable 빈_두명_테이블() {
		return OrderTable.of(NumberOfGuests.from(2), TableEmpty.from(true));
	}

	public static OrderTable 빈_한명_테이블() {
		OrderTable 주문테이블 = spy(주문테이블(1, true));
		lenient().when(주문테이블.getTableGroupId()).thenReturn(2L);
		return 주문테이블;
	}

}

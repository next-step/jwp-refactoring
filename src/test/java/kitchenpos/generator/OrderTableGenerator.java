package kitchenpos.generator;

import static org.mockito.Mockito.*;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.TableEmpty;

public class OrderTableGenerator {

	private OrderTableGenerator() {
	}

	public static OrderTable 주문테이블(int numberOfGuests, boolean empty) {
		OrderTable spy = spy(OrderTable.of(NumberOfGuests.from(numberOfGuests), TableEmpty.from(empty)));
		lenient().when(spy.id()).thenReturn(1L);
		return spy;
	}

	public static OrderTable 비어있지_않은_5명_테이블() {
		OrderTable spy = spy(OrderTable.of(NumberOfGuests.from(2), TableEmpty.from(false)));
		lenient().when(spy.id()).thenReturn(1L);
		return spy;
	}

	public static OrderTable 비어있지_않은_2명_테이블() {
		OrderTable spy = spy(OrderTable.of(NumberOfGuests.from(5), TableEmpty.from(false)));
		lenient().when(spy.id()).thenReturn(1L);
		return spy;
	}


	public static OrderTable 비어있는_다섯명_테이블() {
		OrderTable spy = spy(OrderTable.of(NumberOfGuests.from(5), TableEmpty.from(true)));
		lenient().when(spy.id()).thenReturn(1L);
		return spy;
	}

	public static OrderTable 비어있는_두명_테이블() {
		OrderTable spy = spy(OrderTable.of(NumberOfGuests.from(2), TableEmpty.from(true)));
		lenient().when(spy.id()).thenReturn(2L);
		return spy;
	}

	public static OrderTable 빈_두명_테이블() {
		return OrderTable.of(NumberOfGuests.from(2), TableEmpty.from(true));
	}

	public static OrderTable 빈_한명_테이블() {
		OrderTable 주문테이블 = spy(OrderTable.of(NumberOfGuests.from(1), TableEmpty.from(true)));
		lenient().when(주문테이블.id()).thenReturn(2L);
		return 주문테이블;
	}

}

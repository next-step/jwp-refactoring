package kitchenpos.generator;

import static org.mockito.Mockito.*;

import java.util.Arrays;

import kitchenpos.table.table.domain.NumberOfGuests;
import kitchenpos.table.table.domain.OrderTable;
import kitchenpos.table.table.domain.TableEmpty;
import kitchenpos.table.table.domain.TableGroup;

public class TableGroupGenerator {

	private TableGroupGenerator() {
	}

	public static TableGroup 다섯명_두명_테이블그룹() {
		OrderTable orderTable1 = spy(OrderTable.of(NumberOfGuests.from(5), TableEmpty.from(true)));
		OrderTable orderTable2 = spy(OrderTable.of(NumberOfGuests.from(2), TableEmpty.from(true)));
		TableGroup spy = spy(TableGroup.from(
			Arrays.asList(
				orderTable1,
				orderTable2
			)));
		lenient().when(orderTable1.id()).thenReturn(1L);
		lenient().when(orderTable2.id()).thenReturn(2L);
		lenient().when(spy.id()).thenReturn(1L);
		return spy;
	}
}

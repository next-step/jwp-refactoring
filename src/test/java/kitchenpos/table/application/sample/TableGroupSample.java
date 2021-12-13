package kitchenpos.table.application.sample;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

import java.util.Arrays;
import kitchenpos.table.domain.Headcount;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableStatus;

public class TableGroupSample {

    public static TableGroup tableGroup() {
        TableGroup tableGroup = spy(TableGroup.from(Arrays.asList(
            OrderTable.of(Headcount.from(1), TableStatus.EMPTY),
            OrderTable.of(Headcount.from(2), TableStatus.EMPTY)
        )));
        lenient().when(tableGroup.id()).thenReturn(1L);
        return tableGroup;
    }
}

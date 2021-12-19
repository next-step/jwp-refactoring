package kitchenpos.table.sample;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

import java.util.Arrays;
import kitchenpos.table.domain.Headcount;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupSample {

    public static TableGroup 두명_세명_테이블_그룹() {
        TableGroup tableGroup = spy(TableGroup.from(Arrays.asList(
            OrderTable.empty(Headcount.from(2)),
            OrderTable.empty(Headcount.from(3))
        )));
        lenient().when(tableGroup.id())
            .thenReturn(1L);
        return tableGroup;
    }
}

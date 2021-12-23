package kichenpos.table.group.sample;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

import java.util.Arrays;
import kichenpos.table.group.domain.TableGroup;
import kichenpos.table.table.domain.Headcount;
import kichenpos.table.table.domain.OrderTable;

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

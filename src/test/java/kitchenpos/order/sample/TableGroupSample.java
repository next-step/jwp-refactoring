package kitchenpos.order.sample;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

import java.util.Arrays;
import kitchenpos.order.domain.Headcount;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableStatus;

public class TableGroupSample {

    public static TableGroup 두명_세명_테이블_그룹() {
        TableGroup tableGroup = spy(TableGroup.from(Arrays.asList(
            OrderTable.of(Headcount.from(2), TableStatus.EMPTY),
            OrderTable.of(Headcount.from(3), TableStatus.EMPTY)
        )));
        lenient().when(tableGroup.id())
            .thenReturn(1L);
        return tableGroup;
    }
}

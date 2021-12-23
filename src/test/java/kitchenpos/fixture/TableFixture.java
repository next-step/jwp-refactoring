package kitchenpos.fixture;

import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

import java.time.LocalDateTime;

public class TableFixture {

    public static final OrderTable 테이블_그룹에_속해있지_않은_테이블 = create(1L, null, 0, true);
    public static final OrderTable 테이블_그룹에_속해있는_테이블 = create(2L, 1L, 0, true);
    public static final OrderTable 비어있지_않은_테이블 = create(3L, null, 0, false);
    public static final OrderTable 비어있는_테이블 = create(4L, null, 0, true);
    public static final OrderTable 일반_테이블 = create(1L, null, 4, false);

    private TableFixture() {
        throw new UnsupportedOperationException();
    }

    public static OrderTable create(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        TableGroup tableGroup = tableGroupId == null ? null : TableGroup.of(tableGroupId, LocalDateTime.now());

        return OrderTable.of(id, tableGroup, NumberOfGuests.of(numberOfGuests), Empty.of(empty));
    }
}

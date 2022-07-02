package kitchenpos.table.fixture;

import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

import static kitchenpos.tablegroup.fixture.TableGroupFixture.단체_주문_테이블_그룹;

public class TableFixture {

    public final static OrderTable 비어있는_주문_테이블_그룹_없음 = create(1L, null, 0, true);
    public final static OrderTable 비어있는_주문_테이블_그룹_있음 = create(2L, 단체_주문_테이블_그룹, 0, true);

    public final static OrderTable 주문_테이블_그룹_없음 = create(3L, null, 4, false);
    public final static OrderTable 주문_테이블_여섯이_있음 = create(5L, null, 6, false);

    public final static OrderTable 단체_주문_테이블_4명 = create(6L, null, 4, true);
    public final static OrderTable 단체_주문_테이블_6명 = create(7L, null, 6, true);

    public static OrderTable create(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return OrderTable.of(id, tableGroup, NumberOfGuests.of(numberOfGuests), Empty.of(empty));
    }
}

package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class TableFixture {

    public final static OrderTable 비어있는_주문_테이블_그룹_없음 = create(1L, null, 0, true);
    public final static OrderTable 비어있는_주문_테이블_그룹_있음 = create(2L, 1L, 0, true);

    public final static OrderTable 주문_테이블_그룹_없음 = create(3L, null, 4, false);
    public final static OrderTable 주문_테이블_여섯이_있음 = create(5L, null, 6, false);

    public final static OrderTable 단체_주문_테이블_4명 = create(6L, null, 4, true);
    public final static OrderTable 단체_주문_테이블_6명 = create(7L, null, 6, true);

    public static OrderTable create(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return orderTable;
    }
}

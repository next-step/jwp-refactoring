package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class TableFixture {

    public static final OrderTable 테이블_그룹에_속해있지_않은_테이블 = create(1L, null, 0, true);
    public static final OrderTable 테이블_그룹에_속해있는_테이블 = create(2L, 1L, 0, true);
    public static final OrderTable 비어있지_않은_테이블 = create(3L, null, 0, false);
    public static final OrderTable 비어있는_테이블 = create(4L, null, 0, true);

    public static final OrderTable 회사A_테이블1 = create(1L, 1L, 6, false);
    public static final OrderTable 회사A_테이블2 = create(2L, 1L, 4, false);

    private TableFixture() {
        throw new UnsupportedOperationException();
    }

    public static OrderTable create(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return orderTable;
    }
}

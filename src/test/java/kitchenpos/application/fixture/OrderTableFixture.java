package kitchenpos.application.fixture;

import static kitchenpos.application.fixture.TableGroupFixture.*;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {
    public static OrderTable 주문_개인테이블 = new OrderTable();
    public static OrderTable 빈_개인테이블 = new OrderTable();
    public static OrderTable 주문1_단체테이블 = new OrderTable();
    public static OrderTable 주문2_단체테이블 = new OrderTable();
    public static OrderTable 손님_10명_개인테이블 = new OrderTable();

    static {
        init();
    }

    public static void init() {
        주문_개인테이블.setId(1L);
        주문_개인테이블.setEmpty(false);
        주문_개인테이블.setTableGroupId(null);

        빈_개인테이블.setId(2L);
        빈_개인테이블.setEmpty(true);
        빈_개인테이블.setTableGroupId(null);

        주문1_단체테이블.setId(3L);
        주문1_단체테이블.setEmpty(true);
        주문1_단체테이블.setTableGroupId(null);

        주문2_단체테이블.setId(4L);
        주문2_단체테이블.setEmpty(true);
        주문2_단체테이블.setTableGroupId(null);

        손님_10명_개인테이블.setId(5L);
        손님_10명_개인테이블.setEmpty(false);
        손님_10명_개인테이블.setNumberOfGuests(10);
        손님_10명_개인테이블.setTableGroupId(null);
    }

    private OrderTableFixture() {}
}

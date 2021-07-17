package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {
    public static OrderTable 주문_테이블_조리_중인_주문_테이블 = new OrderTable(1L, null, 2, false);
    public static OrderTable 주문_테이블_식사_중인_주문_테이블 = new OrderTable(2L, null, 3, false);
    public static OrderTable 주문_테이블_계산_완료된_주문_테이블 = new OrderTable(3L, null, 4, false);
    public static OrderTable 주문_테이블_1번_테이블_그룹에_속한_테이블 = new OrderTable(4L, 1L, 5, false);
    public static OrderTable 주문_테이블_고객_0명_미만의_테이블 = new OrderTable(5L, null, -1, false);
    public static OrderTable 주문_테이블_고객_3명의_빈_테이블 = new OrderTable(6L, null, 3, true);
}

package kitchenpos.domain;

import static kitchenpos.domain.TableGroupTest.*;

public class OrderTableTest {
    public static OrderTable 테이블1 = new OrderTable(1L, 그룹1, 0, true);
    public static OrderTable 테이블2 = new OrderTable(2L, 그룹1, 0, true);
    public static OrderTable 테이블3 = new OrderTable(3L, 0, true);
    public static OrderTable 테이블4 = new OrderTable(4L, 0, true);
    public static OrderTable 테이블5 = new OrderTable(5L, 0, true);
    public static OrderTable 테이블6 = new OrderTable(6L, 0, true);
    public static OrderTable 테이블7 = new OrderTable(7L, 0, true);
    public static OrderTable 테이블8 = new OrderTable(8L, 0, true);
    public static OrderTable 테이블9_사용중 = new OrderTable(9L, null, 4, false);
    public static OrderTable 테이블10_사용중 = new OrderTable(10L, null, 8, false);
    public static OrderTable 테이블11_사용중 = new OrderTable(11L, null, 2, false);
    public static OrderTable 테이블12_사용중_주문전 = new OrderTable(12L, 2, false);
}

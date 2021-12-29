package domain.table;

import api.table.domain.OrderTable;


public class OrderTableFixtures {
    public static OrderTable 주문가능_다섯명테이블() {
        return new OrderTable(5, false);
    }

    public static OrderTable 주문불가_다섯명테이블() {
        return new OrderTable(5, true);
    }

    public static OrderTable 주문가능_두명테이블() {
        return new OrderTable(2, false);
    }

    public static OrderTable 그룹화된_테이블() {
        OrderTable orderTable = new OrderTable(3, false);
        orderTable.groupBy(1L);
        return orderTable;
    }

    public static OrderTable 그룹화되지_않은_테이블() {
        return new OrderTable(3, false);
    }
}


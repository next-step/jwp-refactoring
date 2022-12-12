package kitchenpos.table.domain;

import kitchenpos.domain.TableGroup;

public class OrderTableTest {

    public static OrderTable 주문_테이블_생성(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable.Builder()
                .id(id)
                .tableGroup(tableGroup)
                .numberOfGuests(numberOfGuests)
                .empty(empty)
                .build();
    }


}
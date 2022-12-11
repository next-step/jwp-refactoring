package kitchenpos.domain;

public class OrderTableTest {

    public static OrderTable 주문_테이블_생성(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable.Builder()
                .id(id)
                .tableGroupId(tableGroupId)
                .numberOfGuests(numberOfGuests)
                .empty(empty)
                .build();
    }


}
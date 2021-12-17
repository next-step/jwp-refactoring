package kitchenpos.fixture;

import kitchenpos.table.domain.table.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;

public class OrderTableDomainFixture {

    public static OrderTable 양식_테이블 = new OrderTable(0, true);
    public static OrderTableRequest 양식_테이블_요청 = OrderTableRequest.of(양식_테이블.getNumberOfGuests(), 양식_테이블.isEmpty());

    public static OrderTable 한식_테이블 = new OrderTable(0, true);
    public static OrderTableRequest 한식_테이블_요청 = OrderTableRequest.of(한식_테이블.getNumberOfGuests(), 한식_테이블.isEmpty());

}

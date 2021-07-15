package kitchenpos.ordertable.domain;

import org.junit.jupiter.api.DisplayName;

import kitchenpos.generic.guests.domain.NumberOfGuests;

@DisplayName("주문 테이블 단위 테스트")
public class OrderTableTest {
    public static OrderTable 테이블1 = new OrderTable(1L, 1L, NumberOfGuests.of(0), false);
    public static OrderTable 테이블2 = new OrderTable(2L, 1L, NumberOfGuests.of(0), false);
    public static OrderTable 테이블3 = new OrderTable(3L, null, NumberOfGuests.of(0), true);
    public static OrderTable 테이블4 = new OrderTable(4L, null, NumberOfGuests.of(0), true);
    public static OrderTable 테이블5 = new OrderTable(5L, null, NumberOfGuests.of(0), true);
    public static OrderTable 테이블6 = new OrderTable(6L, null, NumberOfGuests.of(0), true);
    public static OrderTable 테이블7 = new OrderTable(7L, null, NumberOfGuests.of(0), true);
    public static OrderTable 테이블8_빈자리 = new OrderTable(8L, null, NumberOfGuests.of(0), true);
    public static OrderTable 테이블9_사용중 = new OrderTable(9L, 2L, NumberOfGuests.of(4), false);
    public static OrderTable 테이블10_사용중 = new OrderTable(10L, 2L, NumberOfGuests.of(8), false);
    public static OrderTable 테이블11_사용중 = new OrderTable(11L, null, NumberOfGuests.of(2), false);
    public static OrderTable 테이블12_사용중_주문전 = new OrderTable(12L, null, NumberOfGuests.of(2), false);

    public static OrderTable orderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroupId, NumberOfGuests.of(numberOfGuests), empty);
    }
}

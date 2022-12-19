package kitchenpos.table.ordertable.domain;

import kitchenpos.table.ordertable.domain.OrderTable;

import java.util.concurrent.atomic.AtomicLong;

public class OrderTableTestFixture {

    public static final boolean 빈_상태 = true;
    public static final boolean 비어있지_않은_상태 = false;
    public static final int 두_명의_방문객 = 2;
    public static final int 한_명의_방문객 = 1;

    private static final AtomicLong 테이블_id = new AtomicLong(100);

    public static OrderTable 빈_테이블() {
        return OrderTable.ofNumberOfGuestsAndEmpty(0, 빈_상태);
    }

    public static OrderTable 두_명의_방문객이_존재하는_테이블() {
        return OrderTable.ofNumberOfGuestsAndEmpty(두_명의_방문객, 비어있지_않은_상태);
    }

    public static OrderTable 두_명의_방문객이_존재하는_테이블_아이디_포함() {
        return OrderTable.ofNumberOfGuestsAndEmpty(테이블_id.getAndIncrement(), 두_명의_방문객, 비어있지_않은_상태);
    }

    public static OrderTable 주문_테이블(int numberOfGuest, boolean empty) {
        return OrderTable.ofNumberOfGuestsAndEmpty(numberOfGuest, empty);
    }

}

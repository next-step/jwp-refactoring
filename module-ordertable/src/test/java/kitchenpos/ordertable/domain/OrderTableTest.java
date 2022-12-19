package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 주문_테이블의_비어있음_여부를_수정할_수_있다() {
        OrderTable orderTable = new OrderTable(1, false);

        orderTable.changeEmpty(true);

        assertThat(orderTable.isEmpty()).isTrue();
    }


    @Test
    void 빈_주문_테이블이면_방문한_손님_수를_수정_할_수_없다() {
        OrderTable orderTable = new OrderTable(1, true);

        ThrowingCallable 빈_주문_테이블_방문한_손님수_수정_할_경우 = () -> orderTable.changeNumberOfGuests(2);

        Assertions.assertThatIllegalArgumentException().isThrownBy(빈_주문_테이블_방문한_손님수_수정_할_경우);
    }

    @Test
    void 방문한_손님_수_수정시_0명_이하로_수정_할_수_없다() {
        OrderTable orderTable = new OrderTable(1, false);

        ThrowingCallable 빈_테이블인_상태에서_방문한_손님_수_수정 = () -> orderTable.changeNumberOfGuests(-1);

        Assertions.assertThatIllegalArgumentException().isThrownBy(빈_테이블인_상태에서_방문한_손님_수_수정);
    }
}

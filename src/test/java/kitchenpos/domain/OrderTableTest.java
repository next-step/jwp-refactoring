package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Arrays;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 이미_단체_지정이_된_주문_테이블은_수정할_수_없다() {
        OrderTable orderTable = new OrderTable(1, false);
        orderTable.changeTableGroup(new TableGroup());

        ThrowingCallable 이미_단체_지정이_된_테이블_수정 = orderTable::validateAlreadyTableGroup;

        assertThatIllegalArgumentException().isThrownBy(이미_단체_지정이_된_테이블_수정);
    }

    @Test
    void 조리_식사_상태의_주문이_포함되어_있으면_수정할_수_없다() {
        OrderTable orderTable = new OrderTable(1, false);
        orderTable.addOrder(new Order(new OrderTable(1, false)));

        ThrowingCallable 조리_식사_상태의_주문이_포함_된_테이블_수정 = () -> orderTable.validateOrderStatus(
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));

        assertThatIllegalArgumentException().isThrownBy(조리_식사_상태의_주문이_포함_된_테이블_수정);
    }

    @Test
    void 비어있음_여부를_수정할_수_있다() {
        OrderTable orderTable = new OrderTable(1, false);

        orderTable.changeEmpty(true);

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    void 방문한_손님_수_수정시_빈_테이블일_경우_수정_할_수_없다() {
        OrderTable orderTable = new OrderTable(1, false);
        orderTable.changeEmpty(true);

        ThrowingCallable 빈_테이블인_상태에서_방문한_손님_수_수정 = orderTable::validateEmpty;

        assertThatIllegalArgumentException().isThrownBy(빈_테이블인_상태에서_방문한_손님_수_수정);
    }

    @Test
    void 방문한_손님_수_수정시_0명_이하로_수정_할_수_없다() {
        OrderTable orderTable = new OrderTable();

        ThrowingCallable 빈_테이블인_상태에서_방문한_손님_수_수정 = () -> orderTable.changeNumberOfGuests(-1);

        assertThatIllegalArgumentException().isThrownBy(빈_테이블인_상태에서_방문한_손님_수_수정);
    }
}

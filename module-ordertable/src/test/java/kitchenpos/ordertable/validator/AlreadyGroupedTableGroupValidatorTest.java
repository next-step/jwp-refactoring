package kitchenpos.ordertable.validator;

import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

public class AlreadyGroupedTableGroupValidatorTest {

    @Test
    void 이미_단체_지정이_된_주문_테이블은_비어있음_여부를_변경할_수_없다() {
        AlreadyGroupedOrderTableValidator validator = new AlreadyGroupedOrderTableValidator();
        OrderTable orderTable = new OrderTable(1, false);
        orderTable.changeTableGroupId(1L);

        ThrowingCallable 이미_단체_지정이_된_주문_테이블의_비어있음_여부를_수정_할_경우 = () -> validator.validate(orderTable);
        Assertions.assertThatIllegalArgumentException().isThrownBy(이미_단체_지정이_된_주문_테이블의_비어있음_여부를_수정_할_경우);
    }
}

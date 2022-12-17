package kitchenpos.validator.tablegroup.impl;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class AlreadyGroupedTableGroupValidatorTest {

    @Test
    void 이미_단체_지정이_된_주문_테이블은_단체_지정_불가능하다() {
        AlreadyGroupedTableGroupValidator validator = new AlreadyGroupedTableGroupValidator();
        OrderTable orderTable = new OrderTable(1, false);
        orderTable.changeTableGroupId(1L);

        ThrowingCallable 이미_단체_지정이_된_주문_테이블을_단체_지정_할_경우 = () -> validator.validate(orderTable);

        assertThatIllegalArgumentException().isThrownBy(이미_단체_지정이_된_주문_테이블을_단체_지정_할_경우);
    }

}

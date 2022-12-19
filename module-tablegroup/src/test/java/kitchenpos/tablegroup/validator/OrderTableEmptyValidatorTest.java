package kitchenpos.tablegroup.validator;

import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class OrderTableEmptyValidatorTest {

    @Test
    void 비어있지_않은_주문_테이블은_단체_지정을_할_수_없다() {
        OrderTableEmptyValidator validator = new OrderTableEmptyValidator();

        ThrowingCallable 비어있지_않은_주문_테이블을_단체_지정_할_경우 = () -> validator.validate(new OrderTable(1, false));

        Assertions.assertThatIllegalArgumentException().isThrownBy(비어있지_않은_주문_테이블을_단체_지정_할_경우);
    }
}

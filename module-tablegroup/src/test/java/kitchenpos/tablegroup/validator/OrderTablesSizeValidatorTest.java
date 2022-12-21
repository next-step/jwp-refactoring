package kitchenpos.tablegroup.validator;

import java.util.Collections;
import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class OrderTablesSizeValidatorTest {

    @Test
    void 최소_2개_이상의_주문_테이블들에_대해서만_단체_지정이_가능하다() {
        OrderTablesSizeValidator validator = new OrderTablesSizeValidator();

        ThrowingCallable 단체_지정이_가능한_주문_테이블_최소_갯수를_지정하지_않은_경우 = () -> validator
                .validate(Collections.singletonList(new OrderTable(1, false)));

        Assertions.assertThatIllegalArgumentException().isThrownBy(단체_지정이_가능한_주문_테이블_최소_갯수를_지정하지_않은_경우);
    }
}

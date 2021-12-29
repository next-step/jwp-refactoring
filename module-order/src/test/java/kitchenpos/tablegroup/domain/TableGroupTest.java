package kitchenpos.tablegroup.domain;


import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import kitchenpos.exception.InvalidParameterException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TableGroup 클래스")
class TableGroupTest {


    private TableGroupValidator tableGroupValidator;

    @Test
    @DisplayName("`단체 지정`의 `주문 테이블`은 최소 2개이상 이어야 등록 할 수 있다.")
    void 주문테이블_2개미만_실패() {
        // given
        TableGroup tableGroup = TableGroup.of();

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableGroup.group(tableGroupValidator,
            Collections.singletonList(1L));

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }
}

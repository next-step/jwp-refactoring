package kitchenpos.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static kitchenpos.common.Fixtures.anEmptyOrderTable;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class TableGroupTest {

    @DisplayName("2개 미만의 주문 테이블을 생성한다")
    @Test
    void testCreateTableGroup_withLessThanTwoTable() {
        // given
        OrderTable orderTable = anEmptyOrderTable().build();

        assertThatIllegalArgumentException().isThrownBy(() -> new TableGroup(Collections.singletonList(orderTable)));
    }
}

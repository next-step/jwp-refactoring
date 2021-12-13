package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단체 지정")
class TableGroupTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> TableGroup.from(Arrays.asList(
                OrderTable.of(Headcount.from(1), TableStatus.EMPTY),
                OrderTable.of(Headcount.from(2), TableStatus.EMPTY)
            )));
    }

    @Test
    @DisplayName("주문 테이블들은 필수")
    void instance_nullOrderTables_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> TableGroup.from(null))
            .withMessage("주문 테이블들은 필수입니다.");
    }

    @Test
    @DisplayName("주문 테이블들은 적어도 2개이상")
    void instance_containNull_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> TableGroup.from(
                Collections.singletonList(OrderTable.of(Headcount.from(2), TableStatus.EMPTY))))
            .withMessageEndingWith("개 이상 이어야 합니다.");
    }
}

package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블들")
class OrderTablesTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> OrderTables.from(
                Collections.singletonList(OrderTable.of(Headcount.from(1), TableStatus.EMPTY))
            ));
    }

    @Test
    @DisplayName("주문 테이블 리스트 필수")
    void instance_nullOrderTables_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> OrderTables.from(null))
            .withMessage("주문 테이블 리스트는 필수입니다.");
    }

    @Test
    @DisplayName("주문 테이블 리스트에 null이 포함될 수 없음")
    void instance_containNull_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> OrderTables.from(Collections.singletonList(null)))
            .withMessageEndingWith("null이 포함될 수 없습니다.");
    }
}

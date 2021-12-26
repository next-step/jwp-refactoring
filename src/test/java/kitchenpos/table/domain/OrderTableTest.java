package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {

    @DisplayName("그룹 아이디가 없고, 테이블이 비어있으면 그룹이 가능하다")
    @Test
    void 그룹_가능_상태() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, true);

        // when
        boolean result = orderTable.isGroupable();

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("그룹 아이디가 있으면 그룹 불가")
    @Test
    void 그룹_불가_상태1() {
        // given
        OrderTable orderTable = new OrderTable(1L, 2L, 4, true);

        // when
        boolean result = orderTable.isGroupable();

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("테이블이 비어있지 않으면 그룹 불가")
    @Test
    void 그룹_불가_상태2() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, false);

        // when
        boolean result = orderTable.isGroupable();

        // then
        assertThat(result).isFalse();
    }
}

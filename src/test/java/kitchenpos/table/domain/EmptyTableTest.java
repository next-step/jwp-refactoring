package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.exception.InvalidArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmptyTableTest {

    @Test
    @DisplayName("정합성 체크")
    void validate() {
        assertThatThrownBy(() -> EmptyTable.valueOf(null))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("테이블의 상태는 필수입니다.");
    }

    @Test
    @DisplayName("생성 테스트")
    void create() {
        assertTrue(EmptyTable.valueOf(true).isEmpty());
        assertFalse(EmptyTable.valueOf(false).isEmpty());
        assertTrue(EmptyTable.valueOf(Boolean.TRUE).isEmpty());
        assertFalse(EmptyTable.valueOf(Boolean.FALSE).isEmpty());
    }
}
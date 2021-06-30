package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

class TableGroupCreateTest {
    @Test
    @DisplayName("orderTableIds 1개 이하이면 IllegalArgumentException이 발생한다")
    void orderTableIds_1개_이하이면_IllegalArgumentException이_발생한다() {
        assertThatIllegalArgumentException().isThrownBy(() -> new TableGroupCreate(Collections.emptyList()));
        assertThatIllegalArgumentException().isThrownBy(() -> new TableGroupCreate(Arrays.asList(1L)));
    }

    @Test
    @DisplayName("orderTableIds 2개 이상이면 정상이다")
    void orderTableIds_2개_이상이면_정상이다() {
        assertDoesNotThrow(
                () -> new TableGroupCreate(Arrays.asList(1L, 2L))
        );
    }
}
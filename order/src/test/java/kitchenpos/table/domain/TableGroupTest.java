package kitchenpos.table.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("단체 테스트")
class TableGroupTest {
    @DisplayName("단체 생성 확인")
    @Test
    void 단체_생성_확인() {
        // when
        ThrowableAssert.ThrowingCallable 생성_확인 = TableGroup::of;

        // then
        assertThatNoException().isThrownBy(생성_확인);
    }
}

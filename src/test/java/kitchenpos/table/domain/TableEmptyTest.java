package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("빈테이블인지 주문테이블인지 여부 관련 Domain 단위 테스트")
class TableEmptyTest {

    @DisplayName("빈테이블인지 주문테이블인지 여부는 null 일 수 없다.")
    @Test
    void assignTableGroup_not_empty() {
        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new TableEmpty(null));
    }

}

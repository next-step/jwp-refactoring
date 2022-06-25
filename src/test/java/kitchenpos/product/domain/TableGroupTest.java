package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("테이블그룹 객체가 같은지 검증")
    void verifyEqualsTableGroup() {
        final TableGroupV2 tableGroup = new TableGroupV2(1L, null, Collections.emptyList());

        assertThat(tableGroup).isEqualTo(new TableGroupV2(1L, null, Collections.emptyList()));
    }
}

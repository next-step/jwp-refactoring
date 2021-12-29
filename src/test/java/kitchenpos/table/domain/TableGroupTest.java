package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupTest {
    
    @DisplayName("단체지정은 최소 두 테이블 이상만 가능하다")
    @Test
    void 단체지정_테이블_확인() {
        // given, when, then
        assertThatThrownBy(() -> {
            TableGroup.from(Arrays.asList(OrderTable.of(3, true)));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("단체지정은 최소 두 테이블 이상만 가능합니다");
    }
    
    @DisplayName("단체지정을 해제한다")
    @Test
    void 단체지정_해제() {
        // given
        OrderTable 첫번째_테이블 = OrderTable.of(3, false);
        OrderTable 두번째_테이블 = OrderTable.of(5, false);
        
        TableGroup 단체지정 = TableGroup.from(Arrays.asList(첫번째_테이블, 두번째_테이블));
        
        // when
        단체지정.ungroup();

        // then
        assertAll(
                () -> assertThat(첫번째_테이블.getTableGroupId()).isNull(),
                () -> assertThat(두번째_테이블.getTableGroupId()).isNull()
        );
    }
}

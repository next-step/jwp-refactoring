package kitchenpos.application.table.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("테이블 테스트")
class OrderTableTest {

    @Test
    @DisplayName("테이블그룹에 포함되지 않은 테이블의 경우 테이블 상태 변경 가능")
    void change_table_empty_status() {
        // given
        OrderTable 테이블_1번 = new OrderTable(6, false);
        OrderTable 테이블_2번 = new OrderTable(9, true);

        // when
        테이블_1번.changeEmptyStatus(true);
        테이블_2번.changeEmptyStatus(false);

        Assertions.assertAll(
            () -> assertThat(테이블_1번.isEmpty()).isTrue(),
            () -> assertThat(테이블_2번.isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("테이블그룹에 포함된 테이블의 경우 테이블 상태 변경 불가")
    void not_change_table_empty_status() {
        OrderTable 테이블_1번 = new OrderTable(6, true);
        OrderTable 테이블_2번 = new OrderTable(9, true);
        TableGroup 테이블_그룹_생성 = new TableGroup(Arrays.asList(테이블_1번, 테이블_2번));

        assertThatThrownBy(() -> 테이블_1번.changeEmptyStatus(false))
            .isInstanceOf(IllegalArgumentException.class);
    }

}

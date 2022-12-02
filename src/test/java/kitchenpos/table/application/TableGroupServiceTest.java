package kitchenpos.table.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TableGroupService")
class TableGroupServiceTest {

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void unGroup_success() {

    }

    @DisplayName("테이블 그룹을 해제한다. / 요리중일 경우 해제할 수 없다.")
    @Test
    void unGroup_fail_cooking() {

    }

    @DisplayName("테이블 그룹을 해제한다. / 식사중일 경우 해제할 수 없다.")
    @Test
    void unGroup_fail_meal() {

    }
}

package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.exception.TableGroupAlreadyExistsException;
import kitchenpos.tablegroup.domain.TableGroup;

@DisplayName("OrderTable 테스트")
class OrderTableTest {

    @Test
    @DisplayName("단체지정으로 묶인 테이블의 비움상태 수정 시도시 오류.")
    void cannot_change_empty() {
        // given
        OrderTable orderTable = new OrderTable(3, false);
        orderTable.setTableGroup(new TableGroup());

        // then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(TableGroupAlreadyExistsException.class)
                .hasMessage("테이블 그룹이 이미 존재합니다.");
    }
}

package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단체 지정 관련 Domain 단위 테스트")
class TableGroupTest {

    @DisplayName("테이블이 2개미만인 or null 경우 단체지정 할 수 없다.")
    @Test
    void checkPossibleGrouping() {
        //given
        TableGroup tableGroup = new TableGroup(null, null);
        tableGroup.addOrderTable(new OrderTable());

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(tableGroup::checkPossibleGrouping);
    }
}

package kitchenpos.orderTable.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static kitchenpos.utils.TestUtils.getRandomId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문그룹 테이블")
class TableGroupTest {

    @Test
    @DisplayName("주문 테이블의 그룹을 생성한다. 그룹은 2개 이상 등록 가능하다.")
    public void initTableGroup() {
        // given
        TableGroup tableGroup = new TableGroup(
            new OrderTable(getRandomId(), 0),
            new OrderTable(getRandomId(), 0)
        );

        // then
        assertThat(tableGroup).isNotNull();
    }

    @Test
    @DisplayName("2개 이상만 주문그룹을 생성할수 있다.")
    public void exceptionInitTableGroup1() {
        assertThatThrownBy(() -> {
            new TableGroup(
                new OrderTable(getRandomId(), 0)
            );
        }).isInstanceOf(IllegalArgumentException.class).hasMessage("주문테이블 최소 갯수는 2개 입니다.");
    }

    @Test
    @DisplayName("주문테이블이 비어있지 않다면, 주문그룹을 생성할수 없다.")
    public void exceptionInitTableGroup2() {
        assertThatThrownBy(() -> {
            new TableGroup(
                new OrderTable(getRandomId(), 1),
                new OrderTable(getRandomId(), 0)
            );
        }).isInstanceOf(IllegalArgumentException.class).hasMessage("테이블이 비어있지 않습니다.");
    }
}
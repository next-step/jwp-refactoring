package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.table.exception.CannotChangeEmptyState;
import kitchenpos.table.exception.CannotChangeNumberOfGuests;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("빈 테이블 생성")
    void 빈_테이블_생성() {
        int numberOfGuests = 0;
        OrderTable orderTable = new OrderTable(numberOfGuests,true);
        assertThat(orderTable.isEmpty()).isTrue();
        assertThat(orderTable.getNumberOfGuests()).isZero();
        assertThat(orderTable.getTableGroup()).isNull();
    }

    @Test
    @DisplayName("비어있지 않은 테이블 생성")
    void 비어있지않은_테이블_생성() {
        int numberOfGuests = 4;
        OrderTable orderTable = new OrderTable(numberOfGuests,false);
        assertThat(orderTable.isEmpty()).isFalse();
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
        assertThat(orderTable.getTableGroup()).isNull();
    }

    @Test
    @DisplayName("빈 테이블로 변경")
    void 빈_테이블로_변경() {
        int numberOfGuests = 4;
        OrderTable orderTable = new OrderTable(numberOfGuests,false);
        orderTable.changeEmpty(true);
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("비어있지않은 테이블로 변경")
    void 비어있지않은_테이블로_변경() {
        int numberOfGuests = 0;
        OrderTable orderTable = new OrderTable(numberOfGuests,true);
        orderTable.changeEmpty(false);
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블그룹에 포함된 테이블의 경우 공석여부 변경 시도시 실패")
    void 테이블_공석상태변경_테이블그룹에_포함된경우() {
        int numberOfGuests = 0;
        OrderTable emptyTable = new OrderTable(numberOfGuests, true);
        OrderTable emptyTable2 = new OrderTable(numberOfGuests, true);
        TableGroup tableGroup = new TableGroup(Lists.newArrayList(emptyTable, emptyTable2));

        assertThatThrownBy(() -> emptyTable.changeEmpty(false))
                .isInstanceOf(CannotChangeEmptyState.class);
    }

    @Test
    @DisplayName("테이블 인원수 변경")
    void 테이블_인원수_변경() {
        int numberOfGuests = 0;
        OrderTable orderTable = new OrderTable(numberOfGuests,false);
        int updatedNumberOfGuests = 3;

        orderTable.changeNumberOfGuests(updatedNumberOfGuests);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(updatedNumberOfGuests);
    }

    @Test
    @DisplayName("인원수가 음수일때 테이블 인원수 변경 실패")
    void 테이블_인원수_변경_음수로_변경시도() {
        int numberOfGuests = 0;
        OrderTable orderTable = new OrderTable(numberOfGuests, false);
        int invalidNumberOfGuests = -5;
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(invalidNumberOfGuests))
                .isInstanceOf(CannotChangeNumberOfGuests.class);
    }

    @Test
    @DisplayName("빈 테이블인 경우 테이블 인원수 변경 실패")
    void 테이블_인원수_변경_빈테이블인_경우() {
        int numberOfGuests = 0;
        OrderTable emptyTable = new OrderTable(numberOfGuests, true);
        int updatedNumberOfGuests = 3;
        assertThatThrownBy(() -> emptyTable.changeNumberOfGuests(updatedNumberOfGuests))
                .isInstanceOf(CannotChangeNumberOfGuests.class);
    }
}

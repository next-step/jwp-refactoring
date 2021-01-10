package kitchenpos.domain.orderTable;

import kitchenpos.domain.orderTable.exceptions.InvalidTryChangeEmptyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {
    @DisplayName("공석 여부, 방문한 손님 수를 주입받아서 오브젝트를 생성할 수 있다.")
    @Test
    void createTest() {
        boolean empty = true;
        int numberOfGuests = 0;

        OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        assertThat(orderTable).isNotNull();
        assertThat(orderTable.isGrouped()).isFalse();
    }

    @DisplayName("그룹을 지정해줄 수 있다.")
    @Test
    void groupTest() {
        boolean empty = true;
        int numberOfGuests = 0;
        Long groupId = 1L;
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        orderTable.group(groupId);

        assertThat(orderTable.isGrouped()).isTrue();
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("그룹을 해제할 수 있다.")
    @Test
    void ungroupTest() {
        boolean empty = true;
        int numberOfGuests = 0;
        Long groupId = 1L;
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);
        orderTable.group(groupId);

        orderTable.ungroup();

        assertThat(orderTable.isGrouped()).isFalse();
    }

    @DisplayName("그룹이 지정되어 있으면 공석 여부를 바꿀 수 없다.")
    @Test
    void changeEmptyFailTest() {
        // given
        OrderTable orderTable = new OrderTable(3, true);
        orderTable.group(3L);

        // when, then
        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(InvalidTryChangeEmptyException.class)
                .hasMessage("단체 지정된 주문 테이블의 비움 상태를 바꿀 수 없습니다.");
    }
}
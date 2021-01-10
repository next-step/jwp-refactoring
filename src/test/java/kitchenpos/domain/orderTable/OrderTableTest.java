package kitchenpos.domain.orderTable;

import kitchenpos.domain.orderTable.exceptions.InvalidTryChangeEmptyException;
import kitchenpos.domain.orderTable.exceptions.InvalidTryChangeGuestsException;
import kitchenpos.domain.orderTable.exceptions.InvalidTryGroupingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.PATH;

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

    @DisplayName("비어 있는데 손님이 있는 오브젝트를 만들 수 없다.")
    @Test
    void createFailTest() {
        boolean empty = true;
        int numberOfGuests = 1;

        assertThatThrownBy(() -> new OrderTable(numberOfGuests, empty))
                .isInstanceOf(InvalidOrderTableException.class)
                .hasMessage("비어 있는 경우 손님수는 0명이어야 한다.");
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
        OrderTable orderTable = new OrderTable(0, true);
        orderTable.group(3L);

        // when, then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(InvalidTryChangeEmptyException.class)
                .hasMessage("단체 지정된 주문 테이블의 비움 상태를 바꿀 수 없습니다.");
    }

    @DisplayName("비어있는 주문 테이블의 방문한 손님수를 바꿀 수 없다.")
    @Test
    void changeNumberOfGuestsFailTest() {
        // given
        OrderTable orderTable = new OrderTable(0, true);

        // when, then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(3))
                .isInstanceOf(InvalidTryChangeGuestsException.class)
                .hasMessage("비어있는 주문 테이블의 방문한 손님 수를 바꿀 수 없습니다.");
    }

    @DisplayName("비어 있지 않은 주문 테이블 단체 지정 시도 시 예외 발생")
    @Test
    void groupingFailWhenNotEmptyTest() {
        // given
        Long tableGroupId = 1L;
        OrderTable orderTable = new OrderTable(3, false);

        // when, then
        assertThatThrownBy(() -> orderTable.group(tableGroupId))
                .isInstanceOf(InvalidTryGroupingException.class)
                .hasMessage("비어 있지 않은 주문 테이블을 단체 지정할 수 없습니다.");
    }

    @DisplayName("이미 단체 지정된 주문 테이블 단체 지정 시도 시 예외 발생")
    @Test
    void groupingFailWhenAlreadyGroupedTest() {
        // given
        Long tableGroupId = 1L;
        OrderTable orderTable = new OrderTable(0, true);
        orderTable.group(tableGroupId);

        // when, then
        assertThatThrownBy(() -> orderTable.group(tableGroupId))
                .isInstanceOf(InvalidTryGroupingException.class)
                .hasMessage("이미 단체 지정된 주문 테이블을 단체 지정할 수 없습니다.");
    }
}
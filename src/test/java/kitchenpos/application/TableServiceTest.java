package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("테이블 서비스 테스트")
class TableServiceTest extends ServiceTest {

    @Test
    @DisplayName("테이블을 등록한다.")
    void create() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(true);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertAll(
                () -> assertThat(savedOrderTable.getId()).isNotNull(),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests()),
                () -> assertThat(savedOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty())
        );
    }

    @Test
    @DisplayName("테이블의 목록을 조회한다.")
    void list() {
        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables.size()).isPositive();
    }

    @Test
    @DisplayName("테이블의 상태를 변경한다.")
    void changeEmpty() {
        // given
        OrderTable savedOrderTable = 테이블_저장(false);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);

        // when
        OrderTable modifiedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), orderTable);

        // then
        assertThat(modifiedOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty());
    }

    @Test
    @DisplayName("존재하지 않는 테이블 ID로 테이블의 상태를 변경하면 예외를 발생한다.")
    void changeEmptyThrowException1() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeEmpty(0L, orderTable));
    }

    @Test
    @DisplayName("테이블 그룹에 등록된 테이블의 상태를 변경하면 예외를 발생한다.")
    void changeEmptyThrowException2() {
        // given
        TableGroup savedTableGroup = 테이블_그룹_저장();
        OrderTable savedOrderTable = 테이블_조회(savedTableGroup.getOrderTables().get(0).getId());

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTable));
    }

    @Test
    @DisplayName("테이블의 방문한 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        // given
        OrderTable savedOrderTable = 테이블_저장(false);

        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);

        // when
        OrderTable modifiedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable);

        // then
        assertThat(modifiedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @Test
    @DisplayName("0명 이하의 손님 수로 테이블의 방문한 손님 수를 변경하면 예외를 발생한다.")
    void changeNumberOfGuestsThrowException1() {
        // given
        OrderTable savedOrderTable = 테이블_저장(false);

        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(-1);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable));
    }

    @Test
    @DisplayName("존재하지 않는 테이블 ID로 테이블의 방문한 손님 수를 변경하면 예외를 발생한다.")
    void changeNumberOfGuestsThrowException2() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeNumberOfGuests(0L, orderTable));
    }

    @Test
    @DisplayName("비어있는 테이블의 방문한 손님 수를 변경하면 예외를 발생한다.")
    void changeNumberOfGuestsThrowException3() {
        // given
        OrderTable savedOrderTable = 테이블_저장(true);

        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable));
    }
}

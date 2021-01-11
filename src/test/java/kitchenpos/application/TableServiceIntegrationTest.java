package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class TableServiceIntegrationTest {

    @Autowired
    private TableService tableService;

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void createTable() {
        // given
        OrderTable orderTable = tableService.create(new OrderTable(4, true));

        // when then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(4);
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("테이블에 상태값을 변경 가능하다.")
    @Test
    void changeStatus() {
        // given
        OrderTable orderTable = tableService.create(new OrderTable(4, true));

        // when
        OrderTable changedTable = tableService.changeEmpty(orderTable.getId(), new OrderTable(false));

        // then
        assertThat(changedTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블에 인원을 변경 가능하다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = tableService.create(new OrderTable(4, false));

        // when
        OrderTable changedTable = tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(5));

        // then
        assertThat(changedTable.getNumberOfGuests()).isEqualTo(5);
    }

    @DisplayName("테이블에 인원은 0명 이상이어야 한다.")
    @Test
    void tableNumberOfGuestsGraterThanZero() {
        // given
        OrderTable orderTable = tableService.create(new OrderTable(4, true));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
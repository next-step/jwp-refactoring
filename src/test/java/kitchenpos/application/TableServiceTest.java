package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-10
 */
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @DisplayName("테이블 비어있는 값으로 변경시 존재하지 않는 테이블의 경우")
    @Test
    void tableChangeEmptyWithNotCompleteStateTest() {
        OrderTable table = new OrderTable();
        assertThatThrownBy(() -> tableService.changeEmpty(100L, table))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 인원수 변경시 0명인 경우")
    @Test
    void tableChangeNumberOfGuestsWithZeroTest() {
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(100L, table))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("테이블 인원 수 변경시 테이블이 비어있는 상태의 경우")
    @Test
    void tableChangeNumberOfGuestsWithEmptyTable() {
        OrderTable table = new OrderTable();
        table.setId(1L);
        table.setEmpty(true);
        tableService.changeEmpty(1L, table);

        table.setNumberOfGuests(0);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, table))
                .isInstanceOf(IllegalArgumentException.class);

    }
}

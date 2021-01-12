package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.application.creator.OrderTableHelper;
import kitchenpos.domain.OrderTable;
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

    @DisplayName("테이블 생성 테스트")
    @Test
    void tableCreateTest() {
        OrderTable savedOrderTable = tableService.create(OrderTableHelper.create(false));

        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(0);
    }

    @DisplayName("테이블 비어있는 값으로 변경시 존재하지 않는 테이블의 경우")
    @Test
    void tableChangeEmptyWithNotCompleteStateTest() {
        OrderTable orderTableForChangeEmpty = new OrderTable();
        orderTableForChangeEmpty.setEmpty(false);

        assertThatThrownBy(() -> tableService.changeEmpty(999L, orderTableForChangeEmpty))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 인원수 변경시 0명 아래인 경우")
    @Test
    void tableChangeNumberOfGuestsWithZeroTest() {
        OrderTable savedOrderTable = tableService.create(OrderTableHelper.create(false));
        OrderTable orderTableForGuestNumberChange = new OrderTable();
        orderTableForGuestNumberChange.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableForGuestNumberChange))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("테이블 인원 수 변경시 테이블이 비어있는 상태의 경우")
    @Test
    void tableChangeNumberOfGuestsWithEmptyTable() {
        OrderTable savedOrderTable = tableService.create(OrderTableHelper.create(true));
        OrderTable orderTableForGuestNumberChange = new OrderTable();
        orderTableForGuestNumberChange.setNumberOfGuests(0);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTableForGuestNumberChange))
                .isInstanceOf(IllegalArgumentException.class);

    }
}

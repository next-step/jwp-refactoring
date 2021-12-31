package table;

import kitchenpos.AcceptanceTest;
import kitchenpos.global.exception.EntityNotFoundException;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.TableChangeEmptyRequest;
import kitchenpos.table.dto.TableChangeNumberOfGuestRequest;
import kitchenpos.table.exception.CannotChangeTableEmptyException;
import kitchenpos.table.exception.TableNotAvailableException;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("테이블 관련 기능")
class TableServiceTest extends AcceptanceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    @DisplayName("사용여부를 변경하고자 하는 테이블이 존재하지 않으면 예외가 발생한다.")
    void isNotExistTable() {
        // when
        assertThatThrownBy(() -> {
            tableService.changeEmpty(1L, new TableChangeEmptyRequest());
        }).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("방문한 손님 수를 변경하고자 하는 테이블이 존재하지 않으면 예외가 발생한다.")
    void changeNumberOfGuestsIsNotExistTable() {
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(1L, new TableChangeNumberOfGuestRequest(0));
        }).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("방문한 손님 수를 변경하고자 하는 테이블이 사용불가할 경우 예외가 발생한다.")
    void changeNumberOfGuestsIsEmptyTable() {
        // given
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        // when
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(1L, new TableChangeNumberOfGuestRequest(0));
        }).isInstanceOf(TableNotAvailableException.class);
    }

    @Test
    @DisplayName("사용여부를 변경하고자 하는 테이블이 단체 지정 되어 있다면 예외가 발생한다.")
    void isTableGroup() {
        // given
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup.getId(), 0, false));

        // when
        assertThatThrownBy(() -> {
            tableService.changeEmpty(orderTable.getId(), new TableChangeEmptyRequest(true));
        }).isInstanceOf(CannotChangeTableEmptyException.class);
    }
}

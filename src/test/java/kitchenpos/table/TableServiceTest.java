package kitchenpos.table;

import kitchenpos.AcceptanceTest;
import kitchenpos.application.*;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableChangeEmptyRequest;
import kitchenpos.dto.TableChangeNumberOfGuestRequest;
import kitchenpos.exception.TableNotAvailableException;
import kitchenpos.global.exception.EntityNotFoundException;
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
        final OrderTable savedOrderTable = orderTableRepository.save(OrderTable.builder().empty(true).build());

        // when
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(1L, new TableChangeNumberOfGuestRequest(0));
        }).isInstanceOf(TableNotAvailableException.class);
    }

}

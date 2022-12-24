package kitchenpos.table.application;

import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.fixture.OrderTableFixture;
import kitchenpos.table.domain.fixture.TableGroupFixture;
import kitchenpos.table.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.validator.TableValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static kitchenpos.table.application.TableService.CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.OrderTable.TABLE_GROUP_NOT_NULL_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.fixture.NumberOfGuestsFixture.initNumberOfGuests;
import static kitchenpos.table.validator.TableValidator.ORDER_STATUS_NOT_COMPLETION_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@DisplayName("테이블 서비스")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @Mock
    private TableValidator tableValidator;

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create() {

        given(orderTableRepository.save(any())).willReturn(new OrderTable(null, new NumberOfGuests(0), false));

        OrderTableResponse orderTable = tableService.create(new OrderTable(null, initNumberOfGuests(), false));

        assertAll(
                () -> assertThat(orderTable.getTableGroup()).isNull(),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(new NumberOfGuests(0)),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("손님수를 변경한다.")
    @Test
    void changeNumberOfGuests_success() {

        given(orderTableRepository.findById(1L)).willReturn(Optional.of(OrderTableFixture.orderTableA(null, false)));
        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(1);

        assertThat(tableService.changeNumberOfGuests(1L, changeNumberOfGuestsRequest).getNumberOfGuests()).isEqualTo(new NumberOfGuests(1));
    }

    @DisplayName("손님수를 변경한다. / 0명보다 작을 수 없다.")
    @Test
    void changeNumberOfGuests_fail_minimum() {

        given(orderTableRepository.findById(1L)).willReturn(Optional.of(OrderTableFixture.orderTableA(null, false)));
        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, changeNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER_EXCEPTION_MESSAGE);
    }

    @DisplayName("손님수를 변경한다. / 주문테이블이 없을 수 없다.")
    @Test
    void changeNumberOfGuests_fail_notExistOrderTable() {

        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(100L, changeNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님수를 변경한다. / 테이블이 공석 상태면 손님수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuest_fail_notEmptyTable() {

        given(orderTableRepository.findById(1L)).willReturn(Optional.of(new OrderTable(null, new NumberOfGuests(0), true)));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new ChangeNumberOfGuestsRequest(1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("공석 상태로 변경한다.")
    @Test
    void empty_success() {
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(OrderTableFixture.orderTableA(null, false)));
        assertThat(tableService.changeEmpty(1L).isEmpty()).isTrue();
    }

    @DisplayName("공석 상태로 변경한다. / 테이블 그룹이 있을 수 없다.")
    @Test
    void changeEmpty_fail_notTableGroup() {

        given(orderTableRepository.findById(1L)).willReturn(Optional.of(new OrderTable(TableGroupFixture.tableGroupA(), new NumberOfGuests(0), true)));

        assertThatThrownBy(() -> tableService.changeEmpty(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(TABLE_GROUP_NOT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("공석 상태로 변경한다. / 요리중일 경우 변경할 수 없다.")
    @Test
    void empty_fail_cooking() {

        given(orderTableRepository.findById(1L)).willReturn(Optional.of(OrderTableFixture.orderTableA(null, false)));

        doThrow(new IllegalArgumentException(ORDER_STATUS_NOT_COMPLETION_EXCEPTION_MESSAGE))
                .when(tableValidator).validateNotComplete(any());

        assertThatThrownBy(() -> tableService.changeEmpty(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_NOT_COMPLETION_EXCEPTION_MESSAGE);
    }

    @DisplayName("공석 상태로 변경한다. / 식사중일 경우 변경할 수 없다.")
    @Test
    void empty_fail_meal() {

        given(orderTableRepository.findById(1L)).willReturn(Optional.of(OrderTableFixture.orderTableA(null, false)));

        doThrow(new IllegalArgumentException(ORDER_STATUS_NOT_COMPLETION_EXCEPTION_MESSAGE))
                .when(tableValidator).validateNotComplete(any());

        assertThatThrownBy(() -> tableService.changeEmpty(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_NOT_COMPLETION_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문 테이블을 조회한다.")
    @Test
    void list() {

        given(orderTableRepository.findAll()).willReturn(Arrays.asList(OrderTableFixture.orderTableA(null, true), OrderTableFixture.orderTableA(null, true)));

        assertThat(tableService.list()).hasSize(2);
    }
}

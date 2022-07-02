package kitchenpos.service.table;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.service.table.application.TableService;
import kitchenpos.service.table.application.TableValidator;
import kitchenpos.service.table.dto.OrderTableRequest;
import kitchenpos.service.table.dto.OrderTableResponse;
import kitchenpos.service.table.dto.OrderTableUpdateEmptyRequest;
import kitchenpos.service.table.dto.OrderTableUpdateNumberOfGuestsRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private TableValidator tableValidator;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("주문 테이블을 생성할 수 있다.")
    void create() {
        //then
        assertThat(tableService.create(new OrderTableRequest(null, 5, true)).getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    @DisplayName("전체 주문 테이블을 조회할 수 있다.")
    void list() {
        //given
        OrderTable orderTable1 = OrderTable.of(2, true);
        OrderTable orderTable2 = OrderTable.of(10, true);
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(orderTable1, orderTable2));

        //then
        assertThat(tableService.list().stream().map(OrderTableResponse::getNumberOfGuests)).containsExactlyInAnyOrder(2, 10);
    }

    @Test
    @DisplayName("주문 테이블을 빈 테이블로 변경할 수 있다.")
    void changeEmpty() {
        //given
        OrderTable orderTable = OrderTable.of(2, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        //when
        OrderTableResponse updatedOrderTable =
                tableService.changeEmpty(orderTable.getId(), new OrderTableUpdateEmptyRequest(true));

        //then
        assertThat(updatedOrderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블이 조회되지 않으면 빈 테이블로 변경 실패한다.")
    void changeEmpty_failed_1() {
        //given
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(
                () -> tableService.changeEmpty(0L, new OrderTableUpdateEmptyRequest(true))).isExactlyInstanceOf(
                NoSuchElementException.class);
    }

    @Test
    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    void changeNumberOfGuests() {
        //given
        OrderTable orderTable = OrderTable.of(5, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        //when
        OrderTableResponse updatedOrderTable =
                tableService.changeNumberOfGuests(0L, new OrderTableUpdateNumberOfGuestsRequest(0));

        //then
        assertThat(updatedOrderTable.getNumberOfGuests()).isZero();
    }

    @Test
    @DisplayName("주문 테이블이 조회가 안 되면 방문한 손님 수 변경에 실패한다.")
    void changeNumberOfGuests_failed_2() {
        //given
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L,
                new OrderTableUpdateNumberOfGuestsRequest(10))).isExactlyInstanceOf(NoSuchElementException.class);

    }
}

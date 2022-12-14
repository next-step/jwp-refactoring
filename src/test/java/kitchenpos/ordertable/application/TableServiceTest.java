package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTableValidator;
import kitchenpos.ordertable.dto.TableRequest;
import kitchenpos.ordertable.dto.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderTableValidator orderTableValidator;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void create() {
        OrderTable expected = new OrderTable(0, true);
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(expected);

        TableResponse actual = tableService.create(new TableRequest(expected.getNumberOfGuests(), expected.isEmpty()));

        assertAll(
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(0),
                () -> assertThat(actual.isEmpty()).isTrue(),
                () -> assertThat(actual.getTableGroupId()).isNull()
        );
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void list() {
        OrderTable tableA = new OrderTable(0, true);
        OrderTable tableB = new OrderTable(5, false);
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(tableA, tableB));

        List<TableResponse> actual = tableService.list();

        assertThat(actual).hasSize(2);
    }

    @DisplayName("주문 테이블 이용 여부를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable tableA = new OrderTable(0, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(tableA));

        TableResponse actual = tableService.changeEmpty(1L, true);

        verify(orderTableValidator).validateOrderStatus(any());
        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("단체 테이블로 지정되어 있다면 주문 테이블 이용 여부를 변경할 수 없다.")
    @Test
    void changeEmptyWithTableGroup() {
        OrderTable tableA = new OrderTable(5, false);
        tableA.groupBy(1L);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(tableA));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 테이블로 지정되어 있습니다.");
    }

    @DisplayName("주문 상태가 조리 또는 식사중 이라면 주문 테이블 이용 여부를 변경할 수 없다.")
    @Test
    void changeEmptyWithOrderStatus() {
        OrderTable tableA = new OrderTable(5, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(tableA));
        doThrow(new IllegalArgumentException("완료되지 않은 주문이 존재합니다.")).when(orderTableValidator)
                .validateOrderStatus(any());

        assertThatThrownBy(() -> tableService.changeEmpty(1L, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료되지 않은 주문이 존재합니다.");
    }

    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable tableA = new OrderTable(5, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(tableA));

        TableResponse actual = tableService.changeNumberOfGuests(1L, 3);

        assertThat(actual.getNumberOfGuests()).isEqualTo(3);
    }

    @DisplayName("주문 테이블의 손님 수를 음수로 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsWithNegative() {
        OrderTable tableA = new OrderTable(5, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(tableA));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 음수 일 수 없습니다.");
    }

    @DisplayName("주문 테이블이 이용중이 아니라면 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsWithNotEmptyTable() {
        OrderTable tableA = new OrderTable(0, true);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(tableA));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용중이지 않은 테이블은 손님 수를 변경할 수 없습니다.");
    }
}

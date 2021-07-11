package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.EmptyException;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void create() {
        OrderTable orderTable = new OrderTable(2, true);
        OrderTableRequest orderTableRequest = new OrderTableRequest(1, true);
        given(orderTableRepository.save(any())).willReturn(orderTable);

        OrderTableResponse created = tableService.create(orderTableRequest);

        assertAll(
                () -> assertThat(created.getId()).isNotNull(),
                () -> assertThat(created.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests()),
                () -> assertThat(created.isEmpty()).isEqualTo(orderTable.isEmpty()));

        verify(orderTableRepository, times(1)).save(any());
    }

    @DisplayName("주문 테이블 리스트를 조회한다.")
    @Test
    void list() {
        OrderTable orderTable1 = new OrderTable(2, true);
        OrderTable orderTable2 = new OrderTable( 4, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        given(orderTableRepository.findAll()).willReturn(orderTables);

        List<OrderTableResponse> findedTables = tableService.list();

        assertThat(findedTables)
                .containsExactly(OrderTableResponse.from(orderTable1), OrderTableResponse.from(orderTable2));

        verify(orderTableRepository, times(1)).findAll();
    }

    @DisplayName("주문 테이블 empty 값을 변경한다.")
    @ParameterizedTest
    @CsvSource(value = {"false:true", "true:false"}, delimiter = ':')
    void changeEmpty(boolean preEmptyValue, boolean afterEmptyValue) {
        OrderTable orderTable = new OrderTable(2, preEmptyValue);
        OrderTable expectedTable = new OrderTable(2, afterEmptyValue);
        OrderTableRequest changeTable = new OrderTableRequest(2, afterEmptyValue);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderTableRepository.save(any())).willReturn(orderTable);

        OrderTableResponse changed = tableService.changeEmpty(orderTable.getId(), changeTable);

        assertThat(changed.isEmpty()).isEqualTo(expectedTable.isEmpty());

        verify(orderTableRepository, times(1)).findById(anyLong());
        verify(orderTableRepository, times(1)).save(any());
    }

    @DisplayName("주문 테이블 empty 값을 변경 실패한다. - 변경하려는 주문 테이블이 기존에 등록되지 않은 테이블이면 변경 실패")
    @Test
    void fail_changeEmpty1() {
        OrderTable orderTable = new OrderTable(1L, null, 2, true);
        OrderTableRequest changeTable = new OrderTableRequest(2, false);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 empty 값을 변경 실패한다. - 변경하려는 주문 테이블이 테이블 그룹으로 지정되어 있으면 변경 실패")
    @Test
    void fail_changeEmpty2() {
        TableGroup tableGroup = new TableGroup(1L, new OrderTables(new ArrayList<>()));
        OrderTable orderTable = new OrderTable(1L, tableGroup, 2, true);
        OrderTableRequest changeTable = new OrderTableRequest(2, false);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeTable))
                .isInstanceOf(IllegalArgumentException.class);

        verify(orderTableRepository, times(1)).findById(anyLong());
    }

    @DisplayName("주문 테이블 empty 값을 변경 실패한다. - 변경하려는 주문 테이블의 상태가 조리 또는 식사 중일 경우 변경 실패")
    @Test
    void fail_changeEmpty3() {
        OrderTable orderTable = new OrderTable(2, true);
        OrderTableRequest changeTable = new OrderTableRequest(2, false);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeTable))
                .isInstanceOf(IllegalArgumentException.class);

        verify(orderTableRepository, times(1)).findById(anyLong());
        verify(orderRepository, times(1)).existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList());
    }

    @DisplayName("주문 테이블 guests(손님) 숫자값을 변경한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(2, false);
        OrderTable expectedTable = new OrderTable(4, false);
        OrderTableRequest changeTable = new OrderTableRequest(4, false);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderTableRepository.save(any())).willReturn(orderTable);

        OrderTableResponse changed = tableService.changeNumberOfGuests(orderTable.getId(), changeTable);

        assertThat(changed.getNumberOfGuests()).isEqualTo(expectedTable.getNumberOfGuests());

        verify(orderTableRepository, times(1)).findById(anyLong());
        verify(orderTableRepository, times(1)).save(any());
    }

    @DisplayName("주문 테이블 guests 숫자값 변경 실패한다 - 변경하려는 guests 숫자가 0보다 작을 경우 0보다 작을 경우 변경 실패")
    @Test
    void fail_changeNumberOfGuests1() {
        OrderTable orderTable = new OrderTable( 4, false);
        OrderTableRequest changeTable = new OrderTableRequest(-1, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 guests 숫자값 변경 실패한다 - 변경하려는 주문 테이블이 기존에 등록되지 않은 테이블이면 변경 실패")
    @Test
    void fail_changeNumberOfGuests2() {
        OrderTable orderTable = new OrderTable(2, false);
        OrderTableRequest changeTable = new OrderTableRequest(4, false);
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeTable))
                .isInstanceOf(IllegalArgumentException.class);

        verify(orderTableRepository, times(1)).findById(orderTable.getId());
    }

    @DisplayName("주문 테이블 guests 숫자값 변경 실패한다 - 변경하려는 주문 테이블의 empty 값이 true이면 변경 실패")
    @Test
    void fail_changeNumberOfGuests3() {
        OrderTable orderTable = new OrderTable(2, true);
        OrderTableRequest changeTable = new OrderTableRequest(4, false);
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeTable))
                .isInstanceOf(EmptyException.class);

        verify(orderTableRepository, times(1)).findById(orderTable.getId());
    }
}

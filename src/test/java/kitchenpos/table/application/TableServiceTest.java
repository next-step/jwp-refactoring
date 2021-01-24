package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderTableRepository orderTableRepository;
    @InjectMocks
    TableService tableService;

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void createTableTest() {
        OrderTable expected = new OrderTable(null, 3, false);
        given(orderTableRepository.save(any())).willReturn(expected);

        OrderTable saved = tableService.create(expected);

        assertThat(saved).isEqualTo(expected);
    }

    @DisplayName("주문 테이블의 목록을 조회할 수 있다.")
    @Test
    void tableListTest() {
        OrderTable table1 = new OrderTable(null, 3, false);
        OrderTable table2 = new OrderTable(null, 4, false);
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(table1, table2));

        List<OrderTableResponse> results = tableService.list();

        assertThat(results).contains(OrderTableResponse.of(table1), OrderTableResponse.of(table2));
    }

    @DisplayName("빈 테이블 설정")
    @Test
    void setTableEmptyTest() {
        OrderTable expected = new OrderTable(null, 3, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(expected));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);

        OrderTableResponse saved = tableService.changeEmpty(1L, true);

        assertThat(saved.isEmpty()).isTrue();
    }

    @DisplayName("빈 테이블 해지")
    @Test
    void setTableNotEmptyTest() {
        OrderTable expected = new OrderTable(null, 3, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(expected));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);

        OrderTableResponse saved = tableService.changeEmpty(1L, false);

        assertThat(saved.isEmpty()).isFalse();
    }

    @DisplayName("단체 지정된 주문 테이블은 빈 테이블 설정/해지할 수 없다.")
    @Test
    void groupTableCantSetEmptyTest() {
        OrderTable expected = new OrderTable(new TableGroup(1L), 3, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(expected));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 조리/식사인 테이블은 빈 테이블 설정/해지할 수 없다.")
    @Test
    void cookingCantSetEmptyTest() {
        OrderTable expected = new OrderTable(new TableGroup(1L), 3, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(expected));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수를 입력")
    @Test
    void setCustomerCountTest() {
        OrderTableRequest request = new OrderTableRequest(3, false);
        OrderTable expected = new OrderTable(new TableGroup(1L), 3, false);

        given(orderTableRepository.findById(any())).willReturn(Optional.of(expected));
        given(orderTableRepository.save(any())).willReturn(expected);

        OrderTableResponse saved = tableService.changeNumberOfGuests(1L, request);

        assertThat(saved.getNumberOfGuests()).isEqualTo(3);

    }

    @DisplayName("방문한 손님 수가 0명 이하이면 손님수를 입력할 수 없다.")
    @Test
    void underZeroCantSetCustomerCountTest() {
        OrderTableRequest request = new OrderTableRequest(-3, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블은 방문한 손님 수를 입력할 수 없다.")
    @Test
    void emptyTableCSetCustomerCountTest() {
        OrderTable expected = new OrderTable(new TableGroup(1L), 3, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(expected));
        OrderTableRequest request = new OrderTableRequest(3, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
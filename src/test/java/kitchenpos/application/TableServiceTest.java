package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    TableService tableService;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    OrderTable 주문테이블;

    @Test
    @DisplayName("주문테이블을 생성한다(Happy Path)")
    void create() {
        주문테이블 = new OrderTable(1L, 2, true);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(주문테이블);

        OrderTable savedOrderTableGroup = tableService.create(주문테이블);

        assertThat(savedOrderTableGroup).isNotNull()
                .satisfies(orderTable -> {
                            orderTable.getId().equals(주문테이블.getId());
                            Objects.isNull(orderTable.getTableGroupId());
                            String.valueOf(orderTable.getNumberOfGuests()).equals(String.valueOf(주문테이블.getNumberOfGuests()));
                        }
                );
    }

    @Test
    @DisplayName("주문테이블 리스트를 조회한다.")
    void list() {
        주문테이블 = new OrderTable(1L, 2, true);
        given(orderTableDao.findAll()).willReturn(Arrays.asList(주문테이블));

        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).containsExactlyInAnyOrderElementsOf(Arrays.asList(주문테이블));
    }

    @Test
    @DisplayName("테이블 비어있는지 여부 변경 (Happy Path)")
    void changeEmpty() {
        주문테이블 = new OrderTable(1L, null, 2, true);
        OrderTable 변경테이블 = new OrderTable(1L, null, 2, false);
        OrderTable 변경후주문테이블 = new OrderTable(1L, null, 2, false);

        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        given(orderTableDao.save(주문테이블)).willReturn(변경후주문테이블);

        OrderTable changedOrderTable = tableService.changeEmpty(주문테이블.getId(), 변경테이블);

        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("유효하지 않은 테이블일 경우 테이블 비어있는지 여부 변경 불가")
    void changeEmptyInvalidTableId() {
        주문테이블 = new OrderTable(1L, null, 2, true);
        OrderTable 변경테이블 = new OrderTable(1L, null, 2, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문테이블.getId(), 변경테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 테이블 그룹에 포함되어 있는 경우 테이블 비어있는지 여부 변경 불가")
    void changeEmptyInvalidTableGroup() {
        주문테이블 = new OrderTable(1L, 1L, 2, true);
        OrderTable 변경테이블 = new OrderTable(1L, null, 2, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문테이블));

        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문테이블.getId(), 변경테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 주문상태가 조리중/식사중일 경우 테이블 비어있는지 여부 변경 불가")
    void changeEmptyInvalidOrderStatus() {
        주문테이블 = new OrderTable(1L, null, 2, true);
        OrderTable 변경테이블 = new OrderTable(1L, null, 2, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);

        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문테이블.getId(), 변경테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 손님 숫자를 변경한다 (Happy Path)")
    void changeNumberOfGuests() {
        주문테이블 = new OrderTable(1L, null, 2, false);
        OrderTable 변경테이블 = new OrderTable(1L, null, 5, false);
        OrderTable 변경후주문테이블 = new OrderTable(1L, null, 5, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문테이블));
        given(orderTableDao.save(주문테이블)).willReturn(변경후주문테이블);

        OrderTable changedOrderTable = tableService.changeNumberOfGuests(주문테이블.getId(), 변경테이블);

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(변경테이블.getNumberOfGuests());
    }

    @Test
    @DisplayName("변경하려는 손님 숫자가 유효하지 않은 경우 테이블의 손님 숫자 변경 불가")
    void changeNumberOfGuestsInvalidNumbers() {
        주문테이블 = new OrderTable(1L, null, 2, false);
        OrderTable 변경테이블 = new OrderTable(1L, null, -1, false);

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문테이블.getId(), 변경테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("변경하려는 테이블이 유효하지 않은 경우 테이블의 손님 숫자 변경 불가")
    void changeNumberOfGuestsInvalidOrderTable() {
        주문테이블 = new OrderTable(1L, null, 2, false);
        OrderTable 변경테이블 = new OrderTable(1L, null, 5, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문테이블.getId(), 변경테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("변경하려는 테이블이 비어있는 경우 테이블의 손님 숫자 변경 불가")
    void changeNumberOfGuestsInvalidOrderTableEmpty() {
        주문테이블 = new OrderTable(1L, null, 2, true);
        OrderTable 변경테이블 = new OrderTable(1L, null, 5, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문테이블));

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문테이블.getId(), 변경테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
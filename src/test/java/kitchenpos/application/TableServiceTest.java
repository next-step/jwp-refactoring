package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static kitchenpos.utils.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void createTable() {
        given(orderTableDao.save(any())).willReturn(orderTable_groupId_추가(empty_orderTable1, null, true));

        OrderTable result = tableService.create(empty_orderTable1);

        assertThat(result.getTableGroupId()).isNull();
    }

    @DisplayName("빈 테이블로 변경할 수 있다.")
    @Test
    void changeEmpty() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(not_empty_orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(empty_orderTable1);

        OrderTable result = tableService.changeEmpty(not_empty_orderTable.getId(), not_empty_orderTable);

        assertThat(result).isEqualTo(empty_orderTable1);
    }

    @DisplayName("주문 테이블이 등록되어 있지 않으면 변경할 수 없다.")
    @Test
    void changeEmptyException1() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(not_empty_orderTable.getId(), not_empty_orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 단체 지정일 경우 변경할 수 없다.")
    @Test
    void changeEmptyException2() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable1));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), not_empty_orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문들 중에서 주문 상태가 조리 또는 식사가 하나라도 있을 경우 변경할 수 없다.")
    @Test
    void changeEmptyException3() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(not_empty_orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(not_empty_orderTable.getId(), not_empty_orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable newOrderTable = orderTable_numberOfGuests_추가(orderTable1, 3);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable1));
        given(orderTableDao.save(any())).willReturn(newOrderTable);

        OrderTable result = tableService.changeNumberOfGuests(orderTable1.getId(), newOrderTable);

        assertThat(result.getNumberOfGuests()).isEqualTo(3);
    }

    @DisplayName("방문한 손님 수가 0보다 작으면 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException1() {
        OrderTable newOrderTable = orderTable_numberOfGuests_추가(orderTable1, -1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(newOrderTable.getId(), newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 등록되어 있지 않으면 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException2() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable1.getId(), orderTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블일 경우 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException3() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(empty_orderTable1));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(empty_orderTable1.getId(), empty_orderTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

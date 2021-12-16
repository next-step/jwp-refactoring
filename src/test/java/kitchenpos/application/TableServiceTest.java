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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.domain.OrderTableTest.빈자리;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("테이블 관리 테스트")
public class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderTable orderTable;

    @Test
    @DisplayName("빈 테이블 등록")
    void createEmptyTable() {
        // given
        given(orderTableDao.save(any())).willReturn(빈자리);
        // when
        OrderTable actual = tableService.create(빈자리);
        // then
        verify(orderTableDao, only()).save(any());
        assertAll(
                () -> assertThat(actual.getTableGroupId()).isNull(),
                () -> assertThat(actual.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("주문 테이블 리스트 조회")
    void orderTableList() {
        // given
        given(orderTableDao.findAll()).willReturn(Collections.singletonList(빈자리));
        // when
        List<OrderTable> actual = tableService.list();
        // then
        verify(orderTableDao, only()).findAll();
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual).containsExactly(빈자리)
        );
    }

    @Test
    @DisplayName("빈 테이블로 변경")
    void changeEmpty() {
        // given
        given(orderTable.isEmpty()).willReturn(true);

        OrderTable savedOrderTable = mock(OrderTable.class);
        given(savedOrderTable.getTableGroupId()).willReturn(null);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(savedOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(orderTable);
        // when
        OrderTable actual = tableService.changeEmpty(1L, orderTable);
        // then
        assertAll(
                () -> assertThat(actual).isEqualTo(orderTable),
                () -> assertThat(actual.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("등록되지 않은 주문 테이블은 변경 할 수 없다.")
    void notFoundOrderTableToChangeEmpty() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹이 지정된 주문 테이블은 변경 할 수 없다.")
    void nonNullTableGroupId() {
        // given
        OrderTable savedOrderTable = mock(OrderTable.class);
        given(savedOrderTable.getTableGroupId()).willReturn(1L);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(savedOrderTable));
        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
        verify(savedOrderTable, only()).getTableGroupId();
        verify(orderDao, never()).existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList());
    }

    @Test
    @DisplayName("주문 테이블이 존재하고, 주문 상태가 `조리(COOKING)`, `식사(MEAL)`상태이면 변경 할 수 없다.")
    void existsByOrderTableIdAndOrderStatusIn() {
        // given
        OrderTable savedOrderTable = mock(OrderTable.class);
        given(savedOrderTable.getTableGroupId()).willReturn(null);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(savedOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);
        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
        verify(orderDao, only()).existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList());
    }

    @Test
    @DisplayName("방문한 인원 변경")
    void changeNumberOfGuests() {
        // given
        given(orderTable.getNumberOfGuests()).willReturn(2);
        given(orderTable.isEmpty()).willReturn(false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(any())).willReturn(orderTable);
        // when
        OrderTable actual = tableService.changeNumberOfGuests(1L, this.orderTable);
        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(2);
        verify(orderTable, atMostOnce()).setNumberOfGuests(anyInt());
    }

    @Test
    @DisplayName("방문한 인원은 0명 이상이어야 한다.")
    void guestsIsOverZero() {
        // given
        given(orderTable.getNumberOfGuests()).willReturn(-1);
        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, this.orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록되지 않은 주문 테이블은 변경 할 수 없다.")
    void notFoundOrderTableToChangeNumberOfGuests() {
        // given
        given(orderTable.getNumberOfGuests()).willReturn(2);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, this.orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블의 인원은 변경 할 수 없다.")
    void notChangeEmptyTable() {
        // given
        given(orderTable.getNumberOfGuests()).willReturn(2);
        given(orderTable.isEmpty()).willReturn(true);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, this.orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("테이블 생성")
    @Test
    void create() {
        // given
        OrderTable expected = new OrderTable(1L, 1L, 4, false);
        Mockito.when(orderTableDao.save(Mockito.any()))
            .thenReturn(expected);

        OrderTable orderTable = new OrderTable(1L, 4);

        // when
        OrderTable actual = tableService.create(orderTable);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("테이블 목록 조회")
    @Test
    void list() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 4);
        OrderTable orderTable2 = new OrderTable(2L, 2);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        Mockito.when(orderTableDao.findAll())
            .thenReturn(orderTables);

        // when
        List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual).isEqualTo(orderTables);
    }

    @DisplayName("테이블 빈 테이블 여부 변경")
    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, false);

        Mockito.when(orderTableDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(orderTable));
        Mockito.when(orderDao.existsByOrderTableIdAndOrderStatusIn(Mockito.anyLong(), Mockito.anyList()))
            .thenReturn(false);

        OrderTable expected = new OrderTable(1L, 1L, 4, true);
        Mockito.when(orderTableDao.save(Mockito.any()))
            .thenReturn(expected);

        OrderTable request = new OrderTable();
        request.setEmpty(true);

        // when
        OrderTable actual = tableService.changeEmpty(1L, request);

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("테이블 그룹이 있을 시 변경 불가능")
    @Test
    void changeEmptyFailWhenTableGroupExists() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1L, 4, false);

        Mockito.when(orderTableDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(orderTable));

        OrderTable request = new OrderTable();
        request.setEmpty(true);

        // when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableService.changeEmpty(1L, request));
    }

    @DisplayName("요리 중이나 식사 중일 때 변경 불가능")
    @Test
    void changeEmptyFailWhenCookingOrMeal() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, false);

        Mockito.when(orderTableDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(orderTable));
        Mockito.when(orderDao.existsByOrderTableIdAndOrderStatusIn(Mockito.anyLong(), Mockito.anyList()))
            .thenReturn(true);

        OrderTable request = new OrderTable();
        request.setEmpty(true);

        // when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableService.changeEmpty(1L, request));
    }

    @DisplayName("고객 수 변경")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 2, false);
        Mockito.when(orderTableDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(orderTable));

        OrderTable expected = new OrderTable(1L, 1L, 4, false);
        Mockito.when(orderTableDao.save(Mockito.any()))
            .thenReturn(expected);

        OrderTable request = new OrderTable();
        request.setNumberOfGuests(4);

        // when
        OrderTable actual = tableService.changeNumberOfGuests(1L, request);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(4);
    }

    @DisplayName("인원수 0 미만시 변경 불가능")
    @Test
    void modifyNumberOfGuestFailWhenLessThanZero() {
        // given
        OrderTable request = new OrderTable();
        request.setNumberOfGuests(-1);

        // when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableService.changeNumberOfGuests(1L, request));
    }

    @DisplayName("주문 테이블이 비어있을 시 변경 불가능")
    @Test
    void modifyNumberOfGuestFailWhenTableNotExists() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 2, true);
        Mockito.when(orderTableDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(orderTable));

        OrderTable request = new OrderTable();
        request.setNumberOfGuests(4);

        // when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableService.changeNumberOfGuests(1L, request));
    }
}
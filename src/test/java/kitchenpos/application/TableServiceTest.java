package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Test
    void 테이블_등록() {
        //given
        OrderTable orderTable = new OrderTable(1L, null, 3, true);
        given(orderTableDao.save(any())).willReturn(orderTable);

        //when
        OrderTable savedTable = tableService.create(orderTable);

        //then
        assertThat(savedTable.getId()).isNotNull();
    }

    @Test
    void 테이블_상태를_빈_테이블_상태로_변경() {
        //given
        OrderTable orderTable = new OrderTable(1L, null, 3, false);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
        OrderTable changedTable = new OrderTable(1L, null, 3, true);
        given(orderTableDao.save(any())).willReturn(changedTable);

        //when
        OrderTable savedOrderTable = tableService.changeEmpty(1L, changedTable);

        //then
        assertThat(savedOrderTable.isEmpty()).isTrue();
    }

    @Test
    void 존재하지_않는_테이블일_경우_빈_테이블로_변경_실패() {
        //given
        OrderTable orderTable = new OrderTable(999L, null, 1, false);
        given(orderTableDao.findById(999L)).willReturn(Optional.empty());

        //when+then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(999L, orderTable));
    }

    @Test
    void 테이블_그룹이_등록되어_있다면_빈_테이블로_변경_실패() {
        //given
        OrderTable orderTable = new OrderTable(1L, 1L, 3, false);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
        //when+then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(1L, orderTable));
    }

    @Test
    void 주문의_상태가_계산완료가_아닌_경우_빈_테이블로_변경_실패() {
        //given
        OrderTable orderTable = new OrderTable(1L, null, 3, false);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(),
                eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))).willReturn(true);

        //when+then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(1L, orderTable));
    }

    @Test
    void 테이블에_방문한_손님_수를_변경() {
        //given
        OrderTable orderTable = new OrderTable(1L, null, 3, false);
        OrderTable changedTable = new OrderTable(1L, null, 2, false);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(any())).willReturn(changedTable);

        //when
        OrderTable savedOrderTable = tableService.changeNumberOfGuests(1L, changedTable);

        //then
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(2);
    }

    @Test
    void 손님의_수가_0보다_작을_경우_손님_수_변경_실패() {
        //given
        OrderTable orderTable = new OrderTable(1L, null, 3, false);
        OrderTable changedTable = new OrderTable(1L, null, -1, false);

        //when+then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeNumberOfGuests(1L, changedTable));
    }

    @Test
    void 빈_테이블일_경우_손님_수_변경_실패() {
        //given
        OrderTable orderTable = new OrderTable(1L, null, 3, true);
        OrderTable changedTable = new OrderTable(1L, null, 2, true);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));

        //when+then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeNumberOfGuests(1L, changedTable));
    }

    @Test
    void 존재하지_않는_테이블일_경우_손님_수_변경_실패() {
        //given
        OrderTable orderTable = new OrderTable(999L, null, 3, false);
        OrderTable changedTable = new OrderTable(999L, null, 2, false);
        given(orderTableDao.findById(999L)).willReturn(Optional.empty());

        //when+then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeNumberOfGuests(999L, changedTable));
    }
}

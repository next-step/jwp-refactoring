package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("테이블 등록")
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

    @DisplayName("테이블 상태를 빈 테이블 상태로 변경")
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

    @DisplayName("등록되지 않은 테이블일 경우 빈테이블로 변경할 수 없다.")
    @Test
    void 존재하지_않는_테이블일_경우_빈_테이블로_변경_실패() {
        //given
        OrderTable orderTable = new OrderTable(999L, null, 1, false);
        given(orderTableDao.findById(999L)).willReturn(Optional.empty());

        //when+then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(999L, orderTable));
    }

    @DisplayName("테이블 그룹이 등록되어 있다면 빈테이블로 변경할 수 없다.")
    @Test
    void 테이블_그룹이_등록되어_있다면_빈_테이블로_변경_실패() {
        //given
        OrderTable orderTable = new OrderTable(1L, 1L, 3, false);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
        //when+then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(1L, orderTable));
    }

    @DisplayName("주문의 상태가 계산 완료가 아닌 경우 빈테이블로 변경할 수 없다.")
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

    @DisplayName("테이블에 방문한 손님의 수를 변경")
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

    @DisplayName("손님의 수가 0보다 작은 경우 손님의 수를 변경할 수 없다.")
    @Test
    void 손님의_수가_0보다_작을_경우_손님_수_변경_실패() {
        //given
        OrderTable orderTable = new OrderTable(1L, null, 3, false);
        OrderTable changedTable = new OrderTable(1L, null, -1, false);

        //when+then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeNumberOfGuests(1L, changedTable));
    }

    @DisplayName("빈테이블일 경우 손님의 수를 변경할 수 없다.")
    @Test
    void 빈_테이블일_경우_손님_수_변경_실패() {
        //given
        OrderTable orderTable = new OrderTable(1L, null, 3, true);
        OrderTable changedTable = new OrderTable(1L, null, 2, true);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));

        //when+then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeNumberOfGuests(1L, changedTable));
    }

    @DisplayName("등록되지 않은 테이블일 경우 손님의 수를 변경할 수 없다.")
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

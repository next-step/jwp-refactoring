package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable orderTable;

    private OrderTable orderTable2;

    private OrderTable 빈_테이블;

    private OrderTable 주문상태_변경_테이블;

    private OrderTable 손님_수_변경_테이블;

    @BeforeEach
    void setUp() {
        orderTable = OrderTable.of(1L, null, 2, false);
        orderTable2 = OrderTable.of(2L, null, 3, false);
        빈_테이블 = OrderTable.of(1L, null, 2, true);
        주문상태_변경_테이블 = OrderTable.of(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), true);
        손님_수_변경_테이블 = OrderTable.of(orderTable.getId(), orderTable.getTableGroupId(), 4, orderTable.isEmpty());
    }

    @DisplayName("테이블을 등록한다.")
    @Test
    void create() {
        //given
        when(orderTableDao.save(any())).thenReturn(orderTable);

        //when
        OrderTable expected = tableService.create(orderTable);

        //then
        assertThat(orderTable.getId()).isEqualTo(expected.getId());
    }

    @DisplayName("테이블을 조회한다.")
    @Test
    void list() {
        //given
        List<OrderTable> actual = Arrays.asList(orderTable, orderTable2);
        when(orderTableDao.findAll()).thenReturn(actual);

        //when
        List<OrderTable> expected = tableService.list();

        //then
        assertThat(actual.size()).isEqualTo(expected.size());
    }

    @DisplayName("테이블을 빈테이블로 변경한다.")
    @Test
    void changeEmpty() {
        //given

        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(any())).thenReturn(주문상태_변경_테이블);

        //when
        OrderTable expected = tableService.changeEmpty(orderTable.getId(), 주문상태_변경_테이블);

        //then
        assertThat(주문상태_변경_테이블.isEmpty()).isEqualTo(expected.isEmpty());
    }

    @DisplayName("그룹테이블에 속해있는 테이블을 빈테이블로 변경할 수 없다.")
    @Test
    void changeEmpty2() {
        //given
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));

        //then
        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), 주문상태_변경_테이블)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 조리중이거나 식사중인 테이블을 빈테이블로 변경한다.")
    @Test
    void changeEmpty3() {
        //given
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        //then
        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), 주문상태_변경_테이블)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(any())).thenReturn(손님_수_변경_테이블);

        //when
        OrderTable changeNumberOfGuestsOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), 손님_수_변경_테이블);

        //then
        assertThat(changeNumberOfGuestsOrderTable.getNumberOfGuests()).isEqualTo(손님_수_변경_테이블.getNumberOfGuests());
    }

    @DisplayName("테이블의 손님 수를  음수로 변경할 수 없다.")
    @Test
    void changeNumberOfGuests2() {
        //given
        OrderTable 손님_수_음수_변경_테이블 = OrderTable.of(orderTable.getId(), orderTable.getTableGroupId(), -1, orderTable.isEmpty());

        // then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), 손님_수_음수_변경_테이블)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 손님 수는 변경할 수 없다.")
    @Test
    void changeNumberOfGuests3() {
        //given
        when(orderTableDao.findById(any())).thenReturn(Optional.of(빈_테이블));

        // then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(빈_테이블.getId(), 손님_수_변경_테이블)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}

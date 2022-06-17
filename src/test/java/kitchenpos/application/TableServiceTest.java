package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
    }

    @Test
    @DisplayName("주문 테이블을 생성할 수 있다.")
    void create() {
        //given
        given(orderTableDao.save(any())).willReturn(orderTable);

        //then
        assertThat(tableService.create(orderTable)).isEqualTo(orderTable);
    }

    @Test
    @DisplayName("전체 주문 테이블을 조회할 수 있다.")
    void list() {
        //given
        given(orderTableDao.findAll()).willReturn(Arrays.asList(orderTable));

        //then
        assertThat(tableService.list()).containsExactly(orderTable);
    }

    @Test
    @DisplayName("주문 테이블을 빈 테이블로 변경할 수 있다.")
    void changeEmpty() {
        //given
        orderTable.setEmpty(true);
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setEmpty(false);
        given(orderTableDao.findById(any())).willReturn(Optional.of(savedOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(savedOrderTable);

        //when
        OrderTable updatedOrderTable = tableService.changeEmpty(0L, orderTable);

        //then
        assertThat(updatedOrderTable).isExactlyInstanceOf(OrderTable.class);
        assertThat(updatedOrderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블이 조회되지 않으면 빈 테이블로 변경 실패한다.")
    void changeEmpty_failed_1() {
        //given
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(0L, orderTable)).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장된 주문 테이블의 단체 지정이 되어있으면 빈 테이블로 변경 실패한다.")
    void changeEmpty_failed_2() {
        //given
        orderTable.setTableGroupId(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(0L, orderTable)).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 상태가 조리, 식사 인 경우 빈 테이블로 변경 실패한다.")
    void changeEmpty_failed_3() {
        //given
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(0L, orderTable)).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    void changeNumberOfGuests() {
        //given
        orderTable.setNumberOfGuests(10);
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setEmpty(false);
        savedOrderTable.setNumberOfGuests(0);
        given(orderTableDao.findById(any())).willReturn(Optional.of(savedOrderTable));
        given(orderTableDao.save(savedOrderTable)).willReturn(savedOrderTable);

        //when
        OrderTable updatedOrderTable = tableService.changeNumberOfGuests(0L, orderTable);

        //then
        assertThat(updatedOrderTable.isEmpty()).isFalse();
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @Test
    @DisplayName("주문 테이블의 방문한 손님 수가 음수면 방문한 손님 수 변경에 실패한다.")
    void changeNumberOfGuests_failed_1() {
        //given
        orderTable.setNumberOfGuests(-1);

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, orderTable)).isExactlyInstanceOf(
                IllegalArgumentException.class);

    }

    @Test
    @DisplayName("주문 테이블이 조회가 안 되면 방문한 손님 수 변경에 실패한다.")
    void changeNumberOfGuests_failed_2() {
        //given
        orderTable.setNumberOfGuests(10);
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, orderTable)).isExactlyInstanceOf(
                IllegalArgumentException.class);

    }

    @Test
    @DisplayName("주문 테이블이 비어있으면 방문한 손님 수 변경에 실패한다.")
    void changeNumberOfGuests_failed_3() {
        //given
        orderTable.setEmpty(true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, orderTable)).isExactlyInstanceOf(
                IllegalArgumentException.class);

    }
}

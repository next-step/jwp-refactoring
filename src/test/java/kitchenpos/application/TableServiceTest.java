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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("주문 테이블을 생성한다.")
    void create() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);

        given(orderTableDao.save(orderTable)).willReturn(orderTable);

        // when
        OrderTable result = tableService.create(orderTable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다.")
    void list() {
        // given
        given(orderTableDao.findAll()).willReturn(new ArrayList<>());

        // when
        List<OrderTable> result = tableService.list();

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("주문 테이블을 빈 테이블로 변경한다.")
    void changeEmpty() {
        // given
        OrderTable saveOrderTable = new OrderTable();
        saveOrderTable.setEmpty(false);

        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(saveOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        given(orderTableDao.save(any())).willReturn(saveOrderTable);

        // when
        OrderTable result = tableService.changeEmpty(1L, orderTable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블이면 빈 테이블로 변경할 수 없다.")
    void changeEmpty_not_exist_order_table() {
        // given
        OrderTable orderTable = new OrderTable();
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 이미 단체 지정이 되어 있으면 빈 테이블로 변경할 수 없다.")
    void changeEmpty_table_group() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(orderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 조리 또는 식사 중이면 빈 테이블로 변경로 변경할 수 없다.")
    void changeEmpty_cooking_or_meal() {
        // given
        OrderTable orderTable = new OrderTable();
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블에 방문한 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);

        OrderTable saveOrderTable = new OrderTable();
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(saveOrderTable));
        given(orderTableDao.save(any())).willReturn(saveOrderTable);

        // when
        OrderTable result = tableService.changeNumberOfGuests(1L, orderTable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @Test
    @DisplayName("변경할 방문 손님 수가 0보다 작으면 방문한 손님 수를 변경할 수 없다.")
    void changeNumberOfGuests_guest_count_zero() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(-1);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블이면 방문한 손님 수를 변경할 수 없다.")
    void changeNumberOfGuests_not_exist_order_table() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);

        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블이면 방문한 손님 수를 변경할 수 없다.")
    void changeNumberOfGuests_empty_table() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);

        OrderTable saveOrderTable = new OrderTable();
        saveOrderTable.setEmpty(true);
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(saveOrderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @InjectMocks
    TableService tableService;

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void create() {
        //given
        OrderTable savedTable = new OrderTable(1L, null, 0, true);
        given(orderTableDao.save(any())).willReturn(savedTable);

        //when
        OrderTable orderTable = new OrderTable(null, null, 0, true);
        OrderTable createOrderTable = tableService.create(orderTable);

        //then
        assertThat(createOrderTable.getId()).isEqualTo(1L);
        assertThat(createOrderTable.getNumberOfGuests()).isEqualTo(0);
        assertThat(createOrderTable.isEmpty()).isEqualTo(true);
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        given(orderTableDao.findAll())
                .willReturn(
                        Arrays.asList(
                                new OrderTable(1L, null, 0, true),
                                new OrderTable(2L, null, 0, true)
                        )
                );

        //when
        List<OrderTable> orderTables = tableService.list();

        //then
        assertThat(orderTables.size()).isEqualTo(2);
        assertThat(orderTables.get(0).getId()).isEqualTo(1);
        assertThat(orderTables.get(0).getNumberOfGuests()).isEqualTo(0);
        assertThat(orderTables.get(1).getId()).isEqualTo(2);
        assertThat(orderTables.get(1).getNumberOfGuests()).isEqualTo(0);
    }

    @DisplayName("주문 테이블을 비울수 있다.")
    @Test
    void changeEmpty1() {
        //given
        OrderTable savedOrderTable = new OrderTable(2L, null, 0, true);

        given(orderTableDao.findById(2L))
                .willReturn(
                        Optional.of(savedOrderTable)
                );
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
                .willReturn(false);
        given(orderTableDao.save(any()))
                .willReturn(savedOrderTable);

        //when
        OrderTable orderTable = new OrderTable(2L, null, 0, false);
        OrderTable changedOrderTable = tableService.changeEmpty(2L, orderTable);

        //then
        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블을 비울수 있다. - 그룹 지정이 되어 있는것은 비울수 없음")
    @Test
    void changeEmpty2() {
        //given
        given(orderTableDao.findById(2L))
                .willReturn(
                        Optional.of(new OrderTable(2L, 1L, 0, true))
                );

        OrderTable orderTable = new OrderTable(2L, 1L, 0, false);

        //when
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(2L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블을 비울수 있다. - 주문 테이블이 없거나, 이미 조리중, 식사중인 상태이면 주문 테이블을 비울 수 없다.")
    @Test
    void changeEmpty3() {
        //given
        OrderTable findTable = new OrderTable(2L, null, 0, true);
        given(orderTableDao.findById(2L)).willReturn(Optional.of(findTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        OrderTable orderTable = new OrderTable(2L, null, 0, false);

        //when
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(2L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("주문 테이블의 방문한 손님수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests1() {
        //given
        OrderTable findTable = new OrderTable(3L, null, 1, false);
        given(orderTableDao.findById(3L)).willReturn(Optional.of(findTable));
        given(orderTableDao.save(any())).willReturn(findTable);

        //when
        OrderTable orderTable = new OrderTable(3L, null, 3, true);
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

        //then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(3);
    }

    @DisplayName("주문 테이블의 방문한 손님수를 변경할 수 있다. - 손님수를 0 이하로 변경 할 수 없다.")
    @Test
    void changeNumberOfGuests2() {
        //given
        OrderTable orderTable = new OrderTable(3L, null, -1, true);

        //when
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문한 손님수를 변경할 수 있다. - 현재 빈 테이블은 변경할 수 없다.")
    @Test
    void changeNumberOfGuests3() {
        //given
        OrderTable findTable = new OrderTable(3L, null, 0, true);
        given(orderTableDao.findById(3L)).willReturn(Optional.of(findTable));

        OrderTable orderTable = new OrderTable(3L, null, 3, true);

        //when
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
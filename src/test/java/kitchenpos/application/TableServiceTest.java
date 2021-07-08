package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 테이블 관리")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("주문 테이블을 추가한다.")
    @Test
    void create() {
        //given
        OrderTable orderTable = OrderTable.of(null,null,0, false);
        given(orderTableDao.save(any())).willReturn(orderTable);

        //when
        OrderTable actual = tableService.create(orderTable);

        //then
        assertThat(actual).isEqualTo(orderTable);
    }

    @DisplayName("주문 테이블을 모두 조회한다.")
    @Test
    void list() {
        //given
        OrderTable orderTable1 = OrderTable.of(1L, null, 0, false);
        OrderTable orderTable2 = OrderTable.of(2L, null, 0, false);
        given(orderTableDao.findAll()).willReturn(Lists.list(orderTable1, orderTable2));

        //when
        List<OrderTable> actual = tableService.list();

        //then
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0).getId()).isEqualTo(orderTable1.getId());
        assertThat(actual.get(1).getId()).isEqualTo(orderTable2.getId());
    }

    @DisplayName("특정 주문 테이블의 상태를 변경한다.")
    @Test
    void changeEmpty() {
        //given
        OrderTable orderTable = OrderTable.of(null, null, 0, true);
        OrderTable changeOrderTable = OrderTable.of(null, null, 0, false);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(changeOrderTable);

        //when
        OrderTable actual = tableService.changeEmpty(1L, changeOrderTable);

        //then
        assertThat(actual.isEmpty()).isFalse();
    }

    @DisplayName("테이블의 주문 상태가 계산 완료가 아니면 테이블 상태를 변경할 수 없다.")
    @Test
    void changeEmptyExceptionIfTableOrderStatusIsNotCompletion() {
        //given
        OrderTable orderTable = OrderTable.of(null, null, 0, true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        //when
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("특정 테이블의 인원 수를 예약한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTable orderTable = OrderTable.of(null, null, 4, false);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(any())).willReturn(orderTable);

        //when
        OrderTable actual = tableService.changeNumberOfGuests(1L, orderTable);

        //then
        assertThat(actual).isEqualTo(orderTable);
        assertThat(actual.getNumberOfGuests()).isEqualTo(4);
    }

    @DisplayName("주문 불가능 상태일 경우 인원수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsIfTableEmptyIsTrue() {
        //given
        OrderTable orderTable = OrderTable.of(null, null, 0, true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

        //when
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("주문 테이블의 인원수는 최소 0명 이상이어야 한다.")
    @Test
    void createTableExceptionIfNumberOfGuestsIsNull() {
        //given
        OrderTable orderTable = OrderTable.of(null, null, -10, false);

        //when
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class); //then
    }
}

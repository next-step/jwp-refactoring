package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("테이블 그룹 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    private TableService tableService;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("새로운 테이블을 등록한다.")
    @Test
    void createTableTest() {
        when(orderTableDao.save(any())).thenReturn(new OrderTable(1L, 1L, 3, false));

        // when
        final OrderTable createdOrderTable = tableService.create(new OrderTable());

        // then
        assertAll(
                () -> assertThat(createdOrderTable.getId()).isEqualTo(1L),
                () -> assertThat(createdOrderTable.getTableGroupId()).isEqualTo(1L),
                () -> assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(3),
                () -> assertThat(createdOrderTable.isEmpty()).isEqualTo(false)
        );
    }

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void getListTableTest() {
        when(orderTableDao.findAll())
                .thenReturn(Lists.newArrayList(new OrderTable(1L, 1L, 3, false), new OrderTable(2L, 1L, 7, false)));

        // when
        final List<OrderTable> createdOrderTables = tableService.list();

        // then
        assertAll(
                () -> assertThat(createdOrderTables.get(0).getId()).isEqualTo(1L),
                () -> assertThat(createdOrderTables.get(1).getId()).isEqualTo(2L)
        );
    }

    @DisplayName("빈 테이블로 변경한다.")
    @Test
    void changeEmptyTableTest() {
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(new OrderTable(1L, null, 3, false)));
        when(orderTableDao.save(any()))
                .thenReturn(new OrderTable(1L, null, 3, false));
        // when
        final OrderTable changeEmptyTable = tableService.changeEmpty(1L, new OrderTable());

        // then
        assertAll(
                () -> assertThat(changeEmptyTable.getId()).isEqualTo(1L),
                () -> assertThat(changeEmptyTable.getTableGroupId()).isEqualTo(null),
                () -> assertThat(changeEmptyTable.getNumberOfGuests()).isEqualTo(3),
                () -> assertThat(changeEmptyTable.isEmpty()).isEqualTo(false)
        );
    }

    @DisplayName("주문 테이블이 반드시 존재한다.")
    @Test
    void changeEmptyTableExistTableExceptionTest() {
        assertThatThrownBy(() -> {
            when(orderTableDao.findById(any())
                    .orElseThrow(IllegalArgumentException::new))
                    .thenReturn(null);
            // when
            final OrderTable changeEmptyTable = tableService.changeEmpty(1L, new OrderTable());

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태는 cooking이나 meal이 아니어야 한다. ")
    @Test
    void changeEmptyTableOrderStatusExceptionTest() {
        assertThatThrownBy(() -> {

            // when
            final OrderTable changeEmptyTable = tableService.changeEmpty(1L, new OrderTable());

            // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 게스트 숫자를 변경한다. ")
    @Test
    void changeNumberOfGuestsTest() {
        // given
        final OrderTable orderTable = new OrderTable(1L, 1L, 7, false);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(any())).thenReturn(orderTable);

        // when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(1L, orderTable);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(7);
    }

    @DisplayName("테이블 게스트 숫자는 0 이하일 수 없다.")
    @Test
    void changeNumberOfGuestsNegativeNumberExceptionTest() {
        assertThatThrownBy(() -> {
            // given
            final OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(0);

            // when
            tableService.changeNumberOfGuests(1L, orderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

}

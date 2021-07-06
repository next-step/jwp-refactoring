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

import java.util.Collections;
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

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(1L);
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);
    }

    @Test
    @DisplayName("단체지정 되지않은 테이블을 생성한다")
    void createTest() {

        // given
        orderTable.setTableGroupId(null);
        when(orderTableDao.save(any())).thenReturn(orderTable);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable).isNotNull();
        assertThat(savedOrderTable.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다")
    void listTest() {

        // given
        when(orderTableDao.findAll()).thenReturn(Collections.singletonList(orderTable));

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).isNotNull();
        assertThat(orderTables).hasSize(1);
        assertThat(orderTables.get(0).getId()).isEqualTo(orderTable.getId());
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않는 경우 예외가 발생한다")
    void notExistOrderTableTest() {

        // given
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(any(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정된 주문 테이블인 경우 예외가 발생한다")
    void notValidOrderTableTest() {

        // given
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(any(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 주문 내 주문 상태가 조리중 또는 식사중인 주문 테이블이 있는 경우 예외가 발생한다")
    void containsNotValidOrderTableStatusTest() {

        // given
        orderTable.setTableGroupId(null);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(true);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(any(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블을 빈 테이블로 변경한다")
    void changeEmptyTest() {

        // given
        orderTable.setTableGroupId(null);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(false);
        when(orderTableDao.save(any())).thenReturn(orderTable);

        // when
        OrderTable changedOrderTable = tableService.changeEmpty(any(), this.orderTable);

        // then
        assertThat(changedOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty());
    }
}
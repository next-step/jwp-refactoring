package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setup() {
        this.tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void createOrderTableTest() {
        // given
        OrderTable orderTableRequest = new OrderTable();
        OrderTable saved = new OrderTable();
        saved.setId(1L);
        given(orderTableDao.save(orderTableRequest)).willReturn(saved);

        // when
        OrderTable orderTable = tableService.create(orderTableRequest);

        // then
        assertThat(orderTable.getId()).isNotNull();
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void orderTableListTest() {
        // given
        int expectedSize = 2;
        given(orderTableDao.findAll()).willReturn(Arrays.asList(new OrderTable(), new OrderTable()));

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).hasSize(expectedSize);
    }

    @DisplayName("존재하지 않는 주문 테이블의 비움 상태를 바꿀 수 없다.")
    @Test
    void changeEmptyFailWithNotExistOrderTableTest() {
        // given
        Long targetId = 1L;
        OrderTable emptyRequest = new OrderTable();
        emptyRequest.setEmpty(false);
        given(orderTableDao.findById(targetId)).willThrow(new IllegalArgumentException());

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(targetId, emptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 주문 테이블의 비움 상태를 바꿀 수 없다.")
    @Test
    void changeEmptyFailWithGroupedTableTest() {
        // given
        Long targetId = 1L;

        OrderTable emptyRequest = new OrderTable();
        emptyRequest.setEmpty(false);

        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setId(targetId);
        savedOrderTable.setTableGroupId(1L);

        given(orderTableDao.findById(targetId)).willReturn(Optional.of(savedOrderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(targetId, emptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 조리거나 식사인 주문 테이블의 비움 상태를 바꿀 수 없다.")
    @Test
    void changeEmptyFailWithInvalidOrderStatusTest() {
        // given
        Long targetId = 1L;

        OrderTable emptyRequest = new OrderTable();
        emptyRequest.setEmpty(false);

        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setId(targetId);

        given(orderTableDao.findById(targetId)).willReturn(Optional.of(savedOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(targetId, emptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 비움 상태를 바꿀 수 있다.")
    @Test
    void changeEmptyTest() {
        // given
        Long targetId = 1L;

        OrderTable emptyRequest = new OrderTable();
        emptyRequest.setEmpty(false);

        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setId(targetId);

        OrderTable changedOrderTable = new OrderTable();
        changedOrderTable.setEmpty(emptyRequest.isEmpty());

        given(orderTableDao.findById(targetId)).willReturn(Optional.of(savedOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);
        given(orderTableDao.save(savedOrderTable)).willReturn(changedOrderTable);

        // when
        OrderTable orderTable = tableService.changeEmpty(targetId, emptyRequest);

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }
}

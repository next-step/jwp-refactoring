package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.TableService;
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

@DisplayName("테이블 서비스 테스트")
@ExtendWith({MockitoExtension.class})
public class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private final OrderTable emptyTable = new OrderTable();
    private final OrderTable requestTable = new OrderTable();

    @BeforeEach
    void setUp() {
        emptyTable.setId(1L);
        emptyTable.setEmpty(true);
        emptyTable.setNumberOfGuests(0);
    }

    @Test
    @DisplayName("빈 테이블을 등록한다.")
    void create() {
        when(orderTableDao.save(any(OrderTable.class)))
            .thenReturn(emptyTable);

        OrderTable saved = tableService.create(emptyTable);

        assertNull(saved.getTableGroupId());
    }

    @Test
    @DisplayName("테이블 목록을 조회한다.")
    void list() {
        when(orderTableDao.findAll())
            .thenReturn(Arrays.asList(emptyTable));

        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("테이블의 상태를 변경할 수 있다.")
    void changeEmpty() {
        requestTable.setEmpty(false);

        assertTrue(emptyTable.isEmpty());

        when(orderTableDao.findById(anyLong()))
            .thenReturn(Optional.of(emptyTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any()))
            .thenReturn(false);
        when(orderTableDao.save(any(OrderTable.class)))
            .thenReturn(emptyTable);

        OrderTable orderTable = tableService.changeEmpty(emptyTable.getId(), requestTable);

        assertFalse(orderTable.isEmpty());
    }

    @Test
    @DisplayName("주문이 진행중인 테이블은 상태를 변경할 수 없다.")
    void changeEmptyValidateOrderStatus() {
        requestTable.setEmpty(false);

        assertAll(() -> {
            assertTrue(emptyTable.isEmpty());
            assertNull(emptyTable.getTableGroupId());
        });

        when(orderTableDao.findById(anyLong()))
            .thenReturn(Optional.of(emptyTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any()))
            .thenReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(emptyTable.getId(), requestTable))
            .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("단체 지정된 테이블은 상태를 변경할 수 없다.")
    void changeEmptyValidateTableGroup() {
        requestTable.setEmpty(false);
        emptyTable.setTableGroupId(1L);

        assertTrue(emptyTable.isEmpty());

        when(orderTableDao.findById(anyLong()))
            .thenReturn(Optional.of(emptyTable));

        assertThatThrownBy(() -> tableService.changeEmpty(emptyTable.getId(), requestTable))
            .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("테이블의 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        requestTable.setNumberOfGuests(2);
        emptyTable.setEmpty(false);

        when(orderTableDao.findById(anyLong()))
            .thenReturn(Optional.of(emptyTable));
        when(orderTableDao.save(any()))
            .thenReturn(emptyTable);

        OrderTable orderTable = tableService.changeNumberOfGuests(emptyTable.getId(), requestTable);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(2);
    }

    @Test
    @DisplayName("손님의 수는 0 이상 이다.")
    void changeNumberOfGuestsValidateNumber() {
        requestTable.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(emptyTable.getId(), requestTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블은 손님의 수를 변경할 수 없다.")
    void changeNumberOfGuestsValidateEmpty() {
        requestTable.setNumberOfGuests(5);

        when(orderTableDao.findById(anyLong()))
            .thenReturn(Optional.of(emptyTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(emptyTable.getId(), requestTable))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

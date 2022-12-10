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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문테이블")
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private OrderTable orderTable3;

    @BeforeEach
    void setUp() {
        orderTable1 = generateOrderTable(1L, 5, false);
        orderTable2 = generateOrderTable(2L, 3, false);
        orderTable3 = generateOrderTable(3L, 4, false);
    }

    @Test
    @DisplayName("전체 주문 테이블을 조회할 수 있다.")
    void tableTest1() {
        given(orderTableDao.findAll()).willReturn(Arrays.asList(orderTable1, orderTable2, orderTable3));

        List<OrderTable> orderTables = tableService.list();
        assertThat(orderTables.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("새로운 주문 테이블을 추가할 수 있다.")
    void tableTest2() {
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable1);

        OrderTable orderTable = tableService.create(orderTable1);
        assertThat(orderTable.getId()).isEqualTo(orderTable1.getId());
    }

    @Test
    @DisplayName("주문 테이블의 공석여부를 변경할 수 있다.")
    void tableTest3() {
        OrderTable changedOrderTable = generateOrderTable(null, 0, true);

        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        orderTable1.setEmpty(changedOrderTable.isEmpty());
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);

        OrderTable orderTable = tableService.changeEmpty(orderTable1.getId(), changedOrderTable);
        assertThat(orderTable.isEmpty()).isEqualTo(changedOrderTable.isEmpty());
    }

    @Test
    @DisplayName("공석여부 변경 - 주문 테이블은 단체 지정이 되어있으면 안된다.")
    void tableTest4() {
        OrderTable orderTable = generateOrderTable(1L, 1L);

        given(orderTableDao.findById(any(Long.class))).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), any(OrderTable.class))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("공석여부를 변경 - 주문 테이블의 상태가 조리중이거나 식사중이면 안된다.")
    void tableTest5() {
        given(orderTableDao.findById(any(Long.class))).willReturn(Optional.of(orderTable1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), any(OrderTable.class))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 게스트 수를 변경할 수 있다.")
    void tableTest6() {
        OrderTable changedOrderTable = generateOrderTable(null, 5, false);

        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));

        orderTable1.setNumberOfGuests(changedOrderTable.getNumberOfGuests());
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);

        OrderTable orderTable = tableService.changeNumberOfGuests(orderTable1.getId(), changedOrderTable);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(changedOrderTable.getNumberOfGuests());
    }

    @Test
    @DisplayName("게스트 수 변경 - 게스트 수는 음수여선 안된다.")
    void tableTest7() {
        OrderTable changedOrderTable = generateOrderTable(null, -1, false);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable1.getId(), changedOrderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("게스트 수 변경 - 존재하지 않는 주문 테이블로 요청할 수 없다.")
    void tableTest8() {
        OrderTable changedOrderTable = generateOrderTable(null, 5, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable1.getId(), changedOrderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("게스트 수 변경 - 빈 주문 테이블이어선 안된다.")
    void tableTest9() {
        OrderTable changedOrderTable = generateOrderTable(null, 5, false);

        given(orderTableDao.findById(any(Long.class))).willReturn(Optional.empty());
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable1.getId(), changedOrderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    public static OrderTable generateOrderTable(Long id, int numberOfGuests, boolean empty) {
        return OrderTable.of(id, null, numberOfGuests, empty);
    }

    public static OrderTable generateOrderTable(Long id, Long tableGroupId) {
        return OrderTable.of(id, tableGroupId, 0, true);
    }

}
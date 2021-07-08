package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.utils.domain.OrderTableObjects;
import kitchenpos.utils.domain.TableGroupObjects;

@ExtendWith(MockitoExtension.class)
@DisplayName("테이블 서비스")
class TableServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;
    private OrderTable changeEmptyOrderTable;
    private OrderTable beforeOrderTable;
    private TableGroup tableGroup1;
    private OrderTable changeNumberOrderTable;
    private List<OrderTable> orderTables;

    @BeforeEach
    void setUp() {
        OrderTableObjects orderTableObjects = new OrderTableObjects();
        TableGroupObjects tableGroupObjects = new TableGroupObjects();
        changeEmptyOrderTable = orderTableObjects.getOrderTable1();
        beforeOrderTable = orderTableObjects.getOrderTable2();
        changeNumberOrderTable = orderTableObjects.getOrderTable3();
        tableGroup1 = tableGroupObjects.getTableGroup1();
        orderTables = orderTableObjects.getOrderTables();
    }

    @Test
    @DisplayName("주문 테이블 전체 조회")
    void find_all_orderTable() {
        // mocking
        when(orderTableDao.findAll()).thenReturn(orderTables);

        // when
        List<OrderTable> resultOrderTable = tableService.list();

        // then
        assertThat(resultOrderTable.size()).isEqualTo(orderTables.size());
    }

    @Test
    @DisplayName("테이블 비움처리 요청")
    void changeEmpty_table() {
        // given
        changeEmptyOrderTable.setEmpty(true);
        beforeOrderTable.setEmpty(false);

        // mocking
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(beforeOrderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any(List.class))).thenReturn(false);
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(beforeOrderTable);

        // when
        OrderTable resultOrderTable = tableService.changeEmpty(changeEmptyOrderTable.getId(), changeEmptyOrderTable);

        // then
        assertThat(resultOrderTable.isEmpty()).isTrue();

    }

    @TestFactory
    @DisplayName("테이블 비움처리시 오류 발생")
    List<DynamicTest> changeEmpty_exception() {
        return Arrays.asList(
                dynamicTest("주문 테이블 조회 불가시 오류 발생.", () -> {
                    // mocking
                    when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

                    // when
                    assertThatThrownBy(() -> tableService.changeEmpty(changeEmptyOrderTable.getId(), changeEmptyOrderTable))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("단체지정이 된 상태일 경우 오류 발생.", () -> {
                    // given
                    changeEmptyOrderTable.setTableGroupId(tableGroup1.getId());

                    // mocking
                    when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(changeEmptyOrderTable));

                    // when
                    assertThatThrownBy(() -> tableService.changeEmpty(changeEmptyOrderTable.getId(), changeEmptyOrderTable))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("주문 상태가 COOKING이거나 MEAL상태이면 오류 발생.", () -> {
                    // mocking
                    changeEmptyOrderTable.setTableGroupId(null);
                    when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(changeEmptyOrderTable));
                    when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any(List.class))).thenReturn(true);

                    // when
                    assertThatThrownBy(() -> tableService.changeEmpty(changeEmptyOrderTable.getId(), changeEmptyOrderTable))
                            .isInstanceOf(IllegalArgumentException.class);
                })
        );
    }

    @Test
    @DisplayName("고객 수 변경 요청")
    void change_numberOfGuests() {
        // given
        beforeOrderTable.setNumberOfGuests(5);
        beforeOrderTable.setEmpty(false);
        changeNumberOrderTable.setNumberOfGuests(4);

        // mocking
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(beforeOrderTable));
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(beforeOrderTable);

        // when
        OrderTable resultOrderTable = tableService.changeNumberOfGuests(beforeOrderTable.getId(), changeEmptyOrderTable);

        // then
        assertThat(resultOrderTable.getNumberOfGuests()).isEqualTo(changeEmptyOrderTable.getNumberOfGuests());
    }

    @TestFactory
    @DisplayName("고객 수 변경 요청 오류 발생")
    List<DynamicTest> changeNumberOfGuests_exception() {
        return Arrays.asList(
                dynamicTest("변경하려는 고객의 수가 음수로 입력되었을 경우 오류 발생.", () -> {
                    // given
                    changeNumberOrderTable.setNumberOfGuests(-1);

                    // then
                    assertThatThrownBy(() -> tableService.changeNumberOfGuests(changeNumberOrderTable.getId(), changeEmptyOrderTable))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("변경하려는 테이블 조회가 실패 할 경우 오류 발생.", () -> {
                    // mocking
                    when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

                    // then
                    assertThatThrownBy(() -> tableService.changeNumberOfGuests(changeNumberOrderTable.getId(), changeEmptyOrderTable))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("변경하려는 테이블이 이미 비어있는 경우 오류 발생.", () -> {
                    // given
                    changeNumberOrderTable.setEmpty(true);

                    // mocking
                    when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(changeNumberOrderTable));

                    // then
                    assertThatThrownBy(() -> tableService.changeNumberOfGuests(changeNumberOrderTable.getId(), changeEmptyOrderTable))
                            .isInstanceOf(IllegalArgumentException.class);
                })
        );
    }
}

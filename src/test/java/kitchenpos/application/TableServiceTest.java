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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 테이블 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setNumberOfGuests(0);
        orderTable1.setEmpty(true);

        orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setNumberOfGuests(0);
        orderTable2.setEmpty(true);
    }

    @DisplayName("단체 지정되지 않은 주문 테이블을 등록한다.")
    @Test
    void create() {
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);

        OrderTable createdOrderTable = tableService.create(this.orderTable1);

        assertThat(createdOrderTable.getId()).isEqualTo(this.orderTable1.getId());
        assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(this.orderTable1.getNumberOfGuests());
        assertThat(createdOrderTable.isEmpty()).isEqualTo(this.orderTable1.isEmpty());
    }

    @Test
    void list() {
        given(orderTableDao.findAll()).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).containsExactly(orderTable1, orderTable2);
        assertThat(orderTables.get(0).getId()).isEqualTo(orderTable1.getId());
        assertThat(orderTables.get(0).getNumberOfGuests()).isEqualTo(orderTable1.getNumberOfGuests());
        assertThat(orderTables.get(0).isEmpty()).isEqualTo(orderTable1.isEmpty());
        assertThat(orderTables.get(1).getId()).isEqualTo(orderTable2.getId());
        assertThat(orderTables.get(1).getNumberOfGuests()).isEqualTo(orderTable2.getNumberOfGuests());
        assertThat(orderTables.get(1).isEmpty()).isEqualTo(orderTable2.isEmpty());
    }

    @DisplayName("단체에 속한 주문 테이블은 비울수 없다.")
    @Test
    void changeEmpty_단체속한_경우_예외() {
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        orderTable1.setTableGroupId(1L);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), orderTable1))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("주문상태가 조리 또는 식사가 아니라면 예외 발생")
    @Test
    void changeEmpty_주문상태_완료_예외() {
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), orderTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블을 비울 수 있다.")
    @Test
    void changeEmpty() {
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        orderTable1.setEmpty(false);

        OrderTable orderTable = tableService.changeEmpty(orderTable1.getId(), orderTable1);

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블의 손님이 1명 이상이어야 한다.")
    @Test
    void changeNumberOfGuests_손님_0명_예외() {
        orderTable1.setEmpty(false);
        orderTable1.setNumberOfGuests(-10);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(orderTable1.getId(), orderTable1));
    }

    @DisplayName("주문 테이블이 비어있지 않아야 한다.")
    @Test
    void changeNumberOfGuests_주문테이블_없을경우_예외() {
        orderTable1.setEmpty(true);
        orderTable1.setNumberOfGuests(15);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(orderTable1.getId(), orderTable1));
    }

    @DisplayName("주문 테이블의 손님의 수를 입력한다.")
    @Test
    void changeNumberOfGuests() {
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        orderTable1.setEmpty(false);
        orderTable1.setNumberOfGuests(15);

        OrderTable orderTable = tableService.changeNumberOfGuests(orderTable1.getId(), orderTable1);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(orderTable1.getNumberOfGuests());
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(15);
    }
}
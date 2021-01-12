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
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 테이블 기능 테스트")
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

    @Test
    @DisplayName("주문 테이블을 등록할 수 있다.")
    public void create() throws Exception {
        // given
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);

        // when
        OrderTable createdOrderTable = tableService.create(this.orderTable1);

        // then
        assertThat(createdOrderTable.getId()).isEqualTo(this.orderTable1.getId());
        assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(this.orderTable1.getNumberOfGuests());
        assertThat(createdOrderTable.isEmpty()).isEqualTo(this.orderTable1.isEmpty());
    }

    @Test
    @DisplayName("주문 테이블의 목록을 조회할 수 있다.")
    public void list() throws Exception {
        // given
        given(orderTableDao.findAll()).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).containsExactly(orderTable1, orderTable2);
        assertThat(orderTables.get(0).getId()).isEqualTo(orderTable1.getId());
        assertThat(orderTables.get(0).getNumberOfGuests()).isEqualTo(orderTable1.getNumberOfGuests());
        assertThat(orderTables.get(0).isEmpty()).isEqualTo(orderTable1.isEmpty());
    }

    @Test
    @DisplayName("빈 테이블 설정 또는 해지할 수 있다.")
    public void changeEmpty() throws Exception {
        // given
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        orderTable1.setEmpty(false);

        // when
        OrderTable orderTable = tableService.changeEmpty(orderTable1.getId(), orderTable1);

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("단체 지정된 주문 테이블은 빈 테이블 설정 또는 해지할 수 없다.")
    public void changeEmptyFail1() throws Exception {
        // given
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        orderTable1.setTableGroupId(1L);

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), orderTable1));
    }

    @Test
    @DisplayName("주문 상태가 조리 또는 식사인 주문 테이블은 빈 테이블 설정 또는 해지할 수 없다.")
    public void changeEmptyFail2() throws Exception {
        // given
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), orderTable1));
    }

    @Test
    @DisplayName("방문한 손님 수를 입력할 수 있다.")
    public void changeNumberOfGuests() throws Exception {
        // given
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        orderTable1.setEmpty(false);
        orderTable1.setNumberOfGuests(5);

        // when
        OrderTable orderTable = tableService.changeNumberOfGuests(orderTable1.getId(), orderTable1);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(orderTable1.getNumberOfGuests());
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    @DisplayName("방문한 손님 수가 올바르지 않으면 입력할 수 없다. : 방문한 손님 수는 0 명 이상이어야 한다.")
    public void changeNumberOfGuestsFail() throws Exception {
        // given
        orderTable1.setEmpty(false);
        orderTable1.setNumberOfGuests(-1);

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(orderTable1.getId(), orderTable1));
    }

    @Test
    @DisplayName("빈 테이블은 방문한 손님 수를 입력할 수 없다.")
    public void notInputEmptyTable() throws Exception {
        // given
        orderTable1.setEmpty(true);
        orderTable1.setNumberOfGuests(5);

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(orderTable1.getId(), orderTable1));
    }
}

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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 테이블에 대한 비즈니스 로직")
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
        orderTable.setNumberOfGuests(5);
        orderTable.setEmpty(true);
    }

    @DisplayName("주문 테이블를 생성할 수 있다.")
    @Test
    void create() {
        // given
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);

        // when
        OrderTable actual = tableService.create(this.orderTable);

        // then
        assertThat(actual.getId()).isEqualTo(orderTable.getId());
        assertThat(actual.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        assertThat(actual.isEmpty()).isEqualTo(orderTable.isEmpty());
        assertThat(actual.getTableGroupId()).isNull();

    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        // given
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(orderTable));

        // when
        List<OrderTable> list = tableService.list();

        // then
        assertThat(list.get(0).getId()).isEqualTo(orderTable.getId());
        assertThat(list.get(0).getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        assertThat(list.get(0).isEmpty()).isEqualTo(orderTable.isEmpty());
        assertThat(list.get(0).getTableGroupId()).isNull();
    }

    @DisplayName("주문 테이블의 등록 가능 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId()
                , Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);

        OrderTable updateOrderTable = new OrderTable();
        updateOrderTable.setEmpty(false);

        // when
        OrderTable actual = tableService.changeEmpty(this.orderTable.getId(), updateOrderTable);

        // then
        assertThat(actual.isEmpty()).isEqualTo(updateOrderTable.isEmpty());
    }

    @DisplayName("단체 지정이 되어 있다면 상태를 변경할 수 없다.")
    @Test
    void inTableGroup() {
        // given
        orderTable.setTableGroupId(1L);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        OrderTable updateOrderTable = new OrderTable();
        updateOrderTable.setEmpty(false);

        // when / then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(this.orderTable.getId(), updateOrderTable));
    }

    @DisplayName("주문이 조리 중이거나 식사 중일때는 상태를 변경할 수 없다.")
    @Test
    void test() {
        // given
        boolean isCookingOrMeal = true;
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId()
                , Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(isCookingOrMeal);

        OrderTable updateOrderTable = new OrderTable();
        updateOrderTable.setEmpty(false);

        // when / then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(this.orderTable.getId(), updateOrderTable));
    }

    @DisplayName("주문 테이블에 손님 수를 등록한다.")
    @Test
    void numberOfGuests() {
        // given
        orderTable.setEmpty(false);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);

        OrderTable updateOrderTable = new OrderTable();
        updateOrderTable.setNumberOfGuests(10);

        // when
        OrderTable actual = tableService.changeNumberOfGuests(this.orderTable.getId(), updateOrderTable);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(updateOrderTable.getNumberOfGuests());
    }

    @DisplayName("주문 등록 불가 상태인 주문 테이블인 경우 등록할 수 없다.")
    @Test
    void emptyTable() {
        // given
        orderTable.setEmpty(true);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        OrderTable updateOrderTable = new OrderTable();
        updateOrderTable.setNumberOfGuests(10);

        // when / then
        assertThrows(IllegalArgumentException.class, () ->
                tableService.changeNumberOfGuests(this.orderTable.getId(), updateOrderTable));
    }

    @DisplayName("손님 수는 0보다 작을 수 없다.")
    @Test
    void numberOfGuestsRange() {
        // given
        OrderTable updateOrderTable = new OrderTable();
        updateOrderTable.setNumberOfGuests(-1);

        // when / then
        assertThrows(IllegalArgumentException.class, () ->
                tableService.changeNumberOfGuests(this.orderTable.getId(), updateOrderTable));
    }

}

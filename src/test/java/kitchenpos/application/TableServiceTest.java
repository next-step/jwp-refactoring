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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = OrderTable.of(1L, 1L, 4, false);
    }

    @Test
    @DisplayName("주문 테이블 생성")
    public void create() {
        given(orderTableDao.save(orderTable)).willReturn(orderTable);

        OrderTable createdOrderTable = tableService.create(orderTable);

        assertThat(createdOrderTable.getId()).isEqualTo(orderTable.getId());
    }

    @Test
    @DisplayName("주문 테이블 조회")
    public void list() {
        given(orderTableDao.findAll()).willReturn(Arrays.asList(orderTable));

        assertThat(tableService.list()).contains(orderTable);
    }

    @Test
    @DisplayName("주문 테이블 비울 때 주문 테이블 존재하지 않음 Exception")
    public void test1() {
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 비울 때 이미 단체로 지정됐을 경우 Exception")
    public void test2() {
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 비울 때 COOKING 또는 MEAL 상태일 때 Exception")
    public void test3() {
        OrderTable orderTable = OrderTable.of(1L, null, 4, false);

        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 비움")
    public void changEmpty() {
        OrderTable emptyOrderTable = OrderTable.of(1L, null, 4, true);

        given(orderTableDao.findById(emptyOrderTable.getId())).willReturn(Optional.of(emptyOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(emptyOrderTable.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(emptyOrderTable)).willReturn(emptyOrderTable);

        assertThat(tableService.changeEmpty(orderTable.getId(), emptyOrderTable).isEmpty()).isEqualTo(
                emptyOrderTable.isEmpty());
    }

    @Test
    @DisplayName("고객 수 변경 시 0 미만이면 Exception")
    public void test4() {
        OrderTable orderTable = OrderTable.of(1L, null, -1, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("고객 수 변경 시 존재하지 않는 테이블이면 Exception")
    public void test5() {
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.empty());
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("고객 수 변경 시 비어있는 테이블이면 Exception")
    public void test6() {
        OrderTable emptyOrderTable = OrderTable.of(1L, null, 4, true);
        given(orderTableDao.findById(emptyOrderTable.getId())).willReturn(Optional.of(emptyOrderTable));
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, emptyOrderTable)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("고객 수 변경")
    public void test7() {
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(orderTable)).willReturn(orderTable);

        assertThat(tableService.changeNumberOfGuests(1L, orderTable).getNumberOfGuests())
                .isEqualTo(orderTable.getNumberOfGuests());
    }
}
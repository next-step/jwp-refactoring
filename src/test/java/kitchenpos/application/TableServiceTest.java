package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @Test
    @DisplayName("주문 테이블을 생성한다.")
    void createOrderTable() {
        // given
        final OrderTable 주문_테이블 = new OrderTable(5);
        when(orderTableDao.save(any())).thenReturn(주문_테이블);
        // when
        final OrderTable actual = tableService.create(주문_테이블);
        // then
        assertThat(주문_테이블).isEqualTo(new OrderTable(5));
    }

    @Test
    @DisplayName("주문 테이블들을 조회한다.")
    void searchOrderTable() {
        // given
        when(orderTableDao.findAll()).thenReturn(List.of(new OrderTable()));
        // when
        final List<OrderTable> actual = tableService.list();
        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0)).isEqualTo(new OrderTable())
        );
    }

    @Test
    @DisplayName("주문 테이블을 빈 테이블로 변경한다.")
    void changeEmptyOrderTable() {
        // given
        final OrderTable fullOrderTable = new OrderTable(1L, null, 5, false);
        final OrderTable emptyOrderTable = new OrderTable(1L, null, 5, true);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(fullOrderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(1L,
                List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
        when(orderTableDao.save(any())).thenReturn(emptyOrderTable);
        // when
        final OrderTable actual = tableService.changeEmpty(1L, new OrderTable(null, null, 0, false));
        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블 주문번호가 존재하지 않으면 예외 발생")
    void notExistOrderTableId() {
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()));
    }

    @Test
    @DisplayName("빈 테이블로 변경할 테이블이 단체 그룹이면 예외 발생")
    void notTableGroup() {
        // given
        final OrderTable fullOrderTableGroup = new OrderTable(1L, 1L, 5, false);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(fullOrderTableGroup));
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()));
    }

    @Test
    @DisplayName("주문 테이블이 요리중이거나 식사중이면 예외 발생")
    void cookingAndMealOrderTable() {
        // given
        final OrderTable fullOrderTable = new OrderTable(1L, null, 5, false);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(fullOrderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(1L,
                List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()));
    }

    @Test
    @DisplayName("주문 테이블의 방문 고객수를 변경한다.")
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable5Guests = new OrderTable(1L, null, 5, false);
        final OrderTable orderTable3Guests = new OrderTable(1L, null, 5, false);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable5Guests));
        when(orderTableDao.save(any())).thenReturn(orderTable3Guests);
        // when
        final OrderTable actual = tableService.changeNumberOfGuests(1L, new OrderTable(null, null, 3, false));
        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(orderTable3Guests.getNumberOfGuests());
    }

    @Test
    @DisplayName("변경할 고객이 0명 미만이면 예외 발생")
    void negativeNumberOfGuests() {
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTable(null, null, -1, false)));
    }

    @Test
    @DisplayName("방문 고객 변경할 테이블 주문번호가 존재하지 않으면 예외 발생")
    void notExistOrderTableIdByNumberOfGuests() {
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTable(null, null, 3, false)));
    }

    @Test
    @DisplayName("빈 테이블의 고객 수는 변경시 예외 발생")
    void notChangeEmptyOrderTable() {
        // given
        final OrderTable emptyOrderTable = new OrderTable(1L, null, 0, true);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(emptyOrderTable));
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTable(null, null, 1, false)));
    }
}

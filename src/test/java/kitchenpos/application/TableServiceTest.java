package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
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
        when(orderTableDao.save(any())).thenReturn(new OrderTable(5));
        // when
        final OrderTableResponse actual = tableService.create(new OrderTableRequest(5, false));
        // then
        assertAll(
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(5),
                () -> assertThat(actual.isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("주문 테이블들을 조회한다.")
    void searchOrderTable() {
        // given
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(new OrderTable()));
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
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
        when(orderTableDao.save(any())).thenReturn(emptyOrderTable);
        // when
        final OrderTableResponse actual = tableService.changeEmpty(1L, new OrderTableRequest(null, true));
        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블 주문번호가 존재하지 않으면 예외 발생")
    void notExistOrderTableId() {
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L, new OrderTableRequest()));
    }

    @Test
    @DisplayName("빈 테이블로 변경할 테이블이 단체 그룹이면 예외 발생")
    void notTableGroup() {
        // given
        final OrderTable fullOrderTableGroup = new OrderTable(1L, 1L, 5, false);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(fullOrderTableGroup));
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L, new OrderTableRequest()));
    }

    @Test
    @DisplayName("주문 테이블이 요리중이거나 식사중이면 예외 발생")
    void cookingAndMealOrderTable() {
        // given
        final OrderTable fullOrderTable = new OrderTable(1L, null, 5, false);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(fullOrderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(1L,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L, new OrderTableRequest()));
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
        final OrderTableResponse actual = tableService.changeNumberOfGuests(1L, new OrderTableRequest(3, null));
        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(orderTable3Guests.getNumberOfGuests());
    }

    @Test
    @DisplayName("변경할 고객이 0명 미만이면 예외 발생")
    void negativeNumberOfGuests() {
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableRequest(-1, null)));
    }

    @Test
    @DisplayName("방문 고객 변경할 테이블 주문번호가 존재하지 않으면 예외 발생")
    void notExistOrderTableIdByNumberOfGuests() {
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableRequest(3, null)));
    }

    @Test
    @DisplayName("빈 테이블의 고객 수는 변경시 예외 발생")
    void notChangeEmptyOrderTable() {
        // given
        final OrderTable emptyOrderTable = new OrderTable(1L, null, 0, true);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(emptyOrderTable));
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableRequest(1, null)));
    }
}

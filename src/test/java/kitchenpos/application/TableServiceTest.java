package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.application.TableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 서비스에 대한 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable 주문_테이블;
    private OrderTable 주문_테이블_2;

    @BeforeEach
    void setUp() {
        주문_테이블 = OrderTable.of(1L, null, 3, true);
        주문_테이블_2 = OrderTable.of(2L, null, 5, true);
    }

    @DisplayName("주문 테이블을 등록하면 정상적으로 등록된다")
    @Test
    void create_test() {
        // given
        when(orderTableDao.save(주문_테이블))
            .thenReturn(주문_테이블);

        // when
        OrderTable result = tableService.create(주문_테이블);

        // then
        assertThat(result).isNotNull();
    }

    @DisplayName("주문 테이블목록을 조회한다")
    @Test
    void findAll_test() {
        // given
        when(orderTableDao.findAll())
            .thenReturn(Arrays.asList(주문_테이블, 주문_테이블));

        // when
        List<OrderTable> result = tableService.list();

        // then
        assertThat(result).hasSize(2);
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경한다")
    @Test
    void change_empty_test() {
        // given
        주문_테이블.setEmpty(false);
        when(orderTableDao.findById(주문_테이블.getId()))
            .thenReturn(Optional.of(주문_테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(주문_테이블.getId(), Arrays.asList(
            OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .thenReturn(false);
        when(orderTableDao.save(주문_테이블))
            .thenReturn(주문_테이블);

        // when
        OrderTable result = tableService.changeEmpty(주문_테이블.getId(), 주문_테이블_2);

        // then
        assertTrue(result.isEmpty());
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경시 변경할 테이블이 존재하지 않으면 예외가 발생한다")
    @Test
    void change_empty_exception_test() {
        // given
        when(orderTableDao.findById(주문_테이블.getId()))
            .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문_테이블.getId(), 주문_테이블_2);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경시 요리중, 식사중인 테이블이 있다면 예외가 발생한다")
    @Test
    void change_empty_exception_test2() {
        // given
        when(orderTableDao.findById(주문_테이블.getId()))
            .thenReturn(Optional.of(주문_테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(주문_테이블.getId(), Arrays.asList(
            OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .thenReturn(true);

        // then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문_테이블.getId(), 주문_테이블_2);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님 수를 변경하면 정상적으로 변경된다")
    @Test
    void change_number_of_guests_test() {
        // given
        주문_테이블.setEmpty(false);
        when(orderTableDao.findById(주문_테이블.getId()))
            .thenReturn(Optional.of(주문_테이블));
        when(orderTableDao.save(주문_테이블))
            .thenReturn(주문_테이블);

        // when
        OrderTable result = tableService.changeNumberOfGuests(주문_테이블.getId(), 주문_테이블_2);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(주문_테이블_2.getNumberOfGuests());
    }

    @DisplayName("주문 테이블의 손님 수 변경시 변경할 손님 수가 0미만이면 예외가 발생한다")
    @Test
    void change_number_of_guests_exception_test() {
        // given
        주문_테이블_2.setNumberOfGuests(-5);

        // then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문_테이블.getId(), 주문_테이블_2);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님 수 변경시 변경할 주문 테이블이 존재하지 않으면 예외가 발생한다")
    @Test
    void change_number_of_guests_exception_test2() {
        // given
        when(orderTableDao.findById(주문_테이블.getId()))
            .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문_테이블.getId(), 주문_테이블_2);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님 수 변경시 변경할 주문 테이블이 비어있다면 예외가 발생한다")
    @Test
    void change_number_of_guests_exception_test3() {
        // given
        when(orderTableDao.findById(주문_테이블.getId()))
            .thenReturn(Optional.of(주문_테이블));

        // then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문_테이블.getId(), 주문_테이블_2);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}

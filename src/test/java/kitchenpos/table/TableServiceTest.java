package kitchenpos.table;

import kitchenpos.application.TableService;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @InjectMocks
    TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;

    @BeforeEach
    void setUp() {
        주문테이블1 = OrderTable.of(1L, null, 2, true);
        주문테이블2 = OrderTable.of(2L, null, 3, true);
    }

    @DisplayName("주문 테이블 등록")
    @Test
    void createTable() {
        // given
        when(orderTableDao.save(주문테이블1))
                .thenReturn(주문테이블1);

        // when
        OrderTable result = tableService.create(주문테이블1);

        // then
        assertThat(result.getId()).isEqualTo(주문테이블1.getId());
    }

    @DisplayName("주문 테이블 전체 조회")
    @Test
    void findAllTables() {
        // given
        when(orderTableDao.findAll())
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));

        // when
        List<OrderTable> result = tableService.list();

        // then
        assertThat(result).hasSize(2);
    }

    @DisplayName("주문 테이블 빈 테이블로 상태 변경")
    @Test
    void changeEmpty() {
        // given
        when(orderTableDao.findById(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(주문테이블1));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블1.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(false);
        when(orderTableDao.save(주문테이블1))
                .thenReturn(주문테이블1);

        // when
        OrderTable result = tableService.changeEmpty(주문테이블1.getId(), 주문테이블1);

        // then
        assertThat(result.isEmpty());
    }

    @DisplayName("주문 테이블 빈 테이블로 상태 변경 시 단체 지정 아이디가 있는 경우 변경 불가능")
    @Test
    void changeEmptyTestAndIsTableGroup() {
        // given
        주문테이블1.setTableGroupId(1L);
        when(orderTableDao.findById(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(주문테이블1));

        // then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문테이블1.getId(), 주문테이블1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 빈 테이블로 상태 변경 시 `조리`, `식사` 상태인 경우 변경 불가능")
    @Test
    void changeEmptyTestAndNotCompletionStatus() {
        // given
        when(orderTableDao.findById(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(주문테이블1));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블1.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(true);

        // then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문테이블1.getId(), 주문테이블1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        // given
        주문테이블1.setNumberOfGuests(100);
        주문테이블1.setEmpty(false);
        when(orderTableDao.findById(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(주문테이블1));
        when(orderTableDao.save(주문테이블1))
                .thenReturn(주문테이블1);

        // when
        OrderTable result = tableService.changeNumberOfGuests(주문테이블1.getId(), 주문테이블1);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(100);
    }

    @DisplayName("방문한 손님 수 변경 시 `0` 명보다 작은 경우 변경 불가능")
    @Test
    void changeNumberOfGuestsAndGuestMin() {
        // given
        주문테이블1.setNumberOfGuests(-100);
        주문테이블1.setEmpty(false);

        // then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문테이블1.getId(), 주문테이블1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수 변경 시 미등록된 주문 테이블인 경우 변경 불가능")
    @Test
    void changeNumberOfGuestsAndNotResistTable() {
        // given
        when(orderTableDao.findById(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(주문테이블1));

        // then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문테이블1.getId(), 주문테이블1);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
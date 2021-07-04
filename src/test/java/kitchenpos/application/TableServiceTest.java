package kitchenpos.application;

import static kitchenpos.utils.DataInitializer.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.utils.DataInitializer;

@ExtendWith(MockitoExtension.class)
@DisplayName("테이블 서비스")
class TableServiceTest {

    TableService tableService;

    @Mock
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        DataInitializer.reset();
        tableService = new TableService(orderDao, orderTableDao);
    }

    @Test
    @DisplayName("테이블을 생성한다")
    void create() {
        // given
        when(orderTableDao.save(테이블1번_USING)).thenReturn(테이블1번_USING);

        // when
        OrderTable savedTable = tableService.create(테이블1번_USING);

        // then
        assertThat(savedTable.getId()).isEqualTo(테이블1번_USING.getId());
    }

    @Test
    @DisplayName("테이블 목록을 가져온다")
    void list() {
        // given
        List<OrderTable> orderTables = Arrays.asList(테이블1번_USING, 테이블2번_USING, 테이블3번_EMPTY);
        when(orderTableDao.findAll()).thenReturn(orderTables);

        // when
        List<OrderTable> allOrderTables = tableService.list();

        // then
        assertThat(allOrderTables.size()).isEqualTo(orderTables.size());
        assertThat(allOrderTables).containsExactly(테이블1번_USING, 테이블2번_USING, 테이블3번_EMPTY);
    }

    @Test
    @DisplayName("특정 테이블의 테이블 상태를 변경한다")
    void changeEmpty() {
        // given
        OrderTable 빈_테이블 = new OrderTable(true);
        when(orderTableDao.findById(테이블1번_USING.getId()))
            .thenReturn(Optional.ofNullable(테이블1번_USING));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
            .thenReturn(false);
        when(orderTableDao.save(테이블1번_USING)).thenReturn(테이블1번_USING);

        // when
        OrderTable changedTable = tableService.changeEmpty(테이블1번_USING.getId(), 빈_테이블);

        // then
        assertThat(changedTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블 상태 변경 실패(주문 테이블 없음)")
    void changeEmpty_failed1() {
        // given
        OrderTable 빈_테이블 = new OrderTable(true);
        when(orderTableDao.findById(테이블1번_USING.getId()))
            .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(테이블1번_USING.getId(), 빈_테이블))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 상태 변경 실패(테이블이 그룹에 포함되어 있음)")
    void changeEmpty_failed2() {
        // given
        OrderTable 빈_테이블 = new OrderTable(true);
        when(orderTableDao.findById(테이블5번_EMPTY.getId()))
            .thenReturn(Optional.ofNullable(테이블5번_EMPTY));

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(테이블5번_EMPTY.getId(), 빈_테이블))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 상태 변경 실패(테이블이 조리/식사 상태)")
    void changeEmpty_failed3() {
        // given
        OrderTable 빈_테이블 = new OrderTable(true);
        when(orderTableDao.findById(테이블1번_USING.getId()))
            .thenReturn(Optional.ofNullable(테이블1번_USING));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
            .thenReturn(true);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(테이블1번_USING.getId(), 빈_테이블))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("특정 테이블의 손님 수를 변경한다")
    void changeNumberOfGuests() {
        // given
        OrderTable 손님10명 = new OrderTable(10);
        when(orderTableDao.findById(테이블1번_USING.getId()))
            .thenReturn(Optional.ofNullable(테이블1번_USING));
        when(orderTableDao.save(테이블1번_USING)).thenReturn(테이블1번_USING);

        // when
        OrderTable changedTable = tableService.changeNumberOfGuests(테이블1번_USING.getId(), 손님10명);

        // then
        assertThat(changedTable.getNumberOfGuests()).isEqualTo(손님10명.getNumberOfGuests());
    }

    @Test
    @DisplayName("손님 수 변경 실패(손님 숫자가 0보다 작음)")
    void changeNumberOfGuests_failed1() {
        // given
        OrderTable 손님10명 = new OrderTable(-1);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블1번_USING.getId(), 손님10명))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("손님 수 변경 실패(주문 테이블 없음)")
    void changeNumberOfGuests_failed2() {
        // given
        OrderTable 손님10명 = new OrderTable(10);
        when(orderTableDao.findById(테이블1번_USING.getId()))
            .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블1번_USING.getId(), 손님10명))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("손님 수 변경 실패(테이블이 비어있음)")
    void changeNumberOfGuests_failed3() {
        // given
        OrderTable 손님10명 = new OrderTable(10);
        when(orderTableDao.findById(테이블3번_EMPTY.getId()))
            .thenReturn(Optional.ofNullable(테이블3번_EMPTY));

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블3번_EMPTY.getId(), 손님10명))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

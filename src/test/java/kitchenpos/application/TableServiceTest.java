package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.AfterEach;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    public static final OrderTable 일번_테이블 = new OrderTable();
    public static final OrderTable 이번_테이블 = new OrderTable();

    static {
        일번_테이블.setId(1L);
        일번_테이블.setNumberOfGuests(0);
        일번_테이블.setEmpty(true);

        이번_테이블.setId(2L);
        이번_테이블.setNumberOfGuests(0);
        이번_테이블.setEmpty(true);
    }

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    TableService tableService;

    @AfterEach
    void afterEach() {
        일번_테이블.setNumberOfGuests(0);
        일번_테이블.setEmpty(true);
    }

    @Test
    @DisplayName("테이블 추가")
    void create() {
        // given
        orderTableSave(orderTableDao, new OrderTable());
        // when
        final OrderTable orderTable = tableService.create(일번_테이블);
        // then
        assertThat(orderTable).isInstanceOf(OrderTable.class);
    }

    @Test
    @DisplayName("테이블 전체 조회")
    void list() {
        // given
        orderTableFindyAll();
        // when
        final List<OrderTable> list = tableService.list();
        // then
        assertThat(list).hasSize(1);
    }

    @Test
    @DisplayName("테이블 빈 값 상태 변경")
    void changeEmpty() {
        // given
        orderTableFindById(orderTableDao, 일번_테이블);
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
                .willReturn(false);
        일번_테이블.setEmpty(false);
        // given
        orderTableSave(orderTableDao, 일번_테이블);

        // when
        final OrderTable orderTable = tableService.changeEmpty(일번_테이블.getId(), 일번_테이블);
        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블 사람수 변경")
    void changeNumberOfGuests() {
        // given
        orderTableFindById(orderTableDao, 일번_테이블);
        일번_테이블.setEmpty(false);
        일번_테이블.setNumberOfGuests(5);
        orderTableSave(orderTableDao, 일번_테이블);
        // when
        final OrderTable orderTable = tableService.changeNumberOfGuests(일번_테이블.getId(), 일번_테이블);
        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
    }

    private void orderTableSave(final OrderTableDao orderTableDao, final OrderTable 일번테이블) {
        given(orderTableDao.save(any()))
                .willReturn(일번테이블);
    }

    private void orderTableFindById(final OrderTableDao orderTableDao, final OrderTable 일번테이블) {
        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(일번테이블));
    }

    private void orderTableFindyAll() {
        given(orderTableDao.findAll())
                .willReturn(Arrays.asList(일번_테이블));
    }
}

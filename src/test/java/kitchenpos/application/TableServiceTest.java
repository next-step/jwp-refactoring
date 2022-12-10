package kitchenpos.application;

import static kitchenpos.domain.OrderTableTest.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;


    private OrderTable 주문_테이블_1;
    private OrderTable 주문_테이블_2;
    private OrderTable 주문_테이블_3;

    @BeforeEach
    public void setUp() {
        주문_테이블_1 = 주문_테이블_생성(1L, null, 0, true);
        주문_테이블_2 = 주문_테이블_생성(2L, null, 0, false);
        주문_테이블_3 = 주문_테이블_생성(3L, null, 2, false);

    }


    @Test
    @DisplayName("주문 테이블 생성")
    void create() {
        // given
        when(orderTableDao.save(주문_테이블_1)).thenReturn(주문_테이블_1);

        // when
        OrderTable 등록된_주문_테이블 = tableService.create(주문_테이블_1);

        // then
        assertThat(등록된_주문_테이블).isEqualTo(주문_테이블_1);
    }

    @Test
    @DisplayName("주문 테이블 목록 조회")
    void list() {
        // given
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).hasSize(2);
        assertThat(orderTables).contains(주문_테이블_1, 주문_테이블_2);
    }

    @Test
    @DisplayName("주문 테이블이 비었는지 여부 변경")
    void changeEmpty() {
        // given
        when(orderTableDao.findById(any())).thenReturn(Optional.of(주문_테이블_1));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(false);
        when(orderTableDao.save(주문_테이블_1)).thenReturn(주문_테이블_1);

        // when
        tableService.changeEmpty(주문_테이블_1.getId(), 주문_테이블_2);

        // then
        assertThat(주문_테이블_1.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("주문 테이블에 방문한 손님 수를 변경")
    void changeNumberOfGuests() {
        // given
        when(orderTableDao.findById(any())).thenReturn(Optional.of(주문_테이블_2));
        when(orderTableDao.save(주문_테이블_2)).thenReturn(주문_테이블_2);

        // when
        tableService.changeNumberOfGuests(주문_테이블_2.getId(), 주문_테이블_3);

        // then
        assertThat(주문_테이블_2.getNumberOfGuests()).isEqualTo(주문_테이블_3.getNumberOfGuests());
    }
}
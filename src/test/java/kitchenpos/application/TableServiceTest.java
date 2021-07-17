package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@SpringBootTest
class TableServiceTest {

    @MockBean
    private OrderDao orderDao;
    @MockBean
    private OrderTableDao orderTableDao;

    private TableService tableService;

    private OrderTable 테이블_1번, 테이블_2번;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);

        테이블_1번 = new OrderTable();
        테이블_1번.setId(1L);
        테이블_1번.setEmpty(true);

        테이블_2번 = new OrderTable();
        테이블_2번.setId(2L);
        테이블_2번.setEmpty(false);
        테이블_2번.setNumberOfGuests(2);
    }

    @DisplayName("테이블 생성")
    @Test
    void create() {
        when(orderTableDao.save(테이블_1번)).thenReturn(테이블_1번);
        assertThat(tableService.create(테이블_1번)).isEqualTo(테이블_1번);
    }

    @DisplayName("테이블 전체 조회")
    @Test
    void list() {
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(테이블_1번, 테이블_2번));
        assertThat(tableService.list()).contains(테이블_1번, 테이블_2번);
    }

    @DisplayName("빈테이블로 상태 변경")
    @Test
    void changeEmpty() {
        when(orderTableDao.findById(테이블_1번.getId())).thenReturn(Optional.of(테이블_1번));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(테이블_1번.getId(),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
        when(orderTableDao.save(테이블_1번)).thenReturn(테이블_1번);

        assertThat(tableService.changeEmpty(테이블_1번.getId(), 테이블_1번)).isEqualTo(테이블_1번);
    }

    @DisplayName("테이블 인원 수 변경")
    @Test
    void changeNumberOfGuests() {
        when(orderTableDao.findById(테이블_2번.getId())).thenReturn(Optional.of(테이블_2번));
        when(orderTableDao.save(테이블_2번)).thenReturn(테이블_2번);

        assertThat(tableService.changeNumberOfGuests(테이블_2번.getId(), 테이블_2번)).isEqualTo(테이블_2번);
    }
}
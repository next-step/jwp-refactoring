package kitchenpos.application;

import kitchenpos.dao.OrderTableDao;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    private OrderTable table;
    private OrderTable table2;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @BeforeEach
    void setUp() {
        table = new OrderTable();
        table.setId(1L);
        table.setNumberOfGuests(0);
        table.setEmpty(true);

        table2 = new OrderTable();
        table2.setId(2L);
        table2.setNumberOfGuests(0);
        table2.setEmpty(true);
    }

    @DisplayName("테이블 정보를 등록한다")
    @Test
    void create() {
        // given
        given(orderTableDao.save(table)).willReturn(table);

        // when
        OrderTable actual = tableService.create(table);

        // then
        assertThat(actual).isEqualTo(table);
    }

    @DisplayName("등록한 테이블 목록을 조회한다")
    @Test
    void findAll() {
        // given
        given(orderTableDao.findAll()).willReturn(Arrays.asList(table, table2));

        // when
        List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual).isEqualTo(Arrays.asList(table, table2));
    }
}
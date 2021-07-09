package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TableRestControllerTest {

    private OrderTable table;
    private OrderTable table2;
    private OrderTable table3;
    private OrderTable table4;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
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

        table3 = new OrderTable();
        table3.setId(3L);
        table3.setNumberOfGuests(0);
        table3.setEmpty(false);

        table4 = new OrderTable();
        table4.setId(4L);
        table4.setNumberOfGuests(2);
        table4.setEmpty(false);
    }

    @DisplayName("테이블 정보를 등록한다")
    @Test
    void create() {
        // given
        given(tableService.create(any())).willReturn(table);

        // when
        OrderTable actual = testRestTemplate.postForObject("/api/tables", table, OrderTable.class);

        // then
        assertThat(actual.getNumberOfGuests()).isZero();
        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("등록한 테이블 목록을 조회한다")
    @Test
    void list() {
        // given
        given(tableService.list()).willReturn(Arrays.asList(table, table2, table3, table4));

        // when
        OrderTable[] tables = testRestTemplate.getForObject("/api/tables", OrderTable[].class);

        // then
        long[] actual = Arrays.stream(tables)
                .mapToLong(OrderTable::getId)
                .toArray();
        assertThat(actual).isEqualTo(new long[]{1L, 2L, 3L, 4L});
    }

}
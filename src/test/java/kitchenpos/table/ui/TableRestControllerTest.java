package kitchenpos.table.ui;

import com.navercorp.fixturemonkey.FixtureMonkey;
import kitchenpos.ControllerTest;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TableRestController.class)
public class TableRestControllerTest extends ControllerTest {
    @MockBean
    private TableService tableService;

    @DisplayName("주문테이블 생성을 요청하면 생성된 주문테이블응답")
    @Test
    public void returnTable() throws Exception {
        OrderTable orderTable = getOrderTable();
        doReturn(orderTable).when(tableService).create(any(OrderTable.class));

        webMvc.perform(post("/api/tables")
                        .content(mapper.writeValueAsString(new OrderTable()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(orderTable.getId().intValue())))
                .andExpect(jsonPath("$.tableGroupId", is(orderTable.getTableGroupId().intValue())))
                .andExpect(jsonPath("$.numberOfGuests", is(orderTable.getNumberOfGuests())))
                .andExpect(jsonPath("$.empty", is(true)))
                .andExpect(status().isCreated());
    }

    @DisplayName("주문테이블생성을 요청하면 주문생성 실패응답")
    @Test
    public void throwsExceptionWhenTableCreate() throws Exception {
        doThrow(new IllegalArgumentException()).when(tableService).create(any(OrderTable.class));

        webMvc.perform(post("/api/tables")
                        .content(mapper.writeValueAsString(new OrderTable()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("주문테이블 목록을 요청하면 주문테이블 목록을 응답")
    @Test
    public void returnTables() throws Exception {
        List<OrderTable> orderTables = FixtureMonkey.create()
                .giveMeBuilder(OrderTable.class)
                .sampleList(Arbitraries.integers().between(1, 50).sample());
        doReturn(orderTables).when(tableService).list();

        webMvc.perform(get("/api/tables"))
                .andExpect(jsonPath("$", hasSize(orderTables.size())))
                .andExpect(status().isOk());
    }

    private OrderTable getOrderTable() {
        return FixtureMonkey.create()
                .giveMeBuilder(OrderTable.class)
                .set("id", Arbitraries.longs().between(1, 100))
                .set("tableGroupId", Arbitraries.longs().between(1, 100))
                .set("numberOfGuests", Arbitraries.integers().between(2, 5))
                .set("empty", true)
                .sample();
    }
}


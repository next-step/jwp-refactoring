package kitchenpos.table.ui;

import com.navercorp.fixturemonkey.FixtureMonkey;
import kitchenpos.ControllerTest;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
@MockBean(JpaMetamodelMappingContext.class)
public class TableRestControllerTest extends ControllerTest {
    @MockBean
    private TableService tableService;

    @DisplayName("주문테이블 생성을 요청하면 생성된 주문테이블응답")
    @Test
    public void returnTable() throws Exception {
        OrderTableResponse orderTable = getOrderTableResponse();
        doReturn(orderTable).when(tableService).create(any(OrderTableRequest.class));

        webMvc.perform(post("/api/tables")
                .content(mapper.writeValueAsString(new OrderTableRequest()))
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
        doThrow(new IllegalArgumentException()).when(tableService).create(any(OrderTableRequest.class));

        webMvc.perform(post("/api/tables")
                .content(mapper.writeValueAsString(new OrderTableRequest()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("주문테이블 목록을 요청하면 주문테이블 목록을 응답")
    @Test
    public void returnTables() throws Exception {
        List<OrderTableResponse> orderTables = getOrderTables(OrderTable.builder().id(13l)
                .tableGroup(TableGroup.builder().build()).build(), 100);
        doReturn(orderTables).when(tableService).list();

        webMvc.perform(get("/api/tables"))
                .andExpect(jsonPath("$", hasSize(orderTables.size())))
                .andExpect(status().isOk());
    }

    private OrderTableResponse getOrderTableResponse() {
        return FixtureMonkey.create()
                .giveMeBuilder(OrderTableResponse.class)
                .set("id", Arbitraries.longs().between(1, 100))
                .set("tableGroupId", Arbitraries.longs().between(1, 100))
                .set("numberOfGuests", Arbitraries.integers().between(2, 5))
                .set("empty", true)
                .sample();
    }

    private List<OrderTableResponse> getOrderTables(OrderTable orderTable, int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(value -> OrderTableResponse.of(orderTable))
                .collect(Collectors.toList());
    }
}

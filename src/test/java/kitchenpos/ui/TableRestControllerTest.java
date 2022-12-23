package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import kitchenpos.OrderTableBuilder;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest extends ControllerTest {
    @MockBean
    private TableService tableService;

    private OrderTable emptyOrderTable;
    private OrderTable nonEmptyOrderTable;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();

        emptyOrderTable = OrderTableBuilder.emptyOrderTableWithIdAndGuestNo(1L, 2);
        nonEmptyOrderTable = OrderTableBuilder.nonEmptyOrderTableWithIdAndGuestNo(2L, 4);
    }

    @DisplayName("[POST] 주문테이블 생성")
    @Test
    void create() throws Exception {
        given(tableService.create(any(OrderTable.class))).willReturn(emptyOrderTable);

        perform(postAsJson("/api/tables", emptyOrderTable))
            .andExpect(status().isCreated())
            .andExpect(header().string("location", "/api/tables/1"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.tableGroupId").isEmpty())
            .andExpect(jsonPath("$.numberOfGuests").value(2))
            .andExpect(jsonPath("$.empty").value(true));
    }

    @DisplayName("[GET] 주문테이블 목록 조회")
    @Test
    void list() throws Exception {
        given(tableService.list()).willReturn(Arrays.asList(emptyOrderTable, nonEmptyOrderTable));

        perform(get("/api/tables"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].tableGroupId").isEmpty())
            .andExpect(jsonPath("$[0].numberOfGuests").value(2))
            .andExpect(jsonPath("$[0].empty").value(true))
            .andExpect(jsonPath("$[1].id").value(2L))
            .andExpect(jsonPath("$[1].tableGroupId").isEmpty())
            .andExpect(jsonPath("$[1].numberOfGuests").value(4))
            .andExpect(jsonPath("$[1].empty").value(false));
    }

    @DisplayName("[PUT] 주문테이블 비어있는 상태 변경")
    @Test
    void changeEmptyTable() throws Exception {
        given(tableService.changeEmpty(anyLong(), any(OrderTable.class))).willReturn(emptyOrderTable);

        perform(putAsJson("/api/tables/1/empty", nonEmptyOrderTable))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.empty").value(true));
    }

    @DisplayName("[PUT] 주문테이블 방문손님수 변경")
    @Test
    void changeNumberOfGuestsTable() throws Exception {
        given(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class))).willReturn(emptyOrderTable);

        perform(putAsJson("/api/tables/1/number-of-guests", nonEmptyOrderTable))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.numberOfGuests").value(2));
    }


}

package kitchenpos.ui;

import static kitchenpos.OrderTableBuilder.emptyOrderTableWithGuestNo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest extends ControllerTest {
    @MockBean
    private TableGroupService tableGroupService;

    private TableGroup tableGroup;
    private OrderTable emptyOrderTable1;
    private OrderTable emptyOrderTable2;

    @BeforeEach
    protected void setUp() {
        super.setUp();

        emptyOrderTable1 = emptyOrderTableWithGuestNo(1L, 2);
        emptyOrderTable2 = emptyOrderTableWithGuestNo(2L, 4);
        tableGroup = new TableGroup(1L, Arrays.asList(emptyOrderTable1, emptyOrderTable2));
    }

    @DisplayName("[POST] 단체테이블 생성")
    @Test
    void createTableGroup() throws Exception {
        given(tableGroupService.create(any(TableGroup.class))).willReturn(tableGroup);

        perform(postAsJson("/api/table-groups", tableGroup))
            .andExpect(status().isCreated())
            .andExpect(header().string("location", "/api/table-groups/1"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.orderTables").isNotEmpty())
            .andExpect(jsonPath("orderTables[0].id").value(1L))
            .andExpect(jsonPath("orderTables[0].tableGroupId").isEmpty())
            .andExpect(jsonPath("orderTables[0].numberOfGuests").value(2))
            .andExpect(jsonPath("orderTables[0].empty").value(true))
            .andExpect(jsonPath("orderTables[1].id").value(2L))
            .andExpect(jsonPath("orderTables[1].tableGroupId").isEmpty())
            .andExpect(jsonPath("orderTables[1].numberOfGuests").value(4))
            .andExpect(jsonPath("orderTables[1].empty").value(true));
    }

    @DisplayName("[DELETE] 단체테이블 해제")
    @Test
    void ungroupTables() throws Exception {
        perform(delete("/api/table-groups/1")).andExpect(status().isNoContent());
    }


}

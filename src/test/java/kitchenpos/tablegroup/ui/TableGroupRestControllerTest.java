package kitchenpos.tablegroup.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹을 지정해서 등록할 수 있다.")
    @Test
    void create() throws Exception {
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(new OrderTable(1L, null, 2, true));
        orderTables.add(new OrderTable(2L, null, 3, true));
        OrderTables tables = new OrderTables(orderTables);
        TableGroup tableGroup = new TableGroup(1L, tables);
        TableGroupRequest request = new TableGroupRequest(orderTables);
        String params = mapper.writeValueAsString(request);
        given(tableGroupService.create(any())).willReturn(TableGroupResponse.from(tableGroup));

        mockMvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(params))
                .andExpect(status().isCreated());
    }

    @DisplayName("테이블 그룹을 지정해서 해제할 수 있다.")
    @Test
    void ungroup() throws Exception {
        Long tableGroupId = 1L;
        doNothing().when(tableGroupService).ungroup(anyLong());

        mockMvc.perform(delete("/api/table-groups/" + tableGroupId))
                .andExpect(status().isNoContent());
    }
}

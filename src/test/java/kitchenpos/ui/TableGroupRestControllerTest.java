package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
        String params = mapper.writeValueAsString(tableGroup);
        given(tableGroupService.create(any())).willReturn(tableGroup);

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

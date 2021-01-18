package kitchenpos.table.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.service.TableGroupService;
import kitchenpos.table.util.TableGroupRequestBuilder;
import kitchenpos.table.util.TableGroupResponseBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("테이블을 그룹화 할 수 있다.")
    @Test
    void createTableGroup() throws Exception {
        TableGroupRequest tableGroupRequest = new TableGroupRequestBuilder()
                .addOrderTable(1L)
                .addOrderTable(2L)
                .build();

        TableGroupResponse tableGroupResponse =
                new TableGroupResponseBuilder()
                        .withId(1L)
                        .addOrderTable(1L, 4, true)
                        .addOrderTable(2L, 4, true)
                        .build();

        when(tableGroupService.create(any())).thenReturn(tableGroupResponse);


        mockMvc.perform(post("/api/table-groups")
                .content(objectMapper.writeValueAsString(tableGroupRequest)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("테이블 그룹을 삭제 할 수 있다.")
    @Test
    void ungroupTable() throws Exception {
        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 1L))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
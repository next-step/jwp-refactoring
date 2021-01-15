package kitchenpos.table.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.service.TableGroupServiceJpa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

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
    private TableGroupServiceJpa tableGroupServiceJpa;

    @DisplayName("테이블을 그룹화 할 수 있다.")
    @Test
    void createTableGroup() throws Exception {
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Arrays.asList(
                        new OrderTableIdRequest(1L),
                        new OrderTableIdRequest(2L)
                ));

        TableGroupResponse tableGroupResponse = new TableGroupResponse(1L, Arrays.asList(
                new OrderTableResponse(1L, 4, true),
                new OrderTableResponse(1L, 4, true)));

        when(tableGroupServiceJpa.create(any())).thenReturn(tableGroupResponse);


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
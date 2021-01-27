package kitchenpos.table.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private TableGroupService tableGroupService;


    @DisplayName("테이블그룹 등록")
    @Test
    public void create() throws Exception {
        OrderTableResponse table1 = new OrderTableResponse(1L, 3, true);
        OrderTableResponse table2 = new OrderTableResponse(2L, 4, false);
        List<OrderTableResponse> orderTables = Arrays.asList(table1, table2);
        TableGroupResponse expectedTableGroup = new TableGroupResponse(1L, orderTables);
        given(tableGroupService.create(any())).willReturn(expectedTableGroup);

        mockMvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJsonString(expectedTableGroup)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("테이블그룹 해제")
    @Test
    public void ungroup() throws Exception {
        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 1L))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    private String makeJsonString(Object request) throws JsonProcessingException {
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(request);
    }
}
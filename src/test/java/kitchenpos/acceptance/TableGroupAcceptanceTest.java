package kitchenpos.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TableGroupAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("테이블그룹 등록")
    @Test
    public void createTableGroup() throws Exception {
        OrderTableResponse table1 = new OrderTableResponse(1L, 1L,3, true);
        OrderTableResponse table2 = new OrderTableResponse(2L, 1L,4, false);
        List<OrderTableResponse> orderTables = Arrays.asList(table1, table2);
        TableGroupResponse expectedTableGroup = new TableGroupResponse(1L, LocalDateTime.now(), orderTables);
        given(tableGroupService.create(any())).willReturn(expectedTableGroup);

        mockMvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(expectedTableGroup)))
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

    private String serialize(Object request) throws JsonProcessingException {
        mapper = new ObjectMapper();
        return mapper.writeValueAsString(request);
    }
}

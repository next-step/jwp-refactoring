package kitchenpos.table.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.config.MockMvcTestConfig;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TableGroupRestController.class)
@MockMvcTestConfig
class TableGroupRestControllerTest {
    private static final String TABLE_GROUP_API_URI = "/api/table-groups";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 등록할 수 있다.")
    @Test
    void createTest() throws Exception {
        // given
        OrderTableResponse orderTableResponse1 = new OrderTableResponse(1L, 1L, 1, false);
        OrderTableResponse orderTableResponse2 = new OrderTableResponse(2L, 1L, 1, false);
        TableGroupResponse tableGroup = new TableGroupResponse(1L, LocalDateTime.now(), Arrays.asList(orderTableResponse1, orderTableResponse2));
        given(tableGroupService.create(any())).willReturn(tableGroup);

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(1L, 2L));

        // when
        ResultActions actions = mockMvc.perform(post(TABLE_GROUP_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroupRequest)));

        // then
        actions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("location", TABLE_GROUP_API_URI + "/1"));
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void ungroupTest() throws Exception {
        // when
        ResultActions actions = mockMvc.perform(delete(TABLE_GROUP_API_URI + "/{tableGroupId}", 1L));

        // then
        actions.andDo(print())
                .andExpect(status().isNoContent());
    }
}

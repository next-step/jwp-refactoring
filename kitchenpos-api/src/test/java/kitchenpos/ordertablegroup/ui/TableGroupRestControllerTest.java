package kitchenpos.ordertablegroup.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertablegroup.application.OrderTableGroupService;
import kitchenpos.ordertablegroup.dto.OrderTableGroupResponse;
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
    private OrderTableGroupService orderTableGroupService;

    @DisplayName("테이블 그룹을 등록한다.")
    @Test
    void create() throws Exception {
        List<OrderTableResponse> orderTables = Arrays.asList(
            new OrderTableResponse(1L, 1L, 10, false),
            new OrderTableResponse(2L, 1L, 5, false)
        );
        OrderTableGroupResponse tableGroup = new OrderTableGroupResponse(1L, null, orderTables);

        when(orderTableGroupService.create(any())).thenReturn(tableGroup);

        mockMvc.perform(post("/api/table-groups")
            .content(objectMapper.writeValueAsString(tableGroup)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() throws Exception {
        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 1L))
            .andDo(print())
            .andExpect(status().isNoContent());
    }
}

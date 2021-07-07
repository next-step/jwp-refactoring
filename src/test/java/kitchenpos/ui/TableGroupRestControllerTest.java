package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.ui.TableGroupRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TableGroupService tableGroupService;

    OrderTableRequest orderTableRequest1;
    OrderTableRequest orderTableRequest2;
    OrderTableResponse orderTableResponse1;
    OrderTableResponse orderTableResponse2;


    @BeforeEach
    void setUp() {
        orderTableRequest1 = new OrderTableRequest(1L, 0, true);

        orderTableRequest2 = new OrderTableRequest(1L, 0, true);

        orderTableResponse1 = new OrderTableResponse();
        orderTableResponse1.setTableGroupId(1L);
        orderTableResponse1.setEmpty(true);
        orderTableResponse1.setNumberOfGuests(0);

        orderTableResponse2 = new OrderTableResponse();
        orderTableResponse2.setTableGroupId(1L);
        orderTableResponse2.setEmpty(true);
        orderTableResponse2.setNumberOfGuests(0);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("테이블 그룹 생성 Api 테스트")
    @Test
    void create() throws Exception {
        TableGroupRequest tableGroup = new TableGroupRequest(LocalDateTime.now(), Arrays.asList(orderTableRequest1, orderTableRequest2));

        String requestBody = objectMapper.writeValueAsString(tableGroup);

        TableGroupResponse responseTableGroup = new TableGroupResponse();
        responseTableGroup.setOrderTables(Arrays.asList(orderTableResponse1, orderTableResponse2));
        responseTableGroup.setCreatedDate(LocalDateTime.now());
        String responseBody = objectMapper.writeValueAsString(responseTableGroup);

        when(tableGroupService.create(any())).thenReturn(responseTableGroup);
        mockMvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(responseBody))
        ;
    }

    @DisplayName("테이블 그룹 해제 Api 테스트")
    @Test
    void ungroup() throws Exception {
        Long tableGroupId = 1L;
        mockMvc.perform(delete("/api/table-groups/" + tableGroupId)
        )
                .andDo(print())
                .andExpect(status().isNoContent())
        ;
    }
}

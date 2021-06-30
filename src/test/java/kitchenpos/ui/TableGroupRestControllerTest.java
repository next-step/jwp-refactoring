package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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

import static org.junit.jupiter.api.Assertions.*;
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

    OrderTable orderTable1;
    OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable();
        orderTable1.setTableGroupId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setNumberOfGuests(0);

        orderTable2 = new OrderTable();
        orderTable2.setTableGroupId(1L);
        orderTable2.setEmpty(true);
        orderTable2.setNumberOfGuests(0);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("테이블 그룹 생성 Api 테스트")
    @Test
    void create() throws Exception {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
        tableGroup.setCreatedDate(LocalDateTime.now());

        String requestBody = objectMapper.writeValueAsString(tableGroup);

        TableGroup responseTableGroup = new TableGroup();
        responseTableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
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
        mockMvc.perform(delete("/api/table-groups/"+tableGroupId)
        )
                .andDo(print())
                .andExpect(status().isNoContent())
        ;
    }
}

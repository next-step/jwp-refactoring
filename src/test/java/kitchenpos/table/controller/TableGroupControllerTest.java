package kitchenpos.table.controller;

import kitchenpos.common.ControllerTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TableGroupControllerTest extends ControllerTest {

    private TableGroup tableGroup;

    @BeforeEach
    public void setup() {
        List<OrderTable> orderTables = new ArrayList<>();
        OrderTable orderTable = new OrderTable(1L, 0, false);
        OrderTable orderTable2 = new OrderTable(2L, 0, false);
        orderTables.add(orderTable);
        orderTables.add(orderTable2);
        tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
    }

    @Test
    @DisplayName("단체를 지정 한다")
    public void createOrderTableGroup() throws Exception {
        단체_지정_요청(tableGroup)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("$.orderTables", hasSize(2)));
    }

    @Test
    @DisplayName("단체 지정을 해제 한다")
    public void deleteOrderTableGroup() throws Exception {
        단체_지정_요청(tableGroup);

        단체_지정_해제_요청()
            .andExpect(status().isNoContent());
    }

    private ResultActions 단체_지정_요청(TableGroup tableGroup) throws Exception {
        return mockMvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroup)))
                .andDo(print());
    }

    private ResultActions 단체_지정_해제_요청() throws Exception {
        return mockMvc.perform(delete("/api/table-groups/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

}

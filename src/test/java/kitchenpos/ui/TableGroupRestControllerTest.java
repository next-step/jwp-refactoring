package kitchenpos.ui;

import kitchenpos.common.BaseControllerTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("주문 테이블 그룹 관련 기능")
public class TableGroupRestControllerTest extends BaseControllerTest {

    @DisplayName("주문 테이블 그룹 등록")
    @Test
    void testCreateTableGroup() throws Exception {
        // given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        // when & then
        mockMvc.perform(post("/api/table-groups")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroup)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.createdDate").isNotEmpty())
                .andExpect(jsonPath("$.orderTables", hasSize(2)));
    }

    @DisplayName("주문 테이블 그룹 해제")
    @Test
    void testUngroupTableGroup() throws Exception {
        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", 1))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}

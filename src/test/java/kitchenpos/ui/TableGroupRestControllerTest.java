package kitchenpos.ui;

import kitchenpos.common.BaseControllerTest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.common.Fixtures.tableGroupRequest;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("주문 테이블 그룹 관련 기능")
public class TableGroupRestControllerTest extends BaseControllerTest {

    @DisplayName("테이블 그룹 관리")
    @Test
    void testManageTableGroup() throws Exception {
        Long 그룹화_ID = 비어있는_주문_테이블_그룹화_가능(Arrays.asList(1L, 2L));

        주문_테이블_그룹_해제_성공(그룹화_ID);
    }

    @DisplayName("존재하지 않는 주문 테이블을 포함하여 그룹 등록")
    @Test
    void testCreateTableGroup_withNonExistentOrderTable() throws Exception {
        // given
        TableGroupRequest request = tableGroupRequest(Arrays.asList(0L, 1L));

        // when & then
        mockMvc.perform(post("/api/table-groups")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    Long 비어있는_주문_테이블_그룹화_가능(List<Long> tableIds) throws Exception {
        // given
        TableGroupRequest tableGroupRequest = tableGroupRequest(tableIds);

        // when & then
        MvcResult mvcResult = mockMvc.perform(post("/api/table-groups")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroupRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.createdDate").isNotEmpty())
                .andExpect(jsonPath("$.orderTables", hasSize(2)))
                .andReturn();

        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TableGroupResponse.class)
                .getId();
    }

    void 주문_테이블_그룹_해제_성공(Long tableGroupId) throws Exception {
        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", tableGroupId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}

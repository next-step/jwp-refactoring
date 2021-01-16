package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import kitchenpos.dto.TableGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-09
 */
class TableGroupRestControllerTest extends BaseControllerTest {

    @DisplayName("테이블 그룹 생성 테스트")
    @Test
    void tableGroupCreateTest() throws Exception {
        TableGroupCreateRequest createRequest = new TableGroupCreateRequest(Arrays.asList(3L, 4L));

        mockMvc.perform(post("/api/table-groups")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists());
    }

    @DisplayName("테이블 그룹 해제 테스트")
    @Test
    void tableGroupUngroupTest() throws Exception {
        TableGroupCreateRequest createRequest = new TableGroupCreateRequest(Arrays.asList(5L, 6L));

        String location = mockMvc.perform(post("/api/table-groups")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        mockMvc.perform(delete(location))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}

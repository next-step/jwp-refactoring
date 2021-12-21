package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.testfixture.CommonTestFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

class TableGroupRestControllerTest extends IntegrationTest {

    private static final String BASE_PATH = "/api/table-groups";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹 등록")
    @Test
    void create() throws Exception {
        //given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L),
            new OrderTable(2L));
        TableGroup requestTableGroup = new TableGroup(orderTables);
        TableGroup expectedTableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
        given(tableGroupService.create(any()))
            .willReturn(expectedTableGroup);

        //when, then
        mockMvc.perform(post(BASE_PATH)
                .content(CommonTestFixtures.asJsonString(requestTableGroup))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(expectedTableGroup.getId()));
    }

    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() throws Exception {
        //given
        Long tableGroupId = 1L;

        //when,then
        mockMvc.perform(delete(BASE_PATH + "/{tableGroupId}", tableGroupId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}

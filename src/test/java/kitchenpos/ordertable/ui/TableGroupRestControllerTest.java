package kitchenpos.ordertable.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.CommonTestFixtures;
import kitchenpos.ordertable.application.TableGroupService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.dto.TableGroupRequest;
import kitchenpos.ordertable.dto.TableGroupResponse;
import kitchenpos.ordertable.testfixtures.TableGroupTestFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {

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
            new OrderTable(1L, 1, true),
            new OrderTable(2L, 2, true));
        TableGroupRequest requestTableGroup = TableGroupTestFixtures.convertToTableGroupRequest(
            orderTables);

        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        tableGroup.groupTables(orderTables);
        TableGroupResponse expectedTableGroup = TableGroupResponse.from(
            tableGroup);

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

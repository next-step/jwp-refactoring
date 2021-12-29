package kitchenpos.tablegroup.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.common.CommonTestFixtures;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import kitchenpos.tablegroup.TableGroupTestFixtures;
import kitchenpos.ordertable.vo.NumberOfGuests;

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
            new OrderTable(1L, new NumberOfGuests(1), true),
            new OrderTable(2L, new NumberOfGuests(2), true));
        TableGroupRequest requestTableGroup = TableGroupTestFixtures.convertToTableGroupRequest(
            orderTables);

        TableGroup tableGroup = new TableGroup(1L);
        TableGroupResponse expectedTableGroup = TableGroupResponse.from(tableGroup);

        given(tableGroupService.create(any()))
            .willReturn(expectedTableGroup);

        //when, then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH)
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
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_PATH + "/{tableGroupId}", tableGroupId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}

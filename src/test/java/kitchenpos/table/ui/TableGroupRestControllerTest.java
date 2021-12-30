package kitchenpos.table.ui;

import kitchenpos.common.ui.RestControllerTest;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest extends RestControllerTest {

    private static final String API_TABLE_GROUP_ROOT = "/api/table-groups";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TableGroupService tableGroupService;

    TableGroupRequest 단체_지정_요청;
    List<OrderTableRequest> orderTables;
    OrderTableRequest firstOrderTable;
    OrderTableRequest secondOrderTable;

    @BeforeEach
    void setUp() {
        firstOrderTable = OrderTableRequest.of(2, true);
        secondOrderTable = OrderTableRequest.of(2, true);
        orderTables = Arrays.asList(firstOrderTable, secondOrderTable);
        단체_지정_요청 = TableGroupRequest.from(Arrays.asList(firstOrderTable, secondOrderTable));
    }

    @Test
    void 단체_지정() throws Exception {
        // given
        OrderTable firstOrderTable = OrderTable.of(2, true);
        OrderTable secondOrderTable = OrderTable.of(2, true);
        TableGroup tableGroup = TableGroup.from();

        given(tableGroupService.create(any())).willReturn(TableGroupResponse.from(tableGroup, Arrays.asList(firstOrderTable, secondOrderTable)));

        // when
        ResultActions actions = mockMvc.perform(post(API_TABLE_GROUP_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(단체_지정_요청)))
                .andDo(print());

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderTables[0].numberOfGuests").value(2))
                .andExpect(jsonPath("$.orderTables[1].numberOfGuests").value(2));
    }

    @Test
    void 단체_지정_해제() throws Exception {
        // given - when
        ResultActions actions = mockMvc.perform(delete(API_TABLE_GROUP_ROOT + "/" + 1L))
                .andDo(print());

        // then
        actions
                .andExpect(status().isNoContent());
    }

}

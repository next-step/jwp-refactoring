package kitchenpos.order.ui;

import kitchenpos.common.fixtrue.OrderTableFixture;
import kitchenpos.common.fixtrue.TableGroupFixture;
import kitchenpos.common.ui.RestControllerTest;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
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
        firstOrderTable.group(1L);
        OrderTable secondOrderTable = OrderTable.of(2, true);
        secondOrderTable.group(1L);
        TableGroup tableGroup = TableGroup.from(
                Arrays.asList(firstOrderTable, secondOrderTable));

        given(tableGroupService.create(any())).willReturn(TableGroupResponse.from(tableGroup));

        // when
        ResultActions actions = mockMvc.perform(post(API_TABLE_GROUP_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(단체_지정_요청)))
                .andDo(print());

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderTables[0].tableGroupId").value(1L))
                .andExpect(jsonPath("$.orderTables[1].tableGroupId").value(1L));
    }

    @Test
    void 단체_지정_해제() throws Exception {
        // given
        OrderTable firstOrderTable = OrderTableFixture.of(2, true);
        firstOrderTable.group(null);
        OrderTable secondOrderTable = OrderTableFixture.of(2, true);
        secondOrderTable.group(null);
        TableGroup tableGroup = TableGroupFixture.from(
                Arrays.asList(firstOrderTable, secondOrderTable));

        // when
        ResultActions actions = mockMvc.perform(delete(API_TABLE_GROUP_ROOT + "/" + 1L))
                .andDo(print());

        // then
        actions
                .andExpect(status().isNoContent());
    }

}

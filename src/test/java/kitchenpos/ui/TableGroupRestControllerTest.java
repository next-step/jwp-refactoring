package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import kitchenpos.common.fixtrue.OrderTableFixture;
import kitchenpos.common.fixtrue.TableGroupFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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

    @Test
    void 단체_지정() throws Exception {
        // given
        TableGroup tableGroup = TableGroupFixture.of(1L,
                OrderTableFixture.of(1L, 1L, 2, true),
                OrderTableFixture.of(2L, 1L, 4, true));

        given(tableGroupService.create(any())).willReturn(tableGroup);

        // when
        ResultActions actions = mockMvc.perform(post(API_TABLE_GROUP_ROOT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(tableGroup)))
                .andDo(print());

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderTables[0].tableGroupId").value(tableGroup.getId()))
                .andExpect(jsonPath("$.orderTables[1].tableGroupId").value(tableGroup.getId()));
    }

    @Test
    void 단체_지정_해제() throws Exception {
        // given
        TableGroup tableGroup = TableGroupFixture.of(1L,
                OrderTableFixture.of(1L, null, 2, true),
                OrderTableFixture.of(2L, null, 4, true));

        // when
        ResultActions actions = mockMvc.perform(delete(API_TABLE_GROUP_ROOT + "/" + tableGroup.getId()))
                .andDo(print());

        // then
        actions
                .andExpect(status().isNoContent());
    }

}

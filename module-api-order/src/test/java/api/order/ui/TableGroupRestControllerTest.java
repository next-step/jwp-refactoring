package api.order.ui;

import api.order.dto.OrderTableGroupRequest;
import api.order.dto.OrderTableGroupResponse;
import api.order.dto.OrderTableRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("테이블 그룹 통합테스트")
class TableGroupRestControllerTest extends IntegrationSupport {
    private static final String URI = "/api/table-groups";

    @DisplayName("특정 테이블 그룹을 삭제한다.")
    @Test
    void ungroup() throws Exception {
        //given
        OrderTableGroupResponse orderTableGroupResponse = toObject(mockMvc.perform(postAsJson(URI, OrderTableGroupRequest.of(
                Lists.list(
                        OrderTableRequest.of(1L, 0, false),
                        OrderTableRequest.of(2L, 0, false)
                )
        ))).andReturn(), OrderTableGroupResponse.class);

        //when
        ResultActions actions = mockMvc.perform(delete(URI + "/" + orderTableGroupResponse.getId()));

        //then
        actions.andExpect(status().isNoContent());
    }

    @DisplayName("테이블 그룹을 추가한다.")
    @Test
    void create() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(postAsJson(URI, OrderTableGroupRequest.of(
                Lists.list(
                        OrderTableRequest.of(1L, 0, false),
                        OrderTableRequest.of(2L, 0, false)
                )
        )));

        //then
        actions.andExpect(status().isCreated());
    }
}

package kitchenpos.ui;

import kitchenpos.tablegroup.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("단체 지정 Controller Test")
class TableGroupRestControllerTest extends RestControllerTest {

    public static final String TABLE_GROUPS_URL = "/api/table-groups";

    @DisplayName("복수의 주문 테이블을 단체 지정할 수 있다.")
    @Test
    void create() throws Exception {
        //given
        TableGroupRequest tableGroup = new TableGroupRequest();
        tableGroup.setOrderTables(
                Arrays.asList(new OrderTableIdRequest(2L), new OrderTableIdRequest(4L))
        );

        //when
        //then
        단체지정요청(tableGroup)
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(redirectedUrlPattern(TABLE_GROUPS_URL + "/*"))
                .andExpect(jsonPath("$.orderTables.length()", is(2)));
    }

    @DisplayName("단체 지정된 것을 풀 수 있다.")
    @Test
    void ungroup() throws Exception {
        //given
        TableGroupRequest tableGroupRequest = new TableGroupRequest();
        tableGroupRequest.setOrderTables(
                Arrays.asList(new OrderTableIdRequest(2L), new OrderTableIdRequest(4L))
        );
        ResultActions resultActions = 단체지정요청(tableGroupRequest);
        String redirectedUrl = getRedirectedUrl(resultActions);

        //when
        //then
        mockMvc.perform(delete(redirectedUrl))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    private ResultActions 단체지정요청(TableGroupRequest tableGroup) throws Exception {
        return mockMvc.perform(
                post(TABLE_GROUPS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(tableGroup))
        )
                .andDo(print());
    }
}

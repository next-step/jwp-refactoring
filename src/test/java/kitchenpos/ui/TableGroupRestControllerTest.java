package kitchenpos.ui;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
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
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(
                Arrays.asList(
                        new OrderTable(2L ,2L, 0, true),
                        new OrderTable(4L ,2L, 0, true)
                )
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
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(
                Arrays.asList(
                        new OrderTable(2L ,2L, 0, true),
                        new OrderTable(4L ,2L, 0, true)
                )
        );
        ResultActions resultActions = 단체지정요청(tableGroup);
        String redirectedUrl = getRedirectedUrl(resultActions);

        //when
        //then
        mockMvc.perform(delete(redirectedUrl))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    private ResultActions 단체지정요청(TableGroup tableGroup) throws Exception {
        return mockMvc.perform(
                post(TABLE_GROUPS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(tableGroup))
        )
                .andDo(print());
    }
}

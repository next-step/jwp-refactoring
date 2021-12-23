package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

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
class TableGroupRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private TableGroupService tableGroupService;

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("통합 계산을 위해 단체 지정 등록한다.")
    @Test
    void create() throws Exception {
        // given
        final OrderTable orderTable1 = new OrderTable(1L, 1L, 5, false);
        final OrderTable orderTable2 = new OrderTable(2L, 1L, 5, false);
        final List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        final TableGroup tableGroup = new TableGroup(1L, null, orderTables);
        given(tableGroupService.create(any())).willReturn(tableGroup);

        // when
        final ResultActions actions = mvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(tableGroup)))
                .andDo(print());
        //then
        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("orderTables").isArray())
                .andExpect(jsonPath("orderTables[0].id").value(1L))
                .andExpect(jsonPath("orderTables[1].id").value(2L));
    }

    @DisplayName("단체 지정을 해지한다.")
    @Test
    void ungroup() throws Exception {
        // given
        final OrderTable orderTable1 = new OrderTable(1L, 1L, 5, false);
        final OrderTable orderTable2 = new OrderTable(2L, 1L, 5, false);
        final List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        final TableGroup tableGroup = new TableGroup(1L, null, orderTables);

        // when
        final ResultActions actions = mvc.perform(delete("/api/table-groups/{tableGroupId}", tableGroup.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        //then
        actions.andExpect(status().isNoContent());
    }
}

package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import org.assertj.core.util.Lists;
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
        final OrderTable 주문테이블1 = OrderTable.of(1L, 2, false);
        final OrderTable 주문테이블2 = OrderTable.of(2L, 2, false);
        final List<OrderTable> 주문테이블_목록 = Lists.newArrayList(주문테이블1, 주문테이블2);
        final TableGroup 테이블그룹 = TableGroup.of(1L, 주문테이블_목록);

        final TableGroupRequest 테이블그룹_요청 = TableGroupRequest.from(Lists.newArrayList(주문테이블1.getId(), 주문테이블2.getId()));
        final TableGroupResponse 테이블그룹_응답 = TableGroupResponse.from(테이블그룹);
        
        given(tableGroupService.create(any())).willReturn(테이블그룹_응답);

        
        final ResultActions actions = mvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(테이블그룹_요청)))
                .andDo(print());
        
        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("orderTableRespons").hasJsonPath())
                .andExpect(jsonPath("createdDate").hasJsonPath());
    }

    @DisplayName("단체 지정을 해지한다.")
    @Test
    void ungroup() throws Exception {
        final OrderTable 주문테이블1 = OrderTable.of(1L, 2, false);
        final OrderTable 주문테이블2 = OrderTable.of(2L, 2, false);
        final List<OrderTable> 주문테이블_목록 = Lists.newArrayList(주문테이블1, 주문테이블2);
        final TableGroup 테이블그룹 = TableGroup.of(1L, 주문테이블_목록);

        final TableGroupRequest 테이블그룹_요청 = TableGroupRequest.from(Lists.newArrayList(주문테이블1.getId(), 주문테이블2.getId()));
        final TableGroupResponse 테이블그룹_응답 = TableGroupResponse.from(테이블그룹);

        final ResultActions actions = mvc.perform(delete("/api/table-groups/{tableGroupId}", 테이블그룹_응답.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        actions.andExpect(status().isNoContent());
    }
}

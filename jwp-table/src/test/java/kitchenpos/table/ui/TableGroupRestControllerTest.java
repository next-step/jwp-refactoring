package kitchenpos.table.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
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
import kitchenpos.table.fixture.TestOrderTableFactory;
import kitchenpos.table.fixture.TestTableGroupFactory;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
        final OrderTable 주문테이블1 = TestOrderTableFactory.주문_테이블_조회됨(1L, 2, false);
        final OrderTable 주문테이블2 = TestOrderTableFactory.주문_테이블_조회됨(2L, 2, false);
        final List<OrderTable> 주문테이블_목록 = Lists.newArrayList(주문테이블1, 주문테이블2);
        final TableGroup 테이블그룹 = TestTableGroupFactory.테이블_그룹_생성됨(1L);

        final TableGroupRequest 테이블그룹_요청 = TestTableGroupFactory.테이블_그룹_요청(Lists.newArrayList(주문테이블1.getId(), 주문테이블2.getId()));
        final TableGroupResponse 테이블그룹_응답 = TestTableGroupFactory.테이블_그룹_응답(테이블그룹, 주문테이블_목록);

        given(tableGroupService.group(any())).willReturn(테이블그룹_응답);

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
        final OrderTable 주문테이블1 = TestOrderTableFactory.주문_테이블_조회됨(1L, 2, false);
        final OrderTable 주문테이블2 = TestOrderTableFactory.주문_테이블_조회됨(2L, 2, false);
        final List<OrderTable> 주문테이블_목록 = Lists.newArrayList(주문테이블1, 주문테이블2);
        final TableGroup 테이블그룹 = TestTableGroupFactory.테이블_그룹_생성됨(1L);

        final TableGroupResponse 테이블그룹_응답 = TableGroupResponse.of(테이블그룹, 주문테이블_목록);

        final ResultActions actions = mvc.perform(delete("/api/table-groups/{tableGroupId}", 테이블그룹_응답.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        actions.andExpect(status().isNoContent());
    }
}

package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.주문테이블_데이터_생성;
import static kitchenpos.fixture.TableGroupFixture.단체_데이터_생성;
import static kitchenpos.fixture.TableGroupFixture.단체_지정_데이터_생성;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TableGroupRestControllerTest extends BaseRestControllerTest {

    @Mock
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TableGroupRestController(tableGroupService)).build();
    }

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() throws Exception {
        //given
        OrderTable table1 = 주문테이블_데이터_생성(1L, null, 4, false);
        OrderTable table2 = 주문테이블_데이터_생성(2L, null, 3, false);
        List<OrderTable> orderTables = Arrays.asList(table1, table2);
        TableGroup request = 단체_지정_데이터_생성(orderTables);
        String requestBody = objectMapper.writeValueAsString(request);

        given(tableGroupService.create(any())).willReturn(단체_데이터_생성(1L));

        //when //then
        mockMvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() throws Exception {
        //given
        Long tableGroupId = 1L;

        //when //then
        mockMvc.perform(delete("/api/table-groups/{tableGroupId}", tableGroupId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
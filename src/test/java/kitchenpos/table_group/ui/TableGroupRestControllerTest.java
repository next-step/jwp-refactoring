package kitchenpos.table_group.ui;

import static kitchenpos.common.fixture.TableGroupFixture.단체_응답_데이터_생성;
import static kitchenpos.common.fixture.TableGroupFixture.단체_지정_데이터_생성;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.common.ui.BaseRestControllerTest;
import kitchenpos.table.dto.TableGroupRequestDto;
import kitchenpos.table_group.application.TableGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
        TableGroupRequestDto request = 단체_지정_데이터_생성(1L, 2L);
        String requestBody = objectMapper.writeValueAsString(request);

        given(tableGroupService.create(any())).willReturn(단체_응답_데이터_생성(1L));

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
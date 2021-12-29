package kitchenpos.tablegroup.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.dto.TableGroupSaveRequest;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static kitchenpos.tablegroup.fixtures.TableGroupFixtures.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName : kitchenpos.ui
 * fileName : TableGroupRestControllerTest
 * author : haedoang
 * date : 2021/12/15
 * description :
 */
@DisplayName("테이블그룹 컨트롤러 테스트")
@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {
    private TableGroupResponse response;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        response = TableGroupResponse.of(new TableGroup());
    }

    @Test
    @DisplayName("테이블들을 단체로 등록한다.")
    public void postTableGroup() throws Exception {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        given(tableGroupService.create(any(TableGroupSaveRequest.class))).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(그룹테이블_그룹요청()))
        ).andDo(print());

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("단체 테이블을 취소한다.")
    public void deleteTableGroup() throws Exception {
        // when
        ResultActions actions = mockMvc.perform(
                delete("/api/table-groups/" + 1L)
        ).andDo(print());

        // then
        actions.andExpect(status().isNoContent());
    }
}

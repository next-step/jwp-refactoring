package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.*;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.dto.TableGroupSaveRequest;
import kitchenpos.fixtures.MenuFixtures;
import kitchenpos.fixtures.MenuProductFixtures;
import kitchenpos.fixtures.TableGroupFixtures;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static kitchenpos.fixtures.MenuGroupFixtures.메뉴그룹;
import static kitchenpos.fixtures.OrderTableFixtures.주문불가_다섯명테이블;
import static kitchenpos.fixtures.OrderTableFixtures.주문불가_두명테이블;
import static kitchenpos.fixtures.ProductFixtures.양념치킨;
import static kitchenpos.fixtures.TableGroupFixtures.*;
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
    private TableGroup tableGroup;
    private TableGroupResponse response;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroup = new TableGroup(Lists.newArrayList(주문불가_다섯명테이블(), 주문불가_두명테이블()));
        response = TableGroupResponse.of(tableGroup);
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
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.orderTables", hasSize(2)));
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

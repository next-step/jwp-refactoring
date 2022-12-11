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

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TableGroupRestController 테스트")
@WebMvcTest(TableGroupRestController.class)
public class TableGroupRestControllerTest {
    @Autowired
    protected MockMvc webMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private TableGroupService tableGroupService;

    private OrderTable 첫번째_주문_테이블;
    private OrderTable 두번째_주문_테이블;
    private TableGroup 우아한형제들_단체그룹;

    @BeforeEach
    public void setUp() {
        첫번째_주문_테이블 = OrderTable.of(1L, null, 4, false);
        두번째_주문_테이블 = OrderTable.of(2L, null, 2, true);
        우아한형제들_단체그룹 = TableGroup.of(1L, null, Arrays.asList(첫번째_주문_테이블, 두번째_주문_테이블));
    }

    @DisplayName("단체 지정 등록에 실패한다.")
    @Test
    void 단체_지정_등록에_실패한다() throws Exception {
        given(tableGroupService.create(any(TableGroup.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(post("/api/table-groups")
                        .content(objectMapper.writeValueAsString(우아한형제들_단체그룹))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("단체 지정 등록에 성공한다.")
    @Test
    void 단체_지정_등록에_성공한다() throws Exception {
        given(tableGroupService.create(any(TableGroup.class))).willReturn(우아한형제들_단체그룹);

        webMvc.perform(post("/api/table-groups")
                        .content(objectMapper.writeValueAsString(우아한형제들_단체그룹))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(우아한형제들_단체그룹.getId().intValue())))
                .andExpect(jsonPath("$.orderTables", hasSize(2)));
    }

    @DisplayName("단체 지정 삭제에 실패한다.")
    @Test
    void 단체_지정_삭제에_실패한다() throws Exception {
        doThrow(new IllegalArgumentException()).when(tableGroupService).ungroup(anyLong());

        webMvc.perform(delete("/api/table-groups/" + 우아한형제들_단체그룹.getId()))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("단체 지정 삭제에 성공한다.")
    @Test
    void 단체_지정_삭제에_성공한다() throws Exception {
        webMvc.perform(delete("/api/table-groups/" + 우아한형제들_단체그룹.getId()))
                .andExpect(status().isNoContent());
    }
}

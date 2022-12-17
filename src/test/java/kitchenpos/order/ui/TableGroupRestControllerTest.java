package kitchenpos.order.ui;

import kitchenpos.ControllerTest;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
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
public class TableGroupRestControllerTest extends ControllerTest {

    @MockBean
    private TableGroupService tableGroupService;

    private TableGroup 우아한형제들_단체그룹;
    private OrderTable 첫번째_주문_테이블;
    private OrderTable 두번째_주문_테이블;
    private List<OrderTable> 주문_테이블_목록;

    @BeforeEach
    public void setUp() {
        super.setUp();
        우아한형제들_단체그룹 = new TableGroup();

        첫번째_주문_테이블 = new OrderTable(4, true);
        두번째_주문_테이블 = new OrderTable(4, true);

        ReflectionTestUtils.setField(우아한형제들_단체그룹, "id", 1L);
        ReflectionTestUtils.setField(첫번째_주문_테이블, "id", 1L);
        ReflectionTestUtils.setField(두번째_주문_테이블, "id", 2L);

        주문_테이블_목록 = Arrays.asList(첫번째_주문_테이블, 두번째_주문_테이블);
        우아한형제들_단체그룹.group(주문_테이블_목록);
    }

    @DisplayName("단체 지정 등록에 실패한다.")
    @Test
    void 단체_지정_등록에_실패한다() throws Exception {
        List<Long> orderTableIds = 주문_테이블_목록.stream().map(OrderTable::getId).collect(toList());
        given(tableGroupService.create(any(TableGroupRequest.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(post("/api/table-groups")
                        .content(objectMapper.writeValueAsString(new TableGroupRequest(orderTableIds)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("단체 지정 등록에 성공한다.")
    @Test
    void 단체_지정_등록에_성공한다() throws Exception {
        List<Long> orderTableIds = 주문_테이블_목록.stream().map(OrderTable::getId).collect(toList());
        given(tableGroupService.create(any(TableGroupRequest.class))).willReturn(TableGroupResponse.of(우아한형제들_단체그룹));

        webMvc.perform(post("/api/table-groups")
                        .content(objectMapper.writeValueAsString(new TableGroupRequest(orderTableIds)))
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

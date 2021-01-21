package kitchenpos.api.tablegroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.tablegroup.TableGroupService;
import kitchenpos.common.BaseTest;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.dto.TableGroupRequest;
import kitchenpos.domain.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("테이블 그룹 컨트롤러 테스트")
class TableGroupRestControllerTest extends BaseTest {
    public static final String DEFAULT_TABLE_GROUPS_URI = "/api/table-groups/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private TableGroupRequest tableGroupRequest;

    @BeforeEach
    void setUp() {
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));
        orderTableRepository.save(OrderTable.of(null, 0, true));

        tableGroupRequest = new TableGroupRequest();
        tableGroupRequest.setOrderTableIds(Arrays.asList(3L, 4L, 5L));
    }

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void 테이블_그룹_생성() throws Exception {
        final String jsonTypeTableGroup = objectMapper.writeValueAsString(tableGroupRequest);

        mockMvc.perform(post(DEFAULT_TABLE_GROUPS_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonTypeTableGroup))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("orderTables[0].empty").value(true))
            .andExpect(jsonPath("orderTables[0].numberOfGuests").value(0));
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void 테이블_그룹_해제() throws Exception {
        final TableGroupResponse savedTableGroup = tableGroupService.create(tableGroupRequest);

        mockMvc.perform(delete(DEFAULT_TABLE_GROUPS_URI + savedTableGroup.getId()))
            .andDo(print())
            .andExpect(status().isNoContent());
    }
}

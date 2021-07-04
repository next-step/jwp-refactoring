package kitchenpos.ui;

import kitchenpos.application.command.TableGroupService;
import kitchenpos.application.query.TableGroupQueryService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupCreate;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.fixture.CleanUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.dto.response.TableGroupViewResponse.of;
import static kitchenpos.fixture.OrderTableFixture.*;
import static kitchenpos.ui.JsonUtil.toJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TableGroupRestController.class)
@ExtendWith(MockitoExtension.class)
class TableGroupRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableGroupService tableGroupService;

    @MockBean
    private TableGroupQueryService tableGroupQueryService;

    @BeforeEach
    void setUp() {
        CleanUp.cleanUpOrderFirst();
    }

    @Test
    void create() throws Exception {
        // given
        TableGroupCreateRequest createRequest = new TableGroupCreateRequest(Arrays.asList(1L, 2L, 3L));

        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), new OrderTables(사용중인_1명_2건_결제완료1, 사용중인_1명_테이블, 사용중인_1명_2건_결제완료2));

        given(tableGroupService.create(any(TableGroupCreate.class)))
                .willReturn(tableGroup.getId());
        given(tableGroupQueryService.findById(tableGroup.getId()))
                .willReturn(of(tableGroup));

        // when & then
        mockMvc.perform(
                post("/api/table-groups")
                        .content(toJson(createRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(validateTableGroup("$", tableGroup))
                .andExpect(validateOrderTable("$.orderTables[0]", 사용중인_1명_2건_결제완료1))
                .andExpect(validateOrderTable("$.orderTables[1]", 사용중인_1명_테이블))
                .andExpect(validateOrderTable("$.orderTables[2]", 사용중인_1명_2건_결제완료2))
        .andDo(print())
        ;
    }

    private ResultMatcher validateTableGroup(String prefix, TableGroup tableGroup) {
        return result -> {
            ResultMatcher.matchAll(
                    jsonPath(prefix + ".id").value(tableGroup.getId()),
                    jsonPath(prefix + ".createdDate").isNotEmpty()
            ).match(result);
        };
    }

    private ResultMatcher validateOrderTable(String prefix, OrderTable orderTable) {
        return result -> {
            ResultMatcher.matchAll(
                    jsonPath(prefix + ".id").value(orderTable.getId()),
                    jsonPath(prefix + ".tableGroupId").value(orderTable.getTableGroup().getId()),
                    jsonPath(prefix + ".numberOfGuests").value(orderTable.getNumberOfGuests().toInt()),
                    jsonPath(prefix + ".empty").value(orderTable.isEmpty())
            ).match(result);
        };
    }
}
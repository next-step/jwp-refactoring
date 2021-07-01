package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupCreate;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @BeforeEach
    void setUp() {
        CleanUp.cleanUpOrderFirst();
    }

    @Test
    void create() throws Exception {
        // given
        TableGroupCreateRequest createRequest = new TableGroupCreateRequest(Arrays.asList(1L, 2L, 3L));

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.addAll(Arrays.asList(사용중인_1명_2건_결제완료1, 사용중인_1명_테이블, 사용중인_1명_2건_결제완료2));

        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);

        given(tableGroupService.create(any(TableGroupCreate.class)))
                .willReturn(tableGroup);

        // when & then
        mockMvc.perform(
                post("/api/table-groups")
                        .content(toJson(createRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(validateTableGroup("$", tableGroup))
                .andExpect(validateOrderTable("$.orderTables[0]", orderTables.get(0)))
                .andExpect(validateOrderTable("$.orderTables[1]", orderTables.get(1)))
                .andExpect(validateOrderTable("$.orderTables[2]", orderTables.get(2)))
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
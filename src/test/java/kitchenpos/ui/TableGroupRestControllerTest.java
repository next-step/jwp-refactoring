package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.NumberOfGuest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupCreate;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.OrderTableViewResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.ui.JsonUtil.toJson;
import static org.junit.jupiter.api.Assertions.*;
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


    @Test
    void create() throws Exception {
        // given
        TableGroupCreateRequest createRequest = new TableGroupCreateRequest(Arrays.asList(1L, 2L, 3L));

        List<OrderTable> orderTables = new ArrayList<>();

        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), orderTables);
        orderTables.addAll(
            Arrays.asList(
                    new OrderTable(null, tableGroup, null, new NumberOfGuest(1), false),
                    new OrderTable(null, tableGroup, null, new NumberOfGuest(2), true),
                    new OrderTable(null, tableGroup, null, new NumberOfGuest(3), false)
            )
        );

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
                    jsonPath(prefix + ".createdDate").value(tableGroup.getCreatedDate().toString())
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
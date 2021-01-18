package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemCreateRequest;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderStatusChangeDto;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableEmptyChangeRequest;
import kitchenpos.dto.TableGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-08
 */
class OrderRestControllerTest extends BaseControllerTest {

    @DisplayName("주문 생성")
    @Test
    public void menuGroupCreateTest() throws Exception {
        Long orderTableId01 = createGroupedTable();
        Long orderTableId02 = createGroupedTable();
        createdTableGroupId(orderTableId01, orderTableId02);
        tableStatusChange(orderTableId01, false);
        tableStatusChange(orderTableId02, false);

        OrderLineItemCreateRequest orderLineItem = new OrderLineItemCreateRequest(1L, 1);
        OrderCreateRequest order = new OrderCreateRequest(orderTableId01, Collections.singletonList(orderLineItem));

        mockMvc.perform(post("/api/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(order)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("orderTableId").value(orderTableId01))
                .andExpect(jsonPath("orderStatus").value("COOKING"))
                .andExpect(jsonPath("orderLineItems[0].menuId")
                        .value(orderLineItem.getMenuId()))
                .andExpect(jsonPath("orderLineItems[0].quantity")
                        .value(orderLineItem.getQuantity()));
    }


    @DisplayName("주문 조회")
    @Test
    public void menuGroupSelectTest() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("주문 상태 변경")
    @Test
    public void menuGroupChangeOrderStateTest() throws Exception {

        Long orderTableId01 = createGroupedTable();
        Long orderTableId02 = createGroupedTable();
        createdTableGroupId(orderTableId01, orderTableId02);
        tableStatusChange(orderTableId01, false);
        tableStatusChange(orderTableId02, false);

        OrderLineItemCreateRequest orderLineItem = new OrderLineItemCreateRequest(1L, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(orderTableId01, Collections.singletonList(orderLineItem));

        String url = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        OrderStatusChangeDto orderStatusChangeRequest = new OrderStatusChangeDto(OrderStatus.COMPLETION);
        mockMvc.perform(put(url + "/order-status")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(orderStatusChangeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("orderStatus").value("COMPLETION"));

    }

    private Long getCreateTableId() throws Exception {
        String url = mockMvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new OrderTableCreateRequest(null, 4, true))))
                .andReturn()
                .getResponse()
                .getHeader("Location").split("/")[3];

        return Long.valueOf(url);
    }

    private void createdTableGroupId(Long ...tableId) throws Exception {
        TableGroupCreateRequest createRequest = new TableGroupCreateRequest(Arrays.asList(tableId));

        mockMvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists());
    }

    private Long createGroupedTable() throws Exception {
        Long tableId = getCreateTableId();

        createdTableGroupId(tableId, getCreateTableId());

        return tableId;
    }

    private void tableStatusChange(Long tableId, boolean empty) throws Exception {
        OrderTableEmptyChangeRequest request = new OrderTableEmptyChangeRequest(empty);

        mockMvc.perform(put("/api/tables/"+ tableId +"/empty")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));
    }

}

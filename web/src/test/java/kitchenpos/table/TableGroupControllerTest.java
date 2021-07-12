package kitchenpos.table;

import kitchenpos.common.ControllerTest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableStatus;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TableGroupControllerTest extends ControllerTest {

    private List<OrderTableRequest> orderTables;

    @BeforeEach
    public void setup() {
        orderTables = new ArrayList<>();
    }

    @Test
    @DisplayName("단체를 지정 한다")
    public void createOrderTableGroup() throws Exception {
        // given
        orderTables.add(new OrderTableRequest(5L, 0, TableStatus.EMPTY));
        orderTables.add(new OrderTableRequest(6L, 0, TableStatus.EMPTY));
        TableGroupRequest tableGroupRequest = new TableGroupRequest(LocalDateTime.now(), orderTables);

        // when
        // then
        단체_지정_요청(tableGroupRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("$.orderTables", hasSize(2)));
    }


    @Test
    @DisplayName("단체 지정 실패 - 주문 테이블이 하나 일 경우")
    public void createOrderTableGroupFailByOneOrderTable() {
        // given
        orderTables.add(new OrderTableRequest(5L, 0, TableStatus.EMPTY));
        TableGroupRequest tableGroup = new TableGroupRequest(LocalDateTime.now(), orderTables);

        // when
        // then
        assertThrows(NestedServletException.class, () -> 단체_지정_요청(tableGroup));
    }

    @Test
    @DisplayName("단체 지정 실패 - 주문 테이블이 empty가 아닐 경우")
    public void createOrderTableGroupFailByOnderTableIsNotEmpty() {
        // given
        orderTables.add(new OrderTableRequest(3L, 0, TableStatus.ORDER));
        orderTables.add(new OrderTableRequest(6L, 0, TableStatus.ORDER));
        TableGroupRequest tableGroupRequest = new TableGroupRequest(LocalDateTime.now(), orderTables);

        // when
        // then
        assertThrows(NestedServletException.class, () -> 단체_지정_요청(tableGroupRequest));
    }

    @Test
    @DisplayName("단체 지정 실패 - 이미 단체 지정이 된 테이블")
    public void createOrderTableGroupFailByAlreadyExistsTableGroup() throws Exception {
        // given
        orderTables.add(new OrderTableRequest(9L, 0, TableStatus.ORDER));
        orderTables.add(new OrderTableRequest(10L, 0, TableStatus.ORDER));
        TableGroupRequest tableGroup = new TableGroupRequest(LocalDateTime.now(), orderTables);
        단체_지정_요청(tableGroup);

        // when
        // then
        assertThrows(NestedServletException.class, () -> 단체_지정_요청(tableGroup));
    }

    @Test
    @DisplayName("단체 지정 실패 - 기존 데이터베이스에 존재하지 않는 테이블을 포함하여 단체 지정 할 경우")
    public void createOrderTableGroupFailByNotExistsTable() {
        // given
        orderTables.add(new OrderTableRequest(10L, 0, TableStatus.ORDER));
        orderTables.add(new OrderTableRequest(11L, 0, TableStatus.ORDER));
        TableGroupRequest tableGroup = new TableGroupRequest(LocalDateTime.now(), orderTables);

        // when
        // then
        assertThrows(NestedServletException.class, () -> 단체_지정_요청(tableGroup));
    }

    @Test
    @DisplayName("단체 지정을 해제 한다")
    public void deleteOrderTableGroup() throws Exception {
        // given
        orderTables.add(new OrderTableRequest(7L, 0, TableStatus.COMPLETION));
        orderTables.add(new OrderTableRequest(8L, 0, TableStatus.COMPLETION));
        단체_지정_요청(new TableGroupRequest(LocalDateTime.now(), orderTables));

        // when
        // then
        단체_지정_해제_요청()
                .andExpect(status().isNoContent());
    }

    private ResultActions 단체_지정_요청(TableGroupRequest tableGroupRequest) throws Exception {
        return mockMvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroupRequest)))
                .andDo(print());
    }

    private ResultActions 단체_지정_해제_요청() throws Exception {
        return mockMvc.perform(delete("/api/table-groups/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

}

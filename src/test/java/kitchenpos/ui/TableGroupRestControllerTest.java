package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TableGroupRestControllerTest extends ControllerTest {

    private final String TABLE_GROUP_URI = "/api/table-groups";

    @Autowired
    private TableService tableService;

    private List<OrderTableRequest> orderTableRequests;
    private OrderTable 테이블_1번;
    private OrderTable 테이블_2번;


    @BeforeEach
    void setUp() {
        테이블_1번 = 테이블을_생성한다(0, true);
        테이블_2번 = 테이블을_생성한다(0, true);

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(테이블_1번);
        orderTables.add(테이블_2번);
        orderTableRequests = orderTables.stream()
                .map(OrderTableRequest::of)
                .collect(Collectors.toList());
    }

    @DisplayName("테이블 그룹을 등록할 수 있다")
    @Test
    void create() throws Exception {
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);
        String body = objectMapper.writeValueAsString(tableGroupRequest);
        컨트롤러_생성_요청_및_검증(TABLE_GROUP_URI, body);
    }

    @DisplayName("테이블 그룹을 해제할 수 있다")
    @Test
    void ungroup() throws Exception {
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);
        String body = objectMapper.writeValueAsString(tableGroupRequest);
        컨트롤러_생성_요청_및_검증(TABLE_GROUP_URI, body);

        mockMvc.perform(delete(TABLE_GROUP_URI + "/{id}", 1l)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    private OrderTable 테이블을_생성한다(int numberOfGuest, boolean empty) {
        return tableService.create(new OrderTableRequest(numberOfGuest, empty));
    }
}
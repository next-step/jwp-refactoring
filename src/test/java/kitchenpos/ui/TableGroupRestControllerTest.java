package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TableGroupRestControllerTest extends ControllerTest {

    private final String TABLE_GROUP_URI = "/api/table-groups";

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    private List<OrderTable> orderTables;
    private OrderTable 테이블_1번;
    private OrderTable 테이블_2번;


    @BeforeEach
    void setUp() {
        TableGroup 테이블그룹_1번 = tableGroupService.findTableGroupById(1l);
        TableGroup 테이블그룹_2번 = tableGroupService.findTableGroupById(2l);
        테이블_1번 = 테이블을_생성한다(테이블그룹_1번, 0, true);
        테이블_2번 = 테이블을_생성한다(테이블그룹_2번, 0, true);

        orderTables = new ArrayList<>();
        orderTables.add(테이블_1번);
        orderTables.add(테이블_2번);
    }


    @DisplayName("테이블 그룹을 등록할 수 있다")
    @Test
    void create() throws Exception {
        TableGroup tableGroup = new TableGroup(orderTables);
        String body = objectMapper.writeValueAsString(tableGroup);
        컨트롤러_생성_요청_및_검증(TABLE_GROUP_URI, body);
    }

    @DisplayName("테이블 그룹을 해제할 수 있다")
    @Test
    void ungroup() throws Exception {
        TableGroup tableGroup = new TableGroup(orderTables);
        String body = objectMapper.writeValueAsString(tableGroup);
        컨트롤러_생성_요청_및_검증(TABLE_GROUP_URI, body);

        mockMvc.perform(delete(TABLE_GROUP_URI + "/{id}", 1l)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    private OrderTable 테이블을_생성한다(TableGroup tableGroup, int numberOfGuest, boolean empty) {
        return tableService.create(new OrderTable(tableGroup, numberOfGuest, empty));
    }
}
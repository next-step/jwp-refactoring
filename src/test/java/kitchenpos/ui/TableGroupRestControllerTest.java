package kitchenpos.ui;

import kitchenpos.application.DomainTestUtils;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class TableGroupRestControllerTest extends DomainTestUtils {

    @Autowired
    private MockMvc mockMvc;

    private List<OrderTable> orderTables;
    private OrderTable 테이블_1번;
    private OrderTable 테이블_2번;

    @BeforeEach
    void setUp() {
        테이블_1번 = 테이블을_생성한다(1l, 게스트수, 비어있음);
        테이블_2번 = 테이블을_생성한다(2l, 게스트수, 비어있음);

        orderTables = new ArrayList<>();
        orderTables.add(테이블_1번);
        orderTables.add(테이블_2번);
    }


    @DisplayName("테이블 그룹을 등록할 수 있다")
    @Test
    void create() throws Exception {
        TableGroup tableGroup = new TableGroup(orderTables);
        String body = objectMapper.writeValueAsString(tableGroup);
        컨트롤러_생성_요청_및_검증(mockMvc, TABLE_GROUP_URI, body);
    }

    @DisplayName("테이블 그룹을 해제할 수 있다")
    @Test
    void ungroup() throws Exception {
        TableGroup tableGroup = new TableGroup(orderTables);
        String body = objectMapper.writeValueAsString(tableGroup);
        컨트롤러_생성_요청_및_검증(mockMvc, TABLE_GROUP_URI, body);

        mockMvc.perform(delete(TABLE_GROUP_URI + "/{id}", 1l)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
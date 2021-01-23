package kitchenpos.ui;

import kitchenpos.application.DomainTestUtils;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class TableRestControllerTest extends DomainTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        게스트수 = 10;
    }

    @DisplayName("테이블을 생성한다")
    @Test
    void create() throws Exception {
        OrderTable orderTable = new OrderTable(게스트수, 비어있음);
        String body = objectMapper.writeValueAsString(orderTable);

        컨트롤러_생성_요청_및_검증(mockMvc, TABLE_URI, body);
    }

    @DisplayName("테이블을 조회한다")
    @Test
    void list() throws Exception {
        컨트롤러_조회_요청_및_검증(mockMvc, TABLE_URI);
    }

    @DisplayName("테이블 상태와 게스트수를 변경한다")
    @Test
    void changeStatusAndGuests() throws Exception {
        final Long orderTableId = 1l;
        OrderTable orderTable = orderTableDao.findById(orderTableId).get();
        orderTable.setTableGroupId(null);
        orderTableDao.save(orderTable);

        orderTable.setEmpty(비어있지않음);
        String body = objectMapper.writeValueAsString(orderTable);

        테이블_변경_요청_및_검증(body, orderTableId, "/empty");

        orderTable.setNumberOfGuests(10);
        body = objectMapper.writeValueAsString(orderTable);

        테이블_변경_요청_및_검증(body, orderTableId, "/number-of-guests");
    }

    private void 테이블_변경_요청_및_검증(String body, Long id, String url) throws Exception {
        mockMvc.perform(put(TABLE_URI + "/{id}" + url, id)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
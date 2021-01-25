package kitchenpos.ui;

import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TableRestControllerTest extends ControllerTest {

    private final String TABLE_URI = "/api/tables";

    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("테이블을 생성한다")
    @Test
    void create() throws Exception {
        OrderTable orderTable = new OrderTable(10, true);
        String body = objectMapper.writeValueAsString(orderTable);

        컨트롤러_생성_요청_및_검증(TABLE_URI, body);
    }

    @DisplayName("테이블을 조회한다")
    @Test
    void list() throws Exception {
        컨트롤러_조회_요청_및_검증(TABLE_URI);
    }

    @DisplayName("테이블 상태와 게스트수를 변경한다")
    @Test
    void changeStatusAndGuests() throws Exception {
        final Long orderTableId = 4l;
        OrderTable orderTable = orderTableRepository.findById(orderTableId).get();

        orderTable.setEmpty(false);
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
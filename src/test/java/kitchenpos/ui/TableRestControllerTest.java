package kitchenpos.ui;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TableRestControllerTest extends ControllerTest {

    private int 게스트수;
    private boolean empty;

    @BeforeEach
    void setUp() {
        게스트수 = 10;
        empty = false;
    }

    @Test
    void create() throws Exception {
        OrderTable orderTable = new OrderTable(게스트수, empty);
        String body = objectMapper.writeValueAsString(orderTable);

        컨트롤러_생성_요청(TABLE_URI, body);
    }

    @Test
    void list() throws Exception {
        컨트롤러_조회_요청(TABLE_URI);
    }

    @Test
    void changeEmpty() {

    }

    @Test
    void changeNumberOfGuests() {

    }
}
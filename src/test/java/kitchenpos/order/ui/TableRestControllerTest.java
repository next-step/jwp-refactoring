package kitchenpos.order.ui;

import kitchenpos.ControllerTest;
import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TableRestController ui 테스트")
@WebMvcTest(TableRestController.class)
public class TableRestControllerTest extends ControllerTest {
    @MockBean
    private TableService tableService;

    private OrderTable 테이블1;
    private OrderTable 테이블2;

    @BeforeEach
    protected void setUp() {
        super.setUp();

        테이블1 = new OrderTable(1L, 4, true);
        테이블2 = new OrderTable(2L, 6, true);
    }

    @DisplayName("테이블 생성 api 테스트")
    @Test
    void createTable() throws Exception {
        given(tableService.create(any(OrderTable.class))).willReturn(테이블1);

        mockMvc.perform(post("/api/tables")
                        .content(mapper.writeValueAsBytes(테이블1))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @DisplayName("테이블 조회 api 테스트")
    @Test
    void listTables() throws Exception {
        given(tableService.list()).willReturn(Lists.newArrayList(테이블1, 테이블2));

        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk());
    }

    @DisplayName("테이블을 빈테이블로 변경 api 테스트")
    @Test
    void changeEmptyTable() throws Exception {
        given(tableService.changeEmpty(anyLong(), any(OrderTable.class))).willReturn(테이블1);

        mockMvc.perform(put("/api/tables/{id}/empty", 1L)
                        .content(mapper.writeValueAsBytes(테이블1))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @DisplayName("테이블 손님 수 변경 api 테스트")
    @Test
    void changeNumberOfGuestsTable() throws Exception {
        given(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class))).willReturn(테이블1);

        mockMvc.perform(put("/api/tables/{id}/number-of-guests", 1L)
                        .content(mapper.writeValueAsBytes(테이블1))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}

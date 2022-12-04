package kitchenpos.ui;

import kitchenpos.ControllerTest;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("TableRestController 테스트")
@WebMvcTest(TableRestController.class)
class TableRestControllerTest extends ControllerTest {
    @MockBean
    private TableService tableService;

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문테이블1 = new OrderTable(1L, null, 2, false);
        주문테이블2 = new OrderTable(2L, null, 3, false);
    }

    @Test
    void 주문_테이블_생성() throws Exception {
        given(tableService.create(any(OrderTable.class))).willReturn(주문테이블1);

        webMvc.perform(post("/api/tables")
                    .content(mapper.writeValueAsString(주문테이블1))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(주문테이블1.getId().intValue())))
                .andExpect(jsonPath("$.numberOfGuests", is(주문테이블1.getNumberOfGuests())))
                .andExpect(jsonPath("$.empty", is(주문테이블1.isEmpty())));
    }

    @Test
    void 주문_테이블_생성_실패() throws Exception {
        given(tableService.create(any(OrderTable.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(post("/api/tables")
                        .content(mapper.writeValueAsString(주문테이블1))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주문_테이블_목록_조회() throws Exception {
        given(tableService.list()).willReturn(Arrays.asList(주문테이블1, 주문테이블2));

        webMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void 빈_테이블로_변경() throws Exception {
        OrderTable 변경_테이블 = new OrderTable();
        변경_테이블.setId(주문테이블1.getId());
        변경_테이블.setEmpty(true);

        given(tableService.changeEmpty(anyLong(), any(OrderTable.class))).willReturn(변경_테이블);

        webMvc.perform(put("/api/tables/" + 주문테이블1.getId() + "/empty")
                    .content(mapper.writeValueAsString(변경_테이블))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(변경_테이블.getId().intValue())))
                .andExpect(jsonPath("$.empty", is(변경_테이블.isEmpty())));
    }

    @Test
    void 빈_테이블로_변경_실패() throws Exception {
        OrderTable 변경_테이블 = new OrderTable();
        변경_테이블.setId(주문테이블1.getId());
        변경_테이블.setEmpty(true);

        given(tableService.changeEmpty(anyLong(), any(OrderTable.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(put("/api/tables/" + 주문테이블1.getId() + "/empty")
                        .content(mapper.writeValueAsString(변경_테이블))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 방문한_손님_수_변경() throws Exception {
        OrderTable 변경_테이블 = new OrderTable();
        변경_테이블.setId(주문테이블1.getId());
        변경_테이블.setNumberOfGuests(8);

        given(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class))).willReturn(변경_테이블);

        webMvc.perform(put("/api/tables/" + 주문테이블1.getId() + "/number-of-guests")
                    .content(mapper.writeValueAsString(변경_테이블))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(변경_테이블.getId().intValue())))
                .andExpect(jsonPath("$.numberOfGuests", is(변경_테이블.getNumberOfGuests())));
    }

    @Test
    void 방문한_손님_수_변경_실패() throws Exception {
        OrderTable 변경_테이블 = new OrderTable();
        변경_테이블.setId(주문테이블1.getId());
        변경_테이블.setNumberOfGuests(8);

        given(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(put("/api/tables/" + 주문테이블1.getId() + "/number-of-guests")
                        .content(mapper.writeValueAsString(변경_테이블))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}

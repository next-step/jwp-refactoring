package kitchenpos.table.ui;

import kitchenpos.ControllerTest;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderEmpty;
import kitchenpos.table.domain.OrderGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TableRestController 테스트")
@WebMvcTest(TableRestController.class)
public class TableRestControllerTest extends ControllerTest {

    @MockBean
    private TableService tableService;

    private OrderTable 첫번째_주문_테이블;
    private OrderTable 두번째_주문_테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();

        첫번째_주문_테이블 = new OrderTable(2, false);
        두번째_주문_테이블 = new OrderTable(3, false);

        ReflectionTestUtils.setField(첫번째_주문_테이블, "id", 1L);
        ReflectionTestUtils.setField(두번째_주문_테이블, "id", 2L);
    }

    @DisplayName("주문 테이블 등록에 실패한다.")
    @Test
    void 주문_테이블_등록에_실패한다() throws Exception {
        given(tableService.create(any(OrderTableRequest.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(post("/api/tables")
                        .content(objectMapper.writeValueAsString(첫번째_주문_테이블))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("주문 테이블 등록에 성공한다.")
    @Test
    void 주문_테이블_등록에_성공한다() throws Exception {
        given(tableService.create(any(OrderTableRequest.class))).willReturn(new OrderTableResponse(첫번째_주문_테이블));

        webMvc.perform(post("/api/tables")
                        .content(objectMapper.writeValueAsString(new OrderTableRequest(첫번째_주문_테이블.getNumberOfGuests(), 첫번째_주문_테이블.isEmpty())))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(첫번째_주문_테이블.getId().intValue())))
                .andExpect(jsonPath("$.numberOfGuests", is(첫번째_주문_테이블.getNumberOfGuests())))
                .andExpect(jsonPath("$.empty", is(첫번째_주문_테이블.isEmpty())));
    }

    @DisplayName("빈 테이블 변경에 실패한다.")
    @Test
    void 빈_테이블_변경에_실패한다() throws Exception {
        given(tableService.changeEmpty(anyLong(), any(OrderEmpty.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(put("/api/tables/" + 첫번째_주문_테이블.getId() + "/empty")
                        .content(objectMapper.writeValueAsString(두번째_주문_테이블))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("빈 테이블 변경에 성공한다.")
    @Test
    void 빈_테이블_변경에_성공한다() throws Exception {
        given(tableService.changeEmpty(anyLong(), any(OrderEmpty.class))).willReturn(OrderTableResponse.of(첫번째_주문_테이블));

        webMvc.perform(put("/api/tables/" + 첫번째_주문_테이블.getId() + "/empty")
                        .content(objectMapper.writeValueAsString(new OrderEmpty(true)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(첫번째_주문_테이블.getId().intValue())))
                .andExpect(jsonPath("$.empty", is(첫번째_주문_테이블.isEmpty())));
    }

    @DisplayName("방문한 손님 수 변경에 실패한다.")
    @Test
    void 방문한_손님_수_변경에_실패한다() throws Exception {
        given(tableService.changeNumberOfGuests(anyLong(), any(OrderGuests.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(put("/api/tables/" + 첫번째_주문_테이블.getId() + "/number-of-guests")
                        .content(objectMapper.writeValueAsString(new OrderGuests(5)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("방문한 손님 수 변경에 성공한다.")
    @Test
    void 방문한_손님_수_변경에_성공한다() throws Exception {
        given(tableService.changeNumberOfGuests(anyLong(), any(OrderGuests.class))).willReturn(OrderTableResponse.of(첫번째_주문_테이블));

        webMvc.perform(put("/api/tables/" + 첫번째_주문_테이블.getId() + "/number-of-guests")
                        .content(objectMapper.writeValueAsString(new OrderGuests(5)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(첫번째_주문_테이블.getId().intValue())))
                .andExpect(jsonPath("$.numberOfGuests", is(첫번째_주문_테이블.getNumberOfGuests())));
    }
}

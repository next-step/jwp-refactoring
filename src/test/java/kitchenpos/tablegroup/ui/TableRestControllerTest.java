package kitchenpos.tablegroup.ui;

import kitchenpos.ControllerTest;
import kitchenpos.tablegroup.application.TableService;
import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableEmpty;
import kitchenpos.tablegroup.domain.TableGuests;
import kitchenpos.tablegroup.dto.OrderTableRequest;
import kitchenpos.tablegroup.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        주문테이블1 = new OrderTable(2, false);
        주문테이블2 = new OrderTable(3, false);

        ReflectionTestUtils.setField(주문테이블1, "id", 1L);
        ReflectionTestUtils.setField(주문테이블2, "id", 2L);
    }

    @Test
    void 주문_테이블_생성() throws Exception {
        given(tableService.create(any(OrderTableRequest.class))).willReturn(OrderTableResponse.of(주문테이블1));

        webMvc.perform(post("/api/tables")
                        .content(mapper.writeValueAsString(new OrderTableRequest(주문테이블1.getNumberOfGuests(), 주문테이블1.isEmpty())))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(주문테이블1.getId().intValue())))
                .andExpect(jsonPath("$.numberOfGuests", is(주문테이블1.getNumberOfGuests())))
                .andExpect(jsonPath("$.empty", is(주문테이블1.isEmpty())));
    }

    @Test
    void 주문_테이블_생성_실패() throws Exception {
        given(tableService.create(any(OrderTableRequest.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(post("/api/tables")
                        .content(mapper.writeValueAsString(new OrderTableRequest(주문테이블1.getNumberOfGuests(), 주문테이블1.isEmpty())))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주문_테이블_목록_조회() throws Exception {
        given(tableService.list()).willReturn(Arrays.asList(
                OrderTableResponse.of(주문테이블1), OrderTableResponse.of(주문테이블2)));

        webMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void 빈_테이블로_변경() throws Exception {
        주문테이블1.changeEmpty(true);
        given(tableService.changeEmpty(anyLong(), any(TableEmpty.class)))
                .willReturn(OrderTableResponse.of(주문테이블1));

        webMvc.perform(put("/api/tables/" + 주문테이블1.getId() + "/empty")
                        .content(mapper.writeValueAsString(new TableEmpty(true)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(주문테이블1.getId().intValue())))
                .andExpect(jsonPath("$.empty", is(주문테이블1.isEmpty())));
    }

    @Test
    void 빈_테이블로_변경_실패() throws Exception {
        주문테이블1.changeEmpty(true);
        given(tableService.changeEmpty(anyLong(), any(TableEmpty.class)))
                .willThrow(IllegalArgumentException.class);

        webMvc.perform(put("/api/tables/" + 주문테이블1.getId() + "/empty")
                        .content(mapper.writeValueAsString(new TableEmpty(true)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 방문한_손님_수_변경() throws Exception {
        주문테이블1.changeNumberOfGuests(5);
        given(tableService.changeNumberOfGuests(anyLong(), any(TableGuests.class)))
                .willReturn(OrderTableResponse.of(주문테이블1));

        webMvc.perform(put("/api/tables/" + 주문테이블1.getId() + "/number-of-guests")
                        .content(mapper.writeValueAsString(new TableGuests(5)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(주문테이블1.getId().intValue())))
                .andExpect(jsonPath("$.numberOfGuests", is(주문테이블1.getNumberOfGuests())));
    }

    @Test
    void 방문한_손님_수_변경_실패() throws Exception {
        주문테이블1.changeNumberOfGuests(5);
        given(tableService.changeNumberOfGuests(anyLong(), any(TableGuests.class)))
                .willThrow(IllegalArgumentException.class);

        webMvc.perform(put("/api/tables/" + 주문테이블1.getId() + "/number-of-guests")
                        .content(mapper.writeValueAsString(new TableGuests(5)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}
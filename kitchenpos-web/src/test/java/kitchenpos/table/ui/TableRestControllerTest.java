package kitchenpos.table.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.ChangeGuestNumberRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.OrderTableSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.table.fixtures.OrderTableFixtures.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName : kitchenpos.ui
 * fileName : TableRestControllerTest
 * author : haedoang
 * date : 2021/12/15
 * description :
 */
@DisplayName("테이블 컨트롤러 테스트")
@WebMvcTest(TableRestController.class)
class TableRestControllerTest {
    private OrderTableSaveRequest request;
    private OrderTableResponse response;
    final OrderTableResponse 주문불가_다섯명_테이블_응답 = OrderTableResponse.of(주문불가_다섯명테이블());
    final OrderTableResponse 주문가능_두명_테이블_응답 = OrderTableResponse.of(주문가능_두명테이블());

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    @BeforeEach
    void setUp() {
        request = 주문가능_다섯명테이블요청();
        response = OrderTableResponse.of(request.toEntity());
    }

    @Test
    @DisplayName("주문 테이블을 조회한다")
    public void getOrderTable() throws Exception {
        // given
        List<OrderTableResponse> orderTables = Arrays.asList(response);
        given(tableService.list()).willReturn(orderTables);

        // when
        ResultActions actions = mockMvc.perform(
            get("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numberOfGuests", is(5)))
                .andExpect(jsonPath("$[0].empty", is(false)));
    }

    @Test
    @DisplayName("주문 테이블을 등록한다")
    public void postOrderTable() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        given(tableService.create(any(OrderTableSaveRequest.class))).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(
            post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        ).andDo(print());

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.numberOfGuests", is(5)))
                .andExpect(jsonPath("$.empty", is(false)));
    }

    @Test
    @DisplayName("주문 테이블을 빈 테이블로 변경한다")
    public void putOrderTableToEmpty() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        given(tableService.changeEmpty(anyLong(), any(ChangeEmptyRequest.class))).willReturn(주문불가_다섯명_테이블_응답);

        // when
        ResultActions actions = mockMvc.perform(
            put("/api/tables/" + 1L + "/empty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(주문불가로_변경요청()))
        ).andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.empty", is(true)));
    }

    @Test
    @DisplayName("주문 테이블을 사용중인 테이블로 변경한다")
    public void putOrderTableToNotEmpty() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        given(tableService.changeEmpty(anyLong(), any(ChangeEmptyRequest.class))).willReturn(주문가능_두명_테이블_응답);

        // when
        ResultActions actions = mockMvc.perform(
            put("/api/tables/" + 1L + "/empty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(주문가능으로_변경요청()))
        ).andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.empty", is(false)));
    }

    @Test
    @DisplayName("주문 테이블의 사용자 수를 변경한다")
    public void putOrderTableGuests() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        given(tableService.changeNumberOfGuests(anyLong(), any(ChangeGuestNumberRequest.class))).willReturn(주문가능_두명_테이블_응답);

        // when
        ResultActions actions = mockMvc.perform(
            put("/api/tables/" + 1L + "/number-of-guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(두명으로_변경요청()))
        ).andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfGuests", is(2)));
    }
}

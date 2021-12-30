package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.ordertable.OrderTableRequest;
import kitchenpos.dto.ordertable.OrderTableResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private TableService tableService;

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void create() throws Exception {
        final OrderTableRequest 주문테이블_요청1 = OrderTableRequest.of(5, false);

        final OrderTable 주문테이블1 = OrderTable.of(1L, 2, false);
        final OrderTableResponse 주문테이블1_응답 = OrderTableResponse.from(주문테이블1);
        
        given(tableService.create(any())).willReturn(주문테이블1_응답);

        final ResultActions actions = mvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(주문테이블_요청1)))
                .andDo(print());

        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("numberOfGuests").value(2))
                .andExpect(jsonPath("empty").value(false));
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() throws Exception {
        final OrderTable 주문테이블1 = OrderTable.of(1L, 2, false);
        final OrderTable 주문테이블2 = OrderTable.of(2L, 2, false);
        final OrderTableResponse 주문테이블1_응답 = OrderTableResponse.from(주문테이블1);
        final OrderTableResponse 주문테이블2_응답 = OrderTableResponse.from(주문테이블2);
        final List<OrderTableResponse> 주문테이블목록_응답 = Lists.newArrayList(주문테이블1_응답, 주문테이블2_응답);
        
        given(tableService.list()).willReturn(주문테이블목록_응답);

        final ResultActions actions = mvc.perform(get("/api/tables")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json;charset=UTF-8")));
    }

    @DisplayName("빈 테이블로 변경한다.")
    @Test
    void changeEmpty() throws Exception {
        final OrderTable 빈_주문테이블 = OrderTable.of(1L, 0, true);
        final OrderTableRequest 주문테이블1빈테이블_요청 = OrderTableRequest.from(true);
        final OrderTableResponse 주문테이블1빈테이블_응답 = OrderTableResponse.from(빈_주문테이블);
        
        given(tableService.changeEmpty(anyLong(), any())).willReturn(주문테이블1빈테이블_응답);

        final ResultActions actions = mvc.perform(put("/api/tables/{orderTableId}/empty", 주문테이블1빈테이블_응답.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(주문테이블1빈테이블_요청)))
                .andDo(print());

        
        actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json;charset=UTF-8")))
                .andExpect(content().string(containsString(Long.toString(1L))))
                .andExpect(content().string(containsString(String.valueOf(0))));
    }

    @DisplayName("손님수를 변경한다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        final OrderTableRequest 주문테이블2손님수변경_요청 = OrderTableRequest.from(10);
        final OrderTable 손님수변경_주문테이블2 = OrderTable.of(2L, 5, false);
        final OrderTableResponse 주문테이블2손님수변경_응답 = OrderTableResponse.from(손님수변경_주문테이블2);
        
        given(tableService.changeNumberOfGuests(anyLong(), any())).willReturn(주문테이블2손님수변경_응답);

        final ResultActions actions = mvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 주문테이블2손님수변경_응답.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(주문테이블2손님수변경_요청)))
                .andDo(print());

        actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json;charset=UTF-8")))
                .andExpect(content().string(containsString(Long.toString(2L))))
                .andExpect(content().string(containsString(String.valueOf(5))));
    }
}

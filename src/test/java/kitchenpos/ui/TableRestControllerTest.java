package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;
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
        final OrderTable orderTable = new OrderTable(1L, null, 0, false);
        // given
        given(tableService.create(any())).willReturn(orderTable);

        // when
        final ResultActions actions = mvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(orderTable)))
                .andDo(print());
        //then
        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("tableGroupId").value(nullValue()))
                .andExpect(jsonPath("numberOfGuests").value(0))
                .andExpect(jsonPath("empty").value(false));
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void list() throws Exception {
        // given
        final OrderTable orderTable1 = new OrderTable(1L, null, 0, false);
        final OrderTable orderTable2 = new OrderTable(1L, null, 0, false);
        final List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        given(tableService.list()).willReturn(orderTables);

        // when
        final ResultActions actions = mvc.perform(get("/api/tables")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        //then
        actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json;charset=UTF-8")))
                .andExpect(content().string(containsString(Long.toString(1L))));
    }

    @DisplayName("빈 테이블로 변경한다.")
    @Test
    void changeEmpty() throws Exception {
        // given
        final OrderTable orderTable = new OrderTable(1L, null, 0, true);
        given(tableService.changeEmpty(anyLong(), any())).willReturn(orderTable);

        // when
        final ResultActions actions = mvc.perform(put("/api/tables/{orderTableId}/empty", orderTable.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(orderTable)))
                .andDo(print());

        //then
        actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json;charset=UTF-8")))
                .andExpect(content().string(containsString(Long.toString(1L))))
                .andExpect(content().string(containsString(String.valueOf(0))));
    }

    @DisplayName("손님수를 변경한다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        // given
        final OrderTable orderTable = new OrderTable(1L, null, 10, true);
        given(tableService.changeNumberOfGuests(anyLong(), any())).willReturn(orderTable);

        // when
        final ResultActions actions = mvc.perform(put("/api/tables/{orderTableId}/number-of-guests", orderTable.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(orderTable)))
                .andDo(print());

        //then
        actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json;charset=UTF-8")))
                .andExpect(content().string(containsString(Long.toString(1L))))
                .andExpect(content().string(containsString(String.valueOf(10))));
    }
}

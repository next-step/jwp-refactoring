package kitchenpos.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("주문 테이블 관련 테스트")
@SpringBootTest
class TableRestControllerTest {
    public static final String ORDER_TABLE_URI = "/api/tables";

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(@Autowired TableRestController tableRestController) {
        // MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(tableRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setNumberOfGuests(0);
        orderTable1.setEmpty(true);

        orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setNumberOfGuests(0);
        orderTable2.setEmpty(true);
    }

    public String toJsonString(OrderTable orderTable) throws JsonProcessingException {
        return objectMapper.writeValueAsString(orderTable);
    }

    @Test
    @DisplayName("주문 테이블을 등록할 수 있다.")
    public void create() throws Exception {
        // given
        given(tableService.create(any())).willReturn(orderTable1);

        // when
        final ResultActions actions = mockMvc.perform(post(ORDER_TABLE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(orderTable1)));

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.tableGroupId").isEmpty())
                .andExpect(jsonPath("$.numberOfGuests").value(orderTable1.getNumberOfGuests()))
                .andExpect(jsonPath("$.empty").value(orderTable1.isEmpty()));
    }

    @Test
    @DisplayName("주문 테이블의 목록을 조회할 수 있다.")
    public void list() throws Exception {
        // given
        given(tableService.list()).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        final ResultActions actions = mockMvc.perform(get(ORDER_TABLE_URI)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].tableGroupId").isEmpty())
                .andExpect(jsonPath("$[0].numberOfGuests").value(orderTable1.getNumberOfGuests()))
                .andExpect(jsonPath("$[0].empty").value(orderTable1.isEmpty()));
    }

    @Test
    @DisplayName("빈 테이블 설정 할 수 있다.")
    @Transactional
    public void changeEmpty() throws Exception {
        // given
        given(tableService.changeEmpty(orderTable1.getId(), orderTable1)).willReturn(orderTable1);

        // when
        final ResultActions actions = mockMvc.perform(put(ORDER_TABLE_URI +"/{orderTableId}/empty", orderTable1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(orderTable1)));

        // then
        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("빈 테이블 해지할 수 있다.")
    public void changeNumberOfGuests() throws Exception {
        // given
        given(tableService.changeEmpty(orderTable1.getId(), orderTable1)).willReturn(orderTable1);

        // when
        final ResultActions actions = mockMvc.perform(put(ORDER_TABLE_URI +"/{orderTableId}/number-of-guests", orderTable1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(orderTable1)));

        // then
        actions.andExpect(status().isOk());
    }
}
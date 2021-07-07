package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.api.TableRestController;
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
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TableRestController.class)
class TableRestControllerTest {
    private static final String URI = "/api/tables";

    @Autowired
    private TableRestController tableRestController;

    @MockBean
    private TableService tableService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tableRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setNumberOfGuests(6);
        orderTable1.setEmpty(true);
        orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setNumberOfGuests(3);
        orderTable2.setEmpty(true);
    }

    @DisplayName("주문 테이블을 추가한다.")
    @Test
    void create() throws Exception {
        //given
        given(tableService.create(any())).willReturn(orderTable1);

        //when
        ResultActions actions = mockMvc.perform(post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable1)));

        //then
        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.tableGroupId").isEmpty())
                .andExpect(jsonPath("$.numberOfGuests").value(orderTable1.getNumberOfGuests()))
                .andExpect(jsonPath("$.empty").value(orderTable1.isEmpty()));
    }

    @DisplayName("주문 테이블을 모두 조회한다.")
    @Test
    void list() throws Exception {
        //given
        given(tableService.list()).willReturn(Arrays.asList(orderTable1, orderTable2));

        //when
        ResultActions actions = mockMvc.perform(get(URI));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].tableGroupId").isEmpty())
                .andExpect(jsonPath("$[0].numberOfGuests").value(orderTable1.getNumberOfGuests()))
                .andExpect(jsonPath("$[0].empty").value(orderTable1.isEmpty()))
                .andExpect(jsonPath("$[1].id").isNotEmpty())
                .andExpect(jsonPath("$[1].tableGroupId").isEmpty())
                .andExpect(jsonPath("$[1].numberOfGuests").value(orderTable2.getNumberOfGuests()))
                .andExpect(jsonPath("$[1].empty").value(orderTable2.isEmpty()));
    }

    @DisplayName("특정 주문 테이블의 상태를 변경한다.")
    @Test
    void changeEmpty() throws Exception {
        //given
        given(tableService.changeEmpty(orderTable2.getId(), orderTable2)).willReturn(orderTable2);

        //when
        ResultActions actions = mockMvc.perform(put(URI + "/{orderTableId}/empty", orderTable2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable2)));

        //then
        actions.andExpect(status().isOk());
    }

    @DisplayName("특정 테이블의 인원 수를 예약한다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        //given
        given(tableService.changeEmpty(orderTable1.getId(), orderTable1)).willReturn(orderTable1);

        //when
        ResultActions actions = mockMvc.perform(put(URI + "/{orderTableId}/number-of-guests", orderTable1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable1)));

        //then
        actions.andExpect(status().isOk());
    }
}

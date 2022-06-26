package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TableRestControllerTest {
    private static final String URI = "/api/tables";

    @InjectMocks
    private ObjectMapper objectMapper;
    @InjectMocks
    private TableRestController tableRestController;

    @Mock
    private TableService tableService;

    private MockMvc mockMvc;
    private OrderTable 테이블_1;
    private OrderTable 테이블_2;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(tableRestController).build();
        테이블_1 = new OrderTable();
        테이블_2 = new OrderTable();
    }

    @Test
    void post() throws Exception {
        // given
        테이블_1.setNumberOfGuests(5);
        given(tableService.create(any())).willReturn(테이블_1);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(테이블_1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numberOfGuests").value(5))
                .andDo(print());
    }

    @Test
    void get() throws Exception {
        // given
        given(tableService.list()).willReturn(Arrays.asList(테이블_1, 테이블_2));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andDo(print());
    }

    @Test
    void putForEmpty() throws Exception {
        // given
        given(tableService.changeEmpty(anyLong(), any())).willReturn(테이블_1);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{orderTableId}/empty", 1)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(테이블_1)))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    void putForNumberOfGuests() throws Exception {
        // given
        given(tableService.changeNumberOfGuests(anyLong(), any())).willReturn(테이블_1);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put(URI + "/{orderTableId}/number-of-guests", 1)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(테이블_1)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}

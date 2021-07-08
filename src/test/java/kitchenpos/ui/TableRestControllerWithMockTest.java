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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TableRestController.class)
class TableRestControllerWithMockTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    TableRestController tableRestController;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    TableService tableService;

    OrderTable 오더테이블;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tableRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        오더테이블 = new OrderTable();
        오더테이블.setId(1L);
        오더테이블.setNumberOfGuests(3);
    }

    @Test
    @DisplayName("테이블을 생성한다.")
    void create() throws Exception {
        //given
        String requestBody = objectMapper.writeValueAsString(오더테이블);
        when(tableService.create(any())).thenReturn(오더테이블);

        //when && then
        mockMvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("\"id\":1")));
    }

    @Test
    @DisplayName("전체 테이블을 조회한다.")
    void list() throws Exception {
        //given
        when(tableService.list()).thenReturn(Arrays.asList(오더테이블));

        //when && then
        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1")));
    }

    @Test
    @DisplayName("테이블의 상태를 변경한다.")
    void changeEmpty() throws Exception {
        //given
        오더테이블.setEmpty(true);
        OrderTable 오더테이블_상태변경 = new OrderTable();
        오더테이블_상태변경.setEmpty(true);
        String requestBody = objectMapper.writeValueAsString(오더테이블_상태변경);
        when(tableService.changeEmpty(any(), any())).thenReturn(오더테이블);

        //when && then
        mockMvc.perform(put("/api/tables/{orderTableId}/empty", 오더테이블.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"empty\":true")));
    }

    @Test
    @DisplayName("테이블 인원을 변경한다.")
    void changeNumberOfGuests() throws Exception {
        //given
        오더테이블.setNumberOfGuests(4);
        OrderTable 오더테이블_상태변경 = new OrderTable();
        오더테이블_상태변경.setNumberOfGuests(4);
        String requestBody = objectMapper.writeValueAsString(오더테이블_상태변경);
        when(tableService.changeNumberOfGuests(any(), any())).thenReturn(오더테이블);

        //when && then
        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 오더테이블.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"numberOfGuests\":4")));
    }
}
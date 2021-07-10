package kitchenpos.order.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.ui.TableRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TableRestControllerTest {
    MockMvc mockMvc;
    @Autowired
    TableRestController tableRestController;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TableService tableService;

    OrderTable 오더테이블;

    long 테이블_존재하지않는_테이블아이디 = 999L;
    long 테이블_그룹아이디가_존재하는_테이블아이디 = 98L;
    long 테이블_조리중인_테이블아이디 = 2L;
    long 테이블_인원변경할_테이블아이디 = 98L;
    long 테이블_상태변경할_테이블아이디 = 7L;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tableRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        오더테이블 = new OrderTable();
        오더테이블.setId(100L);
        오더테이블.setNumberOfGuests(3);
    }

    @Test
    @DisplayName("테이블을 생성한다.")
    void create() throws Exception {
        //given
        String requestBody = objectMapper.writeValueAsString(오더테이블);

        //when && then
        mockMvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("\"id\":100")));
    }

    @Test
    @DisplayName("전체 테이블을 조회한다.")
    void list() throws Exception {
        //when && then
        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("테이블의 상태를 변경한다.")
    void changeEmpty() throws Exception {
        //given
        오더테이블.setId(테이블_상태변경할_테이블아이디);
        오더테이블.setEmpty(true);
        String requestBody = objectMapper.writeValueAsString(오더테이블);

        //when && then
        mockMvc.perform(put("/api/tables/{orderTableId}/empty", 오더테이블.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"empty\":true")));
    }

    @Test
    @DisplayName("존재하지않는 테이블의 상태변경은 실패한다.")
    void changeEmpty_with_exception_when_not_exist_orderTableId() throws JsonProcessingException {
        //given
        오더테이블.setId(테이블_존재하지않는_테이블아이디);

        //when && then
        테이블_상태변경_요청_실패();
    }

    @Test
    @DisplayName("그룹테이블로 지정되어있을경우 상태변경은 실패한다.")
    void changeEmpty_with_exception() throws JsonProcessingException {
        //given
        오더테이블.setId(테이블_그룹아이디가_존재하는_테이블아이디);

        //when && then
        테이블_상태변경_요청_실패();
    }

    @Test
    @DisplayName("조리 또는 식사중일 경우 상태변경은 실패한다.")
    void changeEmpty_when_orderStatus_in_cooking_or_meal() throws JsonProcessingException {
        //given
        오더테이블.setId(테이블_조리중인_테이블아이디);

        //when && then
        테이블_상태변경_요청_실패();
    }

    @Test
    @DisplayName("테이블 인원을 변경한다.")
    void changeNumberOfGuests() throws Exception {
        //given
        오더테이블.setId(테이블_인원변경할_테이블아이디);
        오더테이블.setNumberOfGuests(44);
        String requestBody = objectMapper.writeValueAsString(오더테이블);

        //when && then
        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 오더테이블.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"numberOfGuests\":44")));
    }

    @Test
    @DisplayName("변경인원이 0명 미만일 경우 변경은 실패한다.")
    void changeNumberOfGuests_with_exception_when_person_smaller_than_zero() throws JsonProcessingException {
        //given
        오더테이블.setNumberOfGuests(-1);
        String requestBody = objectMapper.writeValueAsString(오더테이블);

        //when && then
        try {
            mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 오더테이블.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("없는테이블의 인원을 변경할 경우 변경은 실패한다.")
    void changeNumberOfGuests_with_exception_when_not_exist_orderTableId() throws JsonProcessingException {
        //given
        오더테이블.setId(테이블_존재하지않는_테이블아이디);
        String requestBody = objectMapper.writeValueAsString(오더테이블);

        //when && then
        try {
            mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 오더테이블.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("비어있는 테이블의 인원을 변경할 경우 변경은 실패한다.")
    void changeNumberOfGuests_with_exception_when_orderTable_isEmpty() throws JsonProcessingException {
        //given
        오더테이블.setId(1L);
        String requestBody = objectMapper.writeValueAsString(오더테이블);

        //when && then
        try {
            mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 오더테이블.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    private void 테이블_상태변경_요청_실패() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(오더테이블);

        //when && then
        try {
            mockMvc.perform(put("/api/tables/{orderTableId}/empty", 오더테이블.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
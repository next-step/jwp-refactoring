package kitchenpos.order.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.service.TableService;
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

    OrderTableRequest 오더테이블_리퀘스트;

    long 테이블_존재하지않는_테이블아이디 = 999L;
    long 테이블_그룹아이디가_존재하는_테이블아이디 = 108L;
    long 테이블_조리중인_테이블아이디 = 2L;
    long 테이블_인원변경할_테이블아이디 = 98L;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tableRestController)
            .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
            .alwaysDo(print())
            .build();
    }

    @Test
    @DisplayName("테이블을 생성한다.")
    void create() throws Exception {
        //given
        OrderTableRequest 오더테이블_리퀘스트 = new OrderTableRequest();
        String requestBody = objectMapper.writeValueAsString(오더테이블_리퀘스트);

        //when && then
        mockMvc.perform(post("/api/tables")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isCreated());
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
        오더테이블_리퀘스트 = new OrderTableRequest(8L, 3, true);
        String requestBody = objectMapper.writeValueAsString(오더테이블_리퀘스트);

        //when && then
        mockMvc.perform(put("/api/tables/{orderTableId}/empty", 오더테이블_리퀘스트.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("\"empty\":true")));
    }

    @Test
    @DisplayName("존재하지않는 테이블의 상태변경은 실패한다.")
    void changeEmpty_with_exception_when_not_exist_orderTableId() throws JsonProcessingException {
        //given

        오더테이블_리퀘스트 = new OrderTableRequest(테이블_존재하지않는_테이블아이디, 3, true);

        //when && then
        테이블_상태변경_요청_실패();
    }

    @Test
    @DisplayName("그룹테이블로 지정되어있을경우 상태변경은 실패한다.")
    void changeEmpty_with_exception() throws JsonProcessingException {
        오더테이블_리퀘스트 = new OrderTableRequest(테이블_그룹아이디가_존재하는_테이블아이디, 3, true);

        //when && then
        테이블_상태변경_요청_실패();
    }

    @Test
    @DisplayName("조리 또는 식사중일 경우 상태변경은 실패한다.")
    void changeEmpty_when_orderStatus_in_cooking_or_meal() throws JsonProcessingException {
        //given
        오더테이블_리퀘스트 = new OrderTableRequest(테이블_조리중인_테이블아이디, 3, true);

        //when && then
        테이블_상태변경_요청_실패();
    }

    @Test
    @DisplayName("테이블 인원을 변경한다.")
    void changeNumberOfGuests() throws Exception {
        //given
        오더테이블_리퀘스트 = new OrderTableRequest(테이블_인원변경할_테이블아이디, 44, true);

        String requestBody = objectMapper.writeValueAsString(오더테이블_리퀘스트);

        //when && then
        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 오더테이블_리퀘스트.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("\"numberOfGuests\":44")));
    }

    @Test
    @DisplayName("변경인원이 0명 미만일 경우 변경은 실패한다.")
    void changeNumberOfGuests_with_exception_when_person_smaller_than_zero()
        throws JsonProcessingException {
        //given
        오더테이블_리퀘스트 = new OrderTableRequest(1L, -4, true);
        String requestBody = objectMapper.writeValueAsString(오더테이블_리퀘스트);

        //when && then
        try {
            mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 오더테이블_리퀘스트.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("없는테이블의 인원을 변경할 경우 변경은 실패한다.")
    void changeNumberOfGuests_with_exception_when_not_exist_orderTableId()
        throws JsonProcessingException {
        //given
        오더테이블_리퀘스트 = new OrderTableRequest(테이블_존재하지않는_테이블아이디, 3, true);

        String requestBody = objectMapper.writeValueAsString(오더테이블_리퀘스트);

        //when && then
        try {
            mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 오더테이블_리퀘스트.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("비어있는 테이블의 인원을 변경할 경우 변경은 실패한다.")
    void changeNumberOfGuests_with_exception_when_orderTable_isEmpty()
        throws JsonProcessingException {
        //given
        오더테이블_리퀘스트 = new OrderTableRequest(1L, 3, true);

        String requestBody = objectMapper.writeValueAsString(오더테이블_리퀘스트);

        //when && then
        try {
            mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 오더테이블_리퀘스트.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    private void 테이블_상태변경_요청_실패() throws JsonProcessingException {
        String requestBody = objectMapper.writeValueAsString(오더테이블_리퀘스트);

        //when && then
        try {
            mockMvc.perform(put("/api/tables/{orderTableId}/empty", 오더테이블_리퀘스트.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
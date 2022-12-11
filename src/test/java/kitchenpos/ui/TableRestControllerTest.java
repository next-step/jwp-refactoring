package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

class TableRestControllerTest extends BaseTest {
    private final Long 인원_변경_테스트_좌석_ID = 9L;
    private final OrderTable 좌석 = new OrderTable(1L, 1L, 4, false);
    private final OrderTable 공석_변경_좌석 = new OrderTable(1L, 1L, 4, false);
    private final OrderTable 인원_변경_좌석 = new OrderTable(1L, 1L, 0, false);

    @Test
    void 생성() throws Exception {
        String content = objectMapper.writeValueAsString(좌석);

        생성_요청(content);
    }

    @Test
    void 조회() throws Exception {
        조회_요청();
    }

    @Test
    void 공석으로_변경() throws Exception {
        String content = objectMapper.writeValueAsString(좌석);

        생성_요청(content);

        content = objectMapper.writeValueAsString(공석_변경_좌석);

        공석_변경_요청(좌석.getId(), content);
    }

    @Test
    void 인원_변경() throws Exception {
        String content = objectMapper.writeValueAsString(인원_변경_좌석);

        인원_변경_요청(인원_변경_테스트_좌석_ID, content);
    }

    private Long 생성_요청(String content) throws Exception {
        MvcResult response = mockMvc.perform(post("/api/tables")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
        return ID_반환(response);
    }

    private Long ID_반환(MvcResult response){
        String location = response.getResponse().getHeader("Location");
        Pattern pattern = Pattern.compile("(\\d+)$");
        Matcher matcher = pattern.matcher(location);
        matcher.find();
        return Long.parseLong(matcher.group(), 10);
    }

    private void 조회_요청() throws Exception {
        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    private void 공석_변경_요청(Long id, String content) throws Exception {
        mockMvc.perform(put("/api/tables/" + id + "/empty")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    private void 인원_변경_요청(Long id, String content) throws Exception {
        mockMvc.perform(put("/api/tables/" + id + "/number-of-guests")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
}

package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

class TableGroupRestControllerTest extends BaseTest {
    private final Long 좌석_그룹_ID = 1L;
    private final OrderTable 좌석_1 = new OrderTable(1L, null, 1, true);
    private final OrderTable 좌석_2 = new OrderTable(2L, null, 1, true);
    private final List<OrderTable> 좌석_목록 = Arrays.asList(좌석_1, 좌석_2);

    @Test
    void 생성() throws Exception {
        String content = objectMapper.writeValueAsString(new TableGroup(좌석_그룹_ID, null, 좌석_목록));

        생성_요청(content);
    }

    @Test
    void 제거() throws Exception {
        String content = objectMapper.writeValueAsString(new TableGroup(좌석_그룹_ID, null, 좌석_목록));

        Long id = 생성_요청(content);

        제거_요청(id);
    }

    private Long 생성_요청(String content) throws Exception {
        MvcResult response = mockMvc.perform(post("/api/table-groups")
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

    private void 제거_요청(Long id) throws Exception {
        mockMvc.perform(delete("/api/table-groups/" + id))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}

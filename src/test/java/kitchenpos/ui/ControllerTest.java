package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

    protected final String MENU_GROUP_URI = "/api/menu-groups";
    protected final String MENU_URI = "/api/menus";
    protected final String PRODUCT_URI = "/api/products";
    protected final String TABLE_URI = "/api/tables";
    protected final String TABLE_GROUP_URI = "/api/table-groups";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected TableService tableService;

    protected void 컨트롤러_생성_요청_및_검증(String uri, String body) throws Exception {
        mockMvc.perform(post(uri)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    protected void 컨트롤러_조회_요청_및_검증(String uri) throws Exception {
        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk());
    }

    protected OrderTable 테이블을_생성한다(Long id, int numberOfGuest, boolean empty) {
        return tableService.create(new OrderTable(id, numberOfGuest, empty));
    }
}

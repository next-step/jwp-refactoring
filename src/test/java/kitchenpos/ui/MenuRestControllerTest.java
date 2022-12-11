package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class MenuRestControllerTest extends BaseTest {
    private final String 후라이드 = "후라이드";
    private final BigDecimal 후라이드_가격 = BigDecimal.valueOf(16000);
    private final Long 메뉴_그룹_ID = 1L;
    private final List<MenuProduct> 메뉴_항목 = Arrays.asList(new MenuProduct(1L, 1));

    @Test
    void 생성() throws Exception {
        String content = objectMapper.writeValueAsString(new Menu(후라이드, 후라이드_가격, 메뉴_그룹_ID, 메뉴_항목));

        생성_요청(content);
    }

    @Test
    void 조회() throws Exception {
        조회_요청();
    }

    private void 생성_요청(String content) throws Exception {
        mockMvc.perform(post("/api/menus")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    private void 조회_요청() throws Exception {
        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}

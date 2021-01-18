package kitchenpos.ui;

import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuRestControllerTest extends ControllerTest {

    @Autowired
    private MenuProductDao menuProductDao;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void create() throws Exception {
        List<MenuProduct> menuProducts = menuProductDao.findAll();
        String body = objectMapper.writeValueAsString(new Menu("바베큐치킨", BigDecimal.valueOf(15000), 4l, menuProducts));

        mockMvc.perform(post(MENU_URI)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("메뉴 목록을 조회한다")
    @Test
    void search() throws Exception {
        mockMvc.perform(get(MENU_URI))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
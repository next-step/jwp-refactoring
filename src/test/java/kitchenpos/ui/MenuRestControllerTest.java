package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@SpringBootTest
@AutoConfigureMockMvc
class MenuRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MenuDao menuDao;

    @DisplayName("메뉴를 등록한다")
    @Test
    void menu1() throws Exception {
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(1L, 1), new MenuProduct(2L, 1));
        Menu menu = new Menu("치킨세트", new BigDecimal(20_000), 1L, menuProducts);

        MvcResult result = mockMvc.perform(post("/api/menus")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(menu)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(menu.getName()))
            .andExpect(jsonPath("$.price").value(menu.getPrice().intValue()))
            .andExpect(jsonPath("$.menuGroupId").value(menu.getMenuGroupId()))
            .andExpect(jsonPath("$.menuProducts.length()").value(menuProducts.size()))
            .andReturn();

        assertThat(menuDao.findById(getId(result))).isNotEmpty();
    }

    @DisplayName("전체 메뉴를 조회한다")
    @Test
    void menu2() throws Exception {
        mockMvc.perform(get("/api/menus"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$..id").exists())
            .andExpect(jsonPath("$..name").exists())
            .andExpect(jsonPath("$..price").exists())
            .andExpect(jsonPath("$..menuGroupId").exists())
            .andExpect(jsonPath("$..menuProducts").exists())
        ;
    }

    private Long getId(MvcResult result) throws
        com.fasterxml.jackson.core.JsonProcessingException,
        UnsupportedEncodingException {
        String response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, Menu.class).getId();
    }

}

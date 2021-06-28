package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuRestController.class)
@DisplayName("MenuRestController 클래스")
class MenuRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @Nested
    @DisplayName("POST /api/menus 은")
    class Describe_create {

        @Nested
        @DisplayName("등록할 메뉴가 주어지면")
        class Context_with_menu {
            Menu givenMenu = new Menu();

            @BeforeEach
            void setUp() {
                givenMenu.setName("후라이드+후라이드");
                givenMenu.setPrice(BigDecimal.valueOf(19_000));
                givenMenu.setMenuGroupId(1L);

                MenuProduct menuProduct = new MenuProduct();
                menuProduct.setProductId(1L);
                menuProduct.setQuantity(1);

                givenMenu.setMenuProducts(Collections.singletonList(menuProduct));

                when(menuService.create(any(Menu.class)))
                        .thenReturn(givenMenu);
            }

            @DisplayName("201 Created 와 생성된 메뉴를 응답한다.")
            @Test
            void It_responds_created_with_menu() throws Exception {
                mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)

                        .content(objectMapper.writeValueAsString(givenMenu)))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(
                                objectMapper.writeValueAsString(givenMenu)
                        ));
            }
        }
    }

    @Nested
    @DisplayName("GET /api/menus 는")
    class Describe_list {

        @Nested
        @DisplayName("등록된 메뉴 그룹 목록이 있으면")
        class Context_with_menu_groups {
            List<Menu> menus;

            @BeforeEach
            void setUp() {
                Menu menu = new Menu();
                menus = Collections.singletonList(menu);
                when(menuService.list())
                        .thenReturn(menus);
            }

            @DisplayName("200 OK 와 메뉴 목록을 응답한다.")
            @Test
            void it_responds_ok_with_menus() throws Exception {
                mockMvc.perform(get("/api/menus"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(
                                objectMapper.writeValueAsString(menus)
                        ));
            }
        }
    }
}

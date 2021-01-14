package kitchenpos.menu.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("메뉴 컨트롤러 테스트")
@WebMvcTest(MenuRestController.class)
class MenuRequestRestControllerTest {
    public static final String DEFAULT_MENU_URI = "/api/menus";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

//    @DisplayName("메뉴를 생성한다.")
//    @Test
//    void 메뉴_생성() throws Exception {
//        final Menu expectedMenu = new Menu();
//        expectedMenu.setId(1L);
//        expectedMenu.setName("후라이드+후라이드");
////        expectedMenu.setPrice(BigDecimal.TEN);
//        expectedMenu.setPrice(MenuPrice.of(BigDecimal.TEN));
//
//        final String jsonTypeMenuGroup = objectMapper.writeValueAsString(expectedMenu);
//
//        given(menuService.create(any())).willReturn(expectedMenu);
//
//        mockMvc.perform(post(DEFAULT_MENU_URI)
//            .contentType(MediaType.APPLICATION_JSON_VALUE)
//            .content(jsonTypeMenuGroup))
//            .andDo(print())
//            .andExpect(status().isCreated())
//            .andExpect(jsonPath("id").value(expectedMenu.getId()))
//            .andExpect(jsonPath("name").value(expectedMenu.getName()));
//    }
//
//    @DisplayName("메뉴를 조회한다.")
//    @Test
//    void 메뉴_조회() throws Exception {
//        final List<Menu> expectedMenus = new ArrayList<>();
//
//        final Menu firstMenu = new Menu();
//        firstMenu.setId(1L);
//        firstMenu.setName("한마리메뉴");
////        firstMenu.setPrice(BigDecimal.ONE);
//        firstMenu.setPrice(MenuPrice.of(BigDecimal.ONE));
//
//        final Menu secondMenu = new Menu();
//        secondMenu.setId(2L);
//        secondMenu.setName("두마리메뉴");
////        secondMenu.setPrice(BigDecimal.ONE);
//        secondMenu.setPrice(MenuPrice.of(BigDecimal.ONE));
//
//        expectedMenus.add(firstMenu);
//        expectedMenus.add(secondMenu);
//
//        given(menuService.findAllMenus()).willReturn(expectedMenus);
//
//        mockMvc.perform(get(DEFAULT_MENU_URI)
//            .contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$[0].id").value(expectedMenus.get(0).getId()))
//            .andExpect(jsonPath("$[0].name").value(expectedMenus.get(0).getName()))
//            .andExpect(jsonPath("$[1].id").value(expectedMenus.get(1).getId()))
//            .andExpect(jsonPath("$[1].name").value(expectedMenus.get(1).getName()));
//    }
}

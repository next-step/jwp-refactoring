package kitchenpos.ui;

import kitchenpos.application.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class MenuRestControllerTest extends BaseRestControllerTest {

    @Mock
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new MenuRestController(menuService)).build();
    }

//    @DisplayName("메뉴를 생성한다.")
//    @Test
//    void create() throws Exception {
//        //given
//        String name = "menu";
//        BigDecimal price = BigDecimal.valueOf(1000);
//        Long menuGroupId = 1L;
//        List<MenuProduct> menuProducts = Arrays.asList(createMenuProduct());
//        Menu request = new Menu(null, name, price, menuGroupId, menuProducts);
//        String requestBody = objectMapper.writeValueAsString(request);
//
//        Long id = 1L;
//        given(menuService.create(any())).willReturn(new Menu(id, name, price, menuGroupId, menuProducts));
//
//        //when //then
//        mockMvc.perform(post("/api/menus")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestBody)
//        )
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(id));
//    }
//
//    @DisplayName("메뉴와 메뉴상품을 전체 조회한다.")
//    @Test
//    void list() throws Exception {
//        //given
//        Long id = 1L;
//        String name = "menu";
//        BigDecimal price = BigDecimal.valueOf(1000);
//        Long menuGroupId = 1L;
//        List<MenuProduct> menuProducts = Arrays.asList(createMenuProduct());
//
//        given(menuService.list()).willReturn(Arrays.asList(new Menu(id, name, price, menuGroupId, menuProducts)));
//
//        //when //then
//        mockMvc.perform(get("/api/menus"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(1));
//    }
//
//    private MenuProduct createMenuProduct() {
//        return new MenuProduct(1L, 1L, 1);
//    }
}
package kitchenpos.Menu.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.ui.MenuRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("메뉴 관련 기능 테스트")
@WebMvcTest(MenuRestController.class)
public class MenuRestControllerTest {
    public static final String MENUS_URI = "/api/menus";

    private MenuResponse menu1;
    private MenuResponse menu2;
    private Product product;
    private Product product2;
    private MenuProduct menuProduct;
    private MenuProduct menuProduct2;
    private MenuGroup menuGroup;

    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MenuRestController menuRestController;

    @BeforeEach
    void setUp() {
        setUpMockMvc(menuRestController);
        상품_등록();
        메뉴_상품_등록();
        메뉴_그룹_등록();
        메뉴_응답값_등록();
    }

    @Test
    @DisplayName("메뉴를 등록한다.")
    public void create() throws Exception {
        given(menuService.create(any())).willReturn(menu1);

        MenuRequest request = 메뉴_요청값_등록();
        final ResultActions actions =  메뉴_등록_요청(request);

        메뉴_등록됨(actions);
    }

    @Test
    @DisplayName("메뉴의 목록을 조회한다.")
    public void list() throws Exception {
        given(menuService.list()).willReturn(Arrays.asList(menu1, menu2));

        final ResultActions actions = 메뉴_목록_조회_요청();

        메뉴_목록_조회됨(actions);
    }

    private void setUpMockMvc(MenuRestController menuRestController) {
        mockMvc = MockMvcBuilders.standaloneSetup(menuRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();
    }

    private void 상품_등록() {
        product = new Product(new Name("뿌링클순살치킨"), new Price(new BigDecimal(20000)));
        product2 = new Product(new Name("간장순살치킨"), new Price(new BigDecimal(18000)));
    }

    private void 메뉴_상품_등록() {
        menuProduct = new MenuProduct(1L, product, 1L);
        menuProduct2 = new MenuProduct(1L, product2, 1L);
    }

    private void 메뉴_그룹_등록() {
        menuGroup = new MenuGroup(1L, "순살");
    }

    private void 메뉴_응답값_등록() {
        menu1 = MenuResponse.of(new Menu(1L,new Name("뿌링클"), new Price(new BigDecimal(18000)), menuGroup, Arrays.asList(menuProduct)));
        menu2 = MenuResponse.of(new Menu(2L, new Name("맛초킹"), new Price(new BigDecimal(18000)), menuGroup, Arrays.asList(menuProduct2)));

    }

    private MenuRequest 메뉴_요청값_등록() {
        MenuProductRequest menuProduct = new MenuProductRequest(1L, 1L,1L);
        return new MenuRequest( "뿌링클", new BigDecimal(18000), menuGroup.getId(), Arrays.asList(menuProduct));
    }

    private ResultActions 메뉴_등록_요청(MenuRequest request) throws Exception {
        return mockMvc.perform(post(MENUS_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    private void 메뉴_등록됨(ResultActions actions) throws Exception {
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", MENUS_URI + "/1"))
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("뿌링클")))
                .andExpect(content().string(containsString("18000")))
                .andExpect(content().string(containsString("1")));
    }

    private ResultActions 메뉴_목록_조회_요청() throws Exception {
        return mockMvc.perform(get(MENUS_URI)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private void 메뉴_목록_조회됨(ResultActions actions) throws Exception {
        actions.andExpect(status().isOk())
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("뿌링클")))
                .andExpect(content().string(containsString("18000")))
                .andExpect(content().string(containsString("2")))
                .andExpect(content().string(containsString("맛초킹")));
    }
}

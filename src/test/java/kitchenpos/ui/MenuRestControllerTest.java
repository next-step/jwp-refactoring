package kitchenpos.ui;

import static kitchenpos.application.MenuServiceTest.감자튀김_가격;
import static kitchenpos.application.MenuServiceTest.치즈버거_가격;
import static kitchenpos.application.MenuServiceTest.치즈버거세트_가격;
import static kitchenpos.application.MenuServiceTest.콜라_가격;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.RestControllerTest;
import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("메뉴 API")
@WebMvcTest(MenuRestController.class)
public class MenuRestControllerTest extends RestControllerTest<Menu> {

    private static final String BASE_URL = "/api/menus";

    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private ProductDao productDao;
    @Mock
    private MenuProductDao menuProductDao;
    @MockBean
    private MenuService menuService;

    private Product 치즈버거;
    private Product 감자튀김;
    private Product 콜라;
    private MenuProduct 치즈버거세트_치즈버거;
    private MenuProduct 치즈버거세트_감자튀김;
    private MenuProduct 치즈버거세트_콜라;
    private Menu 치즈버거세트;
    private MenuGroup 패스트푸드;

    @BeforeEach
    void setup() {
        패스트푸드 = new MenuGroup(1L, "패스트푸드");
        치즈버거 = new Product(1L, "치즈버거", 치즈버거_가격);
        감자튀김 = new Product(2L, "감자튀김", 감자튀김_가격);
        콜라 = new Product(3L, "콜라", 콜라_가격);
        치즈버거세트_치즈버거 = new MenuProduct(1L, 1L, 치즈버거.getId(), 1);
        치즈버거세트_감자튀김 = new MenuProduct(2L, 1L, 감자튀김.getId(), 1);
        치즈버거세트_콜라 = new MenuProduct(3L, 1L, 콜라.getId(), 1);
        치즈버거세트 = new Menu(1L, "치즈버거세트", 치즈버거세트_가격, 패스트푸드.getId(), Arrays.asList(치즈버거세트_치즈버거, 치즈버거세트_감자튀김, 치즈버거세트_콜라));
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() throws Exception {
        // Given
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(콜라.getId())).willReturn(Optional.of(콜라));
        given(menuDao.save(any())).willReturn(치즈버거세트);
        given(menuProductDao.save(치즈버거세트_치즈버거)).willReturn(치즈버거세트_치즈버거);
        given(menuService.create(any())).willReturn(치즈버거세트);

        // When & Then
        String responseBody = objectMapper.writeValueAsString(치즈버거세트);
        post(BASE_URL, 치즈버거세트)
            .andExpect(content().string(responseBody));
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() throws Exception {
        // Given
        List<Menu> menus = Collections.singletonList(치즈버거세트);
        given(menuService.list()).willReturn(menus);

        // When & Then
        String responseBody = objectMapper.writeValueAsString(menus);
        get(BASE_URL)
            .andExpect(content().string(responseBody));
    }
}

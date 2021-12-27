package menu.domain.mokito;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.assertj.core.util.*;
import org.junit.jupiter.api.*;

import fixture.*;
import menu.application.*;
import menu.domain.*;
import menu.dto.*;
import menu.repository.*;

@DisplayName("메뉴 관련(Mockito)")
class MenuMockitoTest {
    private MenuRepository menuRepository;
    private MenuGroupService menuGroupService;
    private ProductService productService;
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuRepository = mock(MenuRepository.class);
        menuGroupService = mock(MenuGroupService.class);
        productService = mock(ProductService.class);
        menuService = new MenuService(menuRepository, menuGroupService, productService);
    }

    @DisplayName("메뉴 생성하기")
    @Test
    void createTest() {
        when(menuGroupService.findById(anyLong())).thenReturn(MenuGroupFixture.메뉴그룹_한마리메뉴);
        when(productService.findById(anyLong())).thenReturn(ProductFixture.상품_후라이드치킨);
        when(menuRepository.save(any())).thenReturn(MenuFixture.메뉴_후라이드치킨);

        assertThat(Menu.of(
            MenuFixture.메뉴_후라이드치킨.getName(), MenuFixture.메뉴_후라이드치킨.getPrice(), MenuGroupFixture.메뉴그룹_한마리메뉴)).isInstanceOf(Menu.class);
    }

    @DisplayName("메뉴 조회하기")
    @Test
    void findAllTest() {
        when(menuRepository.findAll()).thenReturn(Lists.newArrayList(MenuFixture.메뉴_후라이드치킨, MenuFixture.메뉴_양념치킨));
        assertThat(menuService.findAll()).containsExactly(MenuResponse.from(MenuFixture.메뉴_후라이드치킨), MenuResponse.from(
            MenuFixture.메뉴_양념치킨));
    }
}

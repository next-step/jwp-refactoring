package kitchenpos.application;

import static kitchenpos.domain.MenuGroupTest.*;
import static kitchenpos.domain.MenuTest.*;
import static kitchenpos.domain.ProductTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Price;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 서비스")
class MenuServiceTest {

    @InjectMocks
    MenuService menuService;

    @Mock
    MenuRepository menuRepository;
    @Mock
    MenuGroupService menuGroupService;
    @Mock
    ProductService productService;

    MenuProductRequest 두마리_후라이드_요청;
    MenuProductRequest 두마리_양념치킨_요청;
    MenuRequest 두마리_양념후라이드_요청;

    MenuProduct 두마리_후라이드;
    MenuProduct 두마리_양념치킨;
    Menu 두마리_양념후라이드;

    @BeforeEach
    void setUp() {
        BigDecimal 이만_삼천 = BigDecimal.valueOf(23000);
        두마리_후라이드_요청 = new MenuProductRequest(후라이드.getId(), 1);
        두마리_양념치킨_요청 = new MenuProductRequest(양념치킨.getId(), 1);
        두마리_양념후라이드_요청 = new MenuRequest(null, "두마리_양념후라이드", 이만_삼천, 두마리메뉴.getId(),
            Arrays.asList(두마리_후라이드_요청, 두마리_양념치킨_요청));

        Price 이만_삼천_원 = Price.valueOf(23000);
        두마리_양념치킨 = new MenuProduct(후라이드, 1);
        두마리_후라이드 = new MenuProduct(양념치킨, 1);
        두마리_양념후라이드 = new Menu(100L, "두마리_양념후라이드", 이만_삼천_원, 두마리메뉴,
            MenuProducts.of(두마리_양념치킨, 두마리_후라이드));
    }

    @Test
    @DisplayName("메뉴를 생성한다")
    void create() {
        when(menuGroupService.findById(두마리메뉴.getId())).thenReturn(두마리메뉴);
        when(productService.findById(후라이드.getId())).thenReturn(후라이드);
        when(productService.findById(양념치킨.getId())).thenReturn(양념치킨);
        when(menuRepository.save(any())).thenReturn(두마리_양념후라이드);

        // when
        Menu savedMenu = menuService.create(두마리_양념후라이드_요청);

        // then
        assertThat(savedMenu.getName()).isEqualTo(두마리_양념후라이드.getName());
        assertThat(savedMenu.getMenuGroup().getId()).isEqualTo(두마리_양념후라이드.getMenuGroup().getId());
        savedMenu.getMenuProducts().forEach(menuProduct -> {
            assertThat(menuProduct.getMenu().getId()).isEqualTo(두마리_양념후라이드.getId());
        });
    }

    @Test
    @DisplayName("메뉴 생성 실패(메뉴 그룹이 존재하지 않음)")
    void create_fail2() {
        // given
        when(menuGroupService.findById(두마리_양념후라이드_요청.getMenuGroupId()))
            .thenThrow(IllegalArgumentException.class);

        // then
        assertThatThrownBy(() -> menuService.create(두마리_양념후라이드_요청))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성 실패(제품이 존재하지 않음)")
    void create_fail3() {
        // given
        when(menuGroupService.findById(두마리메뉴.getId())).thenReturn(두마리메뉴);
        when(productService.findById(후라이드.getId())).thenThrow(IllegalArgumentException.class);

        // then
        assertThatThrownBy(() -> menuService.create(두마리_양념후라이드_요청))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 가져온다")
    void list() {
        // given
        List<Menu> menus = Arrays.asList(후라이드_메뉴, 양념치킨_메뉴, 간장치킨_메뉴);
        when(menuRepository.findAll()).thenReturn(menus);

        // when
        List<Menu> menuList = menuService.list();

        // then
        assertThat(menuList.size()).isEqualTo(3);
        assertThat(menuList).containsExactly(후라이드_메뉴, 양념치킨_메뉴, 간장치킨_메뉴);
    }
}

package kitchenpos.application;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    MenuGroupDao menuGroupDao;

    @Mock
    ProductDao productDao;

    @Mock
    MenuDao menuDao;

    @Mock
    MenuProductDao menuProductDao;

    @InjectMocks
    MenuService menuService;

    Product 샐러드;
    MenuProduct 메뉴_샐러드;
    MenuGroup 채식;

    @BeforeEach
    void init() {
        샐러드 = ProductServiceTest.상품_생성(1L, "샐러드", 100);
        메뉴_샐러드 = 메뉴_상품_생성(샐러드.getId(), 2);
        채식 = MenuGroupServiceTest.메뉴_그룹_생성(1L, "채식");
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void 메뉴_생성_성공() {
        // given
        Menu 기본_메뉴 = 메뉴_생성(채식.getId(), "기본 메뉴", 200, 메뉴_샐러드);
        given(menuGroupDao.existsById(any(Long.class))).willReturn(true);
        given(productDao.findById(any(Long.class))).willReturn(Optional.of(샐러드));
        given(menuDao.save(any(Menu.class))).willReturn(기본_메뉴);
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(메뉴_샐러드);

        // when
        Menu saved = menuService.create(기본_메뉴);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo(기본_메뉴.getName());
        assertThat(saved.getPrice()).isEqualTo(기본_메뉴.getPrice());
    }

    @DisplayName("메뉴 생성에 실패한다.")
    @Test
    void 메뉴_생성_예외_가격() {
        // given
        Menu 메뉴1 = 메뉴_생성(채식.getId(), "기본 메뉴", -5, 메뉴_샐러드);
        Menu 메뉴2 = 메뉴_생성(채식.getId(), "기본 메뉴", 205, 메뉴_샐러드);
        given(menuGroupDao.existsById(any(Long.class))).willReturn(true);
        given(productDao.findById(any(Long.class))).willReturn(Optional.of(샐러드));

        // when, then
        assertThatThrownBy(() -> menuService.create(메뉴1))
                .isInstanceOf(IllegalArgumentException.class);
        // when, then
        assertThatThrownBy(() -> menuService.create(메뉴2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가 속한 그룹이 올바르지 않아 생성에 실패한다.")
    @Test
    void 메뉴_생성_예외_잘못된_그룹() {
        // given
        Menu 기본_메뉴 = 메뉴_생성(0L, "기본 메뉴", 200, 메뉴_샐러드);
        given(menuGroupDao.existsById(any(Long.class))).willReturn(false);

        // when, then
        assertThatThrownBy(() -> menuService.create(기본_메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void 메뉴_목록_조회() {
        // given
        Menu 기본_메뉴 = 메뉴_생성(채식.getId(), "기본 메뉴", 200, 메뉴_샐러드);
        Menu 할인_메뉴 = 메뉴_생성(채식.getId(), "일인 메뉴", 180, 메뉴_샐러드);
        given(menuDao.findAll()).willReturn(Arrays.asList(기본_메뉴, 할인_메뉴));

        // when
        List<Menu> menuList = menuService.list();

        // then
        assertThat(menuList).containsExactly(기본_메뉴, 할인_메뉴);
    }

    public static Menu 메뉴_생성(Long menuGroupId, String name, int price, MenuProduct... menuProducts) {
        Menu menu = new Menu();
        menu.setMenuGroupId(menuGroupId);
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuProducts(Arrays.asList(menuProducts));
        return menu;
    }

    public static MenuProduct 메뉴_상품_생성(Long productId, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}

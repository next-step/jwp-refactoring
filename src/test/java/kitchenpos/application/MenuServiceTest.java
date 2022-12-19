package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
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

import static java.util.Collections.singletonList;
import static kitchenpos.domain.MenuGroupTestFixture.createMenuGroup;
import static kitchenpos.domain.MenuTestFixture.createMenu;
import static kitchenpos.domain.MenuTestFixture.createMenuProduct;
import static kitchenpos.domain.ProductTestFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    private Product 허니콤보;
    private Product 치즈버거;
    private Product 콜라;
    private MenuGroup 치킨세트;

    private MenuGroup 햄버거세트;
    private MenuProduct 허니콤보상품;
    private MenuProduct 콜라상품;

    private MenuProduct 치즈버거상품;
    private Menu 허니콤보세트;

    private Menu 치즈버거세트;


    @BeforeEach
    void setUp() {
        // 상품
        허니콤보 = createProduct(1L, "허니콤보", BigDecimal.valueOf(18000));
        콜라 = createProduct(2L, "콜라", BigDecimal.valueOf(3000));
        치즈버거 = createProduct(3L, "치즈버거", BigDecimal.valueOf(6000));

        // 메뉴그룹
        치킨세트 = createMenuGroup(1L, "치킨세트");
        햄버거세트 = createMenuGroup(2L, "햄버거세트");

        // 메뉴 상품
        허니콤보상품 = createMenuProduct(1L, null, 허니콤보.getId(), 1L);
        콜라상품 = createMenuProduct(2L, null, 콜라.getId(), 1L);
        치즈버거상품 = createMenuProduct(3L, null, 치즈버거.getId(), 1L);


        허니콤보세트 = createMenu(1L, "허니콤보세트", BigDecimal.valueOf(20000), 치킨세트.getId(), Arrays.asList(허니콤보상품, 콜라상품));
        치즈버거세트 = createMenu(2L, "치즈버거세트", BigDecimal.valueOf(8000), 햄버거세트.getId(), Arrays.asList(치즈버거상품, 콜라상품));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void 메뉴_생성() {
        // given
        when(menuGroupDao.existsById(허니콤보세트.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(허니콤보상품.getProductId())).thenReturn(Optional.of(허니콤보));
        when(productDao.findById(콜라상품.getProductId())).thenReturn(Optional.of(콜라));
        when(menuDao.save(허니콤보세트)).thenReturn(허니콤보세트);
        when(menuProductDao.save(허니콤보상품)).thenReturn(허니콤보상품);
        when(menuProductDao.save(콜라상품)).thenReturn(콜라상품);
        // when
        Menu saveMenu = menuService.create(허니콤보세트);
        // then
        assertAll(
                () -> assertThat(saveMenu.getId()).isNotNull(),
                () -> assertThat(saveMenu.getMenuProducts()).containsExactly(허니콤보상품, 콜라상품)
        );
    }

    @DisplayName("메뉴 전체 목록을 조회한다.")
    @Test
    void 메뉴_전체_목록_조회() {
        // given
        List<Menu> menus = Arrays.asList(치즈버거세트, 허니콤보세트);
        when(menuDao.findAll()).thenReturn(menus);
        when(menuProductDao.findAllByMenuId(치즈버거세트.getId())).thenReturn(Arrays.asList(치즈버거상품, 콜라상품));
        when(menuProductDao.findAllByMenuId(허니콤보세트.getId())).thenReturn(Arrays.asList(허니콤보상품, 콜라상품));

        // when
        List<Menu> findMenus = menuService.list();

        // then
        assertAll(
                () -> assertThat(findMenus).hasSize(menus.size()),
                () -> assertThat(findMenus).containsExactly(치즈버거세트, 허니콤보세트)
        );
    }

    @DisplayName("가격이 0원 미만인 메뉴는 생성할 수 없다.")
    @Test
    void 메뉴_생성_예외1() {
        // given
        Menu menu = createMenu(1L, "치즈버거세트", BigDecimal.valueOf(-1000), 햄버거세트.getId(), Arrays.asList(치즈버거상품, 콜라상품));

        // when & then
        Assertions.assertThatThrownBy(
                () -> menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하는 메뉴 그룹에 속하지 않은 메뉴는 생성할 수 없다.")
    @Test
    void 메뉴_생성_예외2() {
        // given
        Menu menu = createMenu(1L, "치즈버거세트", BigDecimal.valueOf(8000L), 햄버거세트.getId(), singletonList(콜라상품));
        when(menuGroupDao.existsById(치즈버거세트.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(콜라상품.getProductId())).thenReturn(Optional.empty());
        // when & then
        Assertions.assertThatThrownBy(
                () -> menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격은 해당 메뉴에 존재하는 메뉴 상품들의 가격의 합보다 클 수 없다.")
    @Test
    void 메뉴_생성_예외3() {
        // given
        Menu menu = createMenu(1L, "치즈버거세트", BigDecimal.valueOf(9000L), 햄버거세트.getId(), Arrays.asList(치즈버거상품, 콜라상품));
        // when && then
        Assertions.assertThatThrownBy(
                () -> menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class);
    }

}

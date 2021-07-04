package kitchenpos.application;

import static java.util.stream.Collectors.*;
import static kitchenpos.utils.DataInitializer.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.utils.DataInitializer;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 서비스")
class MenuServiceTest {

    MenuService menuService;

    @Mock
    MenuDao menuDao;
    @Mock
    MenuGroupDao menuGroupDao;
    @Mock
    MenuProductDao menuProductDao;
    @Mock
    ProductDao productDao;

    MenuProduct 특가세트_소주;
    MenuProduct 특가세트_맥주;

    Menu 특가세트;

    @BeforeEach
    void setUp() {
        DataInitializer.reset();
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

        특가세트_소주 = new MenuProduct(소주.getId(), 1);
        특가세트_맥주 = new MenuProduct(맥주.getId(), 1);

        특가세트 = new Menu(1L, "특가세트", BigDecimal.valueOf(1000), 추천메뉴.getId(),
            Stream.of(특가세트_소주, 특가세트_맥주).collect(toList()));
    }

    @Test
    @DisplayName("메뉴를 생성한다")
    void create() {
        // given
        when(menuGroupDao.existsById(특가세트.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(특가세트_소주.getProductId())).thenReturn(Optional.of(소주));
        when(productDao.findById(특가세트_맥주.getProductId())).thenReturn(Optional.of(맥주));
        when(menuProductDao.save(특가세트_소주)).thenReturn(특가세트_소주);
        when(menuProductDao.save(특가세트_맥주)).thenReturn(특가세트_맥주);
        when(menuDao.save(특가세트)).thenReturn(특가세트);

        // when
        Menu savedMenu = menuService.create(특가세트);

        // then
        assertThat(savedMenu.getName()).isEqualTo(특가세트.getName());
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(특가세트.getMenuGroupId());
        savedMenu.getMenuProducts().forEach(menuProduct -> {
            assertThat(menuProduct.getMenuId()).isEqualTo(특가세트.getId());
        });
    }

    @Test
    @DisplayName("메뉴 생성 실패(가격없음)")
    void create_fail1() {
        // given
        특가세트 = new Menu(null, "특가세트", null, 추천메뉴.getId(),
            Stream.of(특가세트_소주, 특가세트_맥주).collect(toList()));

        // then
        assertThatThrownBy(() -> menuService.create(특가세트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성 실패(메뉴 그룹이 존재하지 않음)")
    void create_fail2() {
        // given
        when(menuGroupDao.existsById(특가세트.getMenuGroupId())).thenReturn(false);

        // then
        assertThatThrownBy(() -> menuService.create(특가세트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성 실패(제품이 존재하지 않음)")
    void create_fail3() {
        // given
        when(menuGroupDao.existsById(특가세트.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(특가세트_소주.getProductId())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> menuService.create(특가세트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성 실패(단품 가격의 합을 초과)")
    void create_fail4() {
        // given
        특가세트 = new Menu(null, "특가세트", BigDecimal.valueOf(3001), 추천메뉴.getId(),
            Arrays.asList(특가세트_소주, 특가세트_맥주));
        when(productDao.findById(특가세트_소주.getProductId())).thenReturn(Optional.of(소주));
        when(productDao.findById(특가세트_맥주.getProductId())).thenReturn(Optional.of(맥주));
        when(menuGroupDao.existsById(특가세트.getMenuGroupId())).thenReturn(true);

        // then
        assertThatThrownBy(() -> menuService.create(특가세트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 가져온다")
    void list() {
        // given
        List<Menu> menus = Arrays.asList(행복세트, 치쏘세트, 피맥세트);
        when(menuDao.findAll()).thenReturn(menus);
        menus.forEach(menu -> {
            when(menuProductDao.findAllByMenuId(menu.getId())).thenReturn(menu.getMenuProducts());
        });

        // when
        List<Menu> menuList = menuService.list();

        // then
        assertThat(menuList.size()).isEqualTo(3);
        assertThat(menuList).containsExactly(행복세트, 치쏘세트, 피맥세트);
    }
}

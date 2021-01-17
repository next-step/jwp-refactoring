package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuDao menuDao;

    @Mock
    MenuGroupDao menuGroupDao;

    @Mock
    MenuProductDao menuProductDao;

    @Mock
    ProductDao productDao;

    @InjectMocks
    MenuService menuService;

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void create1() {
        //given
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(1L, 1L, 1));

        Menu newMenu = new Menu(null, "후라이드치킨", new BigDecimal("16000"), 2L);
        newMenu.setMenuProducts(menuProducts);

        given(menuGroupDao.existsById(any()))
                .willReturn(true);
        given(productDao.findById(any()))
                .willReturn(Optional.of(new Product(1L, "후라이드치킨", new BigDecimal("16000"))));
        given(menuDao.save(newMenu))
                .willReturn(new Menu(1L, "후라이드치킨", new BigDecimal("16000"), 2L));

        //when
        Menu createMenu = menuService.create(newMenu);

        //then
        assertThat(createMenu.getId()).isEqualTo(1L);
        assertThat(createMenu.getName()).isEqualTo("후라이드치킨");
        assertThat(createMenu.getPrice()).isEqualTo(new BigDecimal("16000"));
        assertThat(createMenu.getMenuGroupId()).isEqualTo(2L);
    }

    @DisplayName("메뉴를 등록할 수 있다 - 메뉴의 가격은 0 원 이상이어야 한다.")
    @Test
    void create2() {
        Menu newMenu = new Menu(null, "후라이드치킨", new BigDecimal("-1"), 2L);
        assertThatThrownBy(() -> menuService.create(newMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록할 수 있다 - 등록된 메뉴그룹이 선택되어 있어야 한다.")
    @Test
    void create3() {
        // given
        given(menuGroupDao.existsById(any())).willReturn(false);

        Menu newMenu = new Menu(null, "후라이드치킨", new BigDecimal("16000"), 2L);
        // when
        // then
        assertThatThrownBy(() -> menuService.create(newMenu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴그룹이 없습니다.");
    }

    @DisplayName("메뉴를 등록할 수 있다 - 상품목록의 각 상품이 이미 등록되어 있어야 한다.")
    @Test
    void create4() {
        //given
        given(menuGroupDao.existsById(any()))
                .willReturn(true);
        given(productDao.findById(any()))
                .willReturn(Optional.empty());

        Menu newMenu = new Menu(null, "후라이드치킨", new BigDecimal("16000"), 2L);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(1L, 1L, 1));
        newMenu.setMenuProducts(menuProducts);

        //when
        //then
        assertThatThrownBy(() -> menuService.create(newMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록할 수 있다 - 메뉴의 가격이 상품목록 총합 가격보다 더 크면 안됨")
    @Test
    void create5() {
        //given
        given(menuGroupDao.existsById(any()))
                .willReturn(true);
        given(productDao.findById(any()))
                .willReturn(Optional.of(new Product(1L, "후라이드치킨", new BigDecimal("14000"))));

        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(1L, 1L, 1));

        Menu newMenu = new Menu(null, "후라이드치킨", new BigDecimal("16000"), 2L);
        newMenu.setMenuProducts(menuProducts);

        //when
        //then
        assertThatThrownBy(() -> menuService.create(newMenu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격이 상품목록 총합 가격보다 더 큽니다.");
    }

    @DisplayName("메뉴 목록을 조회할 수 있다")
    @Test
    void list() {
        //given

        given(menuDao.findAll())
                .willReturn(
                        Arrays.asList(
                                new Menu(1L, "후라이드치킨", new BigDecimal("16000"), 1L),
                                new Menu(2L, "양념치킨", new BigDecimal("16000"), 2L)
                        )
                );
        given(menuProductDao.findAllByMenuId(1L))
                .willReturn(Collections.singletonList(new MenuProduct(1L, 1L, 1L)));
        given(menuProductDao.findAllByMenuId(2L))
                .willReturn(Collections.singletonList(new MenuProduct(2L, 2L, 1L)));

        //when
        List<Menu> menus = menuService.list();

        //then
        assertThat(menus.size()).isEqualTo(2);
        assertThat(menus.get(0).getName()).isEqualTo("후라이드치킨");
        assertThat(menus.get(1).getName()).isEqualTo("양념치킨");
    }
}

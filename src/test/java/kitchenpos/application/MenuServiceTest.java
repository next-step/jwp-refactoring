package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    private int 후라이드가격;
    private Product 후라이드;
    private Product 콜라;
    private MenuProduct 후라이드메뉴상품;
    private MenuProduct 콜라메뉴상품;
    private MenuGroup 메뉴분류단품;
    private MenuGroup 메뉴분류세트;
    private Menu 후라이드한마리;
    private Menu 후라이드세트;

    @BeforeEach
    void Setup() {
        후라이드가격 = 15000;

        메뉴분류단품 = new MenuGroup(1L, "메뉴분류단품");
        메뉴분류세트 = new MenuGroup(2L, "메뉴분류세트");

        후라이드 = new Product(1L, "후라이드", BigDecimal.valueOf(후라이드가격));
        콜라 = new Product(2L, "콜라", BigDecimal.valueOf(1000));

        후라이드메뉴상품 = new MenuProduct(1L, null, 1L, 1L);
        콜라메뉴상품 = new MenuProduct(2L, null, 2L, 1L);

        후라이드한마리 = new Menu(1L, "후라이드한마리", BigDecimal.valueOf(15000), 1L, Collections.singletonList(후라이드메뉴상품));
        후라이드세트 = new Menu(2L, "후라이드세트", BigDecimal.valueOf(16000), 2L, Arrays.asList(후라이드메뉴상품, 콜라메뉴상품));
    }

    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void create() {
        //given
        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productDao.findById(1L)).willReturn(Optional.ofNullable(후라이드));
        given(menuProductDao.save(any())).willReturn(후라이드메뉴상품);
        given(menuDao.save(any())).willReturn(후라이드한마리);

        //when
        Menu menu = menuService.create(후라이드한마리);
        //then
        assertAll(
                () -> assertThat(menu.getId()).isNotNull(),
                () -> assertThat(menu.getName()).isEqualTo("후라이드한마리"),
                () -> assertThat(menu.getPrice()).isEqualTo(BigDecimal.valueOf(15000)),
                () -> assertThat(menu.getMenuProducts()).contains(후라이드메뉴상품),
                () -> assertThat(menu.getMenuGroupId()).isEqualTo(1L)
        );

    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        given(menuDao.findAll()).willReturn(Arrays.asList(후라이드한마리, 후라이드세트));
        given(menuProductDao.findAllByMenuId(1L)).willReturn(Collections.singletonList(후라이드메뉴상품));
        given(menuProductDao.findAllByMenuId(2L)).willReturn(Arrays.asList(후라이드메뉴상품, 콜라메뉴상품));

        //when
        List<Menu> menus = menuService.list();

        //then
        assertThat(menus).contains(후라이드한마리, 후라이드세트);

    }

    @DisplayName("메뉴의 상품 가격이 null 이면 에러가 발생한다.")
    @Test
    void priceNull() {
        //given
        Menu 후라이드한마리 = new Menu(1L, "후라이드한마리", null, 1L, Collections.singletonList(후라이드메뉴상품));
        //when & then
        assertThatThrownBy(() -> menuService.create(후라이드한마리)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 상품 가격이 0 보다 작으면 에러가 발생한다.")
    @Test
    void priceZero() {
        //given
        Menu 후라이드한마리 = new Menu(1L, "후라이드한마리", BigDecimal.valueOf(-1), 1L, Collections.singletonList(후라이드메뉴상품));
        //when & then
        assertThatThrownBy(() -> menuService.create(후라이드한마리)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 메뉴 그룹이 존재하지 않으면 에러가 발생한다.")
    @Test
    void menuGroup() {
        //given
        Menu 후라이드한마리 = new Menu(1L, "후라이드한마리", BigDecimal.valueOf(1000), 1L, Collections.singletonList(후라이드메뉴상품));
        given(menuGroupDao.existsById(1L)).willReturn(false);
        //when & then
        assertThatThrownBy(() -> menuService.create(후라이드한마리)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 구성 상품의 가격 * 수량을 모두 더한 값보다 크면 에러가 발생한다.")
    @Test
    void sumPrice() {
        //given
        Menu 후라이드한마리 = new Menu(1L, "후라이드한마리", BigDecimal.valueOf(후라이드가격 + 100), 1L,
                Collections.singletonList(후라이드메뉴상품));

        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productDao.findById(1L)).willReturn(Optional.ofNullable(후라이드));

        //when & then
        assertThatThrownBy(() -> menuService.create(후라이드한마리)).isInstanceOf(IllegalArgumentException.class);

    }

}

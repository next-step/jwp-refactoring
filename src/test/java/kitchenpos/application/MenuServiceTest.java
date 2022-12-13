package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupService menuGroupService;
    @Mock
    private MenuProductRepository menuProductRepository;
    @Mock
    private ProductService productService;
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupService, menuProductRepository, productService);
    }

    @Test
    void 메뉴를_등록할_수_있다() {
        MenuProductRequest menuProduct = new MenuProductRequest(1L, 2L, 1L, 1l);
        Product 후라이드치킨_상품 = new Product(1L, "후라이드치킨", new BigDecimal(16000.00));
        MenuRequest 후라이드치킨_메뉴 = new MenuRequest(1L, "후라이드치킨", new BigDecimal(16000.00), 1L,
                Collections.singletonList(menuProduct));
        MenuGroup 메뉴그룹 = new MenuGroup(1L, "메뉴그룹");
        Menu 메뉴 = new Menu("후라이드치킨", new BigDecimal(16000.00), 메뉴그룹);
        given(menuGroupService.findById(any())).willReturn(메뉴그룹);
        given(productService.findById(any())).willReturn(후라이드치킨_상품);
        given(menuRepository.save(any())).willReturn(메뉴);

        Menu menu = menuService.create(후라이드치킨_메뉴);

        assertThat(menu).isEqualTo(메뉴);
    }

    @Test
    void 메뉴등록시_가격이_0원_미만이면_오류발생() {
        given(menuGroupService.findById(any())).willReturn(new MenuGroup(1L, "메뉴그룹"));
        MenuRequest 메뉴_가격이_0원_미만일_경우 = new MenuRequest(1L, "잘못된_가격이_측정된_메뉴", new BigDecimal(-1), 1L,
                Collections.singletonList(new MenuProductRequest(1L, 1L, 1L, 1l)));

        ThrowingCallable 잘못된_가격의_메뉴_등록 = () -> menuService.create(메뉴_가격이_0원_미만일_경우);

        assertThatIllegalArgumentException().isThrownBy(잘못된_가격의_메뉴_등록);
    }

    @Test
    void 메뉴등록시_가격이_null_이면_오류발생() {
        given(menuGroupService.findById(any())).willReturn(new MenuGroup(1L, "메뉴그룹"));
        MenuRequest 메뉴_가격이_null_일_경우 = new MenuRequest(1L, "잘못된_가격이_측정된_메뉴", null, 1L,
                Collections.singletonList(new MenuProductRequest(1L, 1L, 1L, 1l)));

        ThrowingCallable 잘못된_가격의_메뉴_등록 = () -> menuService.create(메뉴_가격이_null_일_경우);

        assertThatIllegalArgumentException().isThrownBy(잘못된_가격의_메뉴_등록);
    }

    @Test
    void 메뉴의_가격은_메뉴_상품들_가격의_합보다_낮아야_한다() {
        MenuProductRequest menuProduct = new MenuProductRequest(1L, 1L, 1L, 1l);
        MenuProductRequest menuProduct2 = new MenuProductRequest(2L, 1L, 2L, 1l);
        Product 후라이드치킨_상품 = new Product(1L, "후라이드치킨", new BigDecimal(16000.00));
        MenuRequest 메뉴의_가격이_메뉴_상품들_가격의_합보다_높은경우 = new MenuRequest(1L, "후라이드치킨", new BigDecimal(40000.00), 1L,
                Arrays.asList(menuProduct, menuProduct2));
        given(menuGroupService.findById(any())).willReturn(new MenuGroup(1L, "메뉴그룹"));
        given(productService.findById(any())).willReturn(후라이드치킨_상품);

        ThrowingCallable 잘못된_가격의_메뉴_등록 = () -> menuService.create(메뉴의_가격이_메뉴_상품들_가격의_합보다_높은경우);

        assertThatIllegalArgumentException().isThrownBy(잘못된_가격의_메뉴_등록);
    }

    @Test
    void 등록_된_메뉴그룹만_지정할_수_있다() {
        MenuRequest 후라이드치킨 = new MenuRequest(1L, "후라이드치킨", new BigDecimal(16000.00), 1L, null);
        given(menuGroupService.findById(any())).willThrow(IllegalArgumentException.class);

        ThrowingCallable 메뉴그룹이_등록되어_있지_않다 = () -> menuService.create(후라이드치킨);

        assertThatIllegalArgumentException().isThrownBy(메뉴그룹이_등록되어_있지_않다);
    }

    @Test
    void 등록_된_상품만_지정할_수_있다() {
        MenuProductRequest menuProduct = new MenuProductRequest(1L, 2L, 1L, 1l);
        MenuRequest 후라이드치킨 = new MenuRequest(1L, "후라이드치킨", new BigDecimal(16000.00), 1L,
                Collections.singletonList(menuProduct));
        given(menuGroupService.findById(any())).willReturn(new MenuGroup(1L, "메뉴그룹"));
        given(productService.findById(any())).willThrow(IllegalArgumentException.class);

        ThrowingCallable 상품이_등록되어_있지_않다 = () -> menuService.create(후라이드치킨);

        assertThatIllegalArgumentException().isThrownBy(상품이_등록되어_있지_않다);
    }

    @Test
    void 메뉴_목록을_조회할_수_있다() {
        Menu 후라이드치킨 = new Menu("후라이드치킨", new BigDecimal(16000.00), null);
        Menu 양념치킨 = new Menu("양념치킨", new BigDecimal(18000.00), null);
        given(menuRepository.findAll()).willReturn(Arrays.asList(후라이드치킨, 양념치킨));

        List<Menu> menus = menuService.list();

        assertAll(
                () -> assertThat(menus).hasSize(2),
                () -> assertThat(menus).containsExactly(후라이드치킨, 양념치킨)
        );
    }
}

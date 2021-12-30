package kitchenpos.menu.applicaiton;

import kitchenpos.common.fixtrue.MenuGroupFixture;
import kitchenpos.common.fixtrue.MenuProductFixture;
import kitchenpos.common.fixtrue.ProductFixture;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuGroupService menuGroupService;

    @Mock
    ProductService productService;

    @InjectMocks
    MenuService menuService;

    MenuGroup 두마리치킨;
    Product 후라이드치킨;

    MenuRequest 후라이드_후라이드_요청;
    Menu 후라이드_후라이드;
    MenuResponse 후라이드_후라이드_응답;

    MenuProductRequest 후라이드_후라이드_메뉴_상품_요청;
    MenuProduct 후라이드_후라이드_메뉴_상품;

    @BeforeEach
    void setUp() {
        후라이드치킨 = ProductFixture.of("후라이드치킨", BigDecimal.valueOf(16000));
        두마리치킨 = MenuGroupFixture.from("두마리치킨");

        후라이드_후라이드_요청 = MenuRequest.of(
                "후라이드+후라이드",
                BigDecimal.valueOf(31000),
                두마리치킨.getId(),
                Collections.singletonList(MenuProductRequest.of(후라이드치킨.getId(), 2)));

        후라이드_후라이드_메뉴_상품_요청 = MenuProductRequest.of(후라이드치킨.getId(), 2);
        후라이드_후라이드_메뉴_상품 = MenuProductFixture.of(후라이드치킨, 2);
        후라이드_후라이드 = Menu.of(
                후라이드_후라이드_요청.getName(),
                후라이드_후라이드_요청.getPrice(),
                두마리치킨,
                MenuProducts.from(Collections.singletonList(후라이드_후라이드_메뉴_상품)));
        후라이드_후라이드_응답 = MenuResponse.from(후라이드_후라이드);
    }

    @Test
    void 메뉴_생성() {
        // given
        given(menuGroupService.findMenuGroupById(후라이드_후라이드_요청.getMenuGroupId())).willReturn(두마리치킨);
        given(productService.findProductById(후라이드_후라이드_요청.getMenuProducts().get(0).getProductId())).willReturn(후라이드치킨);
        given(menuRepository.save(any())).willReturn(후라이드_후라이드);

        // when
        MenuResponse actual = menuService.create(후라이드_후라이드_요청);

        // then
        assertAll(() -> {
            assertThat(actual.getName()).isEqualTo("후라이드+후라이드");
            assertThat(actual.getPrice()).isEqualTo(BigDecimal.valueOf(31000));
        });
    }

    @Test
    void 메뉴_생성_시_등록하려는_메뉴는_메뉴_그룹이_존재해야한다() {
        // given
        MenuRequest 메뉴_그룹_없는_후라이드_후라이드_요청 = MenuRequest.of(
                "후라이드+후라이드",
                BigDecimal.valueOf(31000),
                null,
                Collections.singletonList(MenuProductRequest.of(후라이드치킨.getId(), 2)));

        given(menuGroupService.findMenuGroupById(any())).willThrow(new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다."));

        // then
        ThrowingCallable throwingCallable = () -> menuService.create(메뉴_그룹_없는_후라이드_후라이드_요청);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("메뉴 그룹이 존재하지 않습니다.");
    }

    @DisplayName("등록하려는 `메뉴`의 가격은 `메뉴 상품`들의 수량 * `상품`의 가격을 모두 더한 금액보다 작아야한다")
    @Test
    void 등록하려는_메뉴의_가격은_메뉴_상품_들의_가격을_모두_더한_금액보다_작아야한다() {
        // given
        MenuRequest 비싼_후라이드_후라이드_요청 = MenuRequest.of(
                "후라이드+후라이드",
                BigDecimal.valueOf(33000),
                두마리치킨.getId(),
                Collections.singletonList(MenuProductRequest.of(후라이드치킨.getId(), 2)));

        given(menuGroupService.findMenuGroupById(any())).willReturn(두마리치킨);
        given(productService.findProductById(any())).willReturn(후라이드치킨);


        // then
        ThrowingCallable throwingCallable = () -> menuService.create(비싼_후라이드_후라이드_요청);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("메뉴의 가격은 메뉴 상품들의 수량 * 상품의 가격을 모두 더한 금액 보다 작거나 같아야 합니다.");
    }

    @Test
    void 메뉴_조회() {
        // given
        List<Menu> menus = Collections.singletonList(후라이드_후라이드);
        given(menuRepository.findAll()).willReturn(menus);

        // when
        List<MenuResponse> actual = menuService.list();

        // then
        Assertions.assertAll(() -> {
            assertThat(actual).hasSize(1);
        });
    }
}

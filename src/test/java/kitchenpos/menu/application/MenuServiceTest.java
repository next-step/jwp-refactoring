package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu_group.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.fixture.MenuFixture;
import kitchenpos.menu_group.fixture.MenuGroupFixture;
import kitchenpos.menu.fixture.MenuProductFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.fixture.ProductFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    MenuRepository menuRepository;
    @Mock
    MenuGroupRepository menuGroupRepository;
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    MenuService menuService;

    private Product 강정치킨;
    private MenuGroup 추천메뉴;
    private Menu 더블강정;
    private Menu 트리플강정;

    @BeforeEach
    void setup() {
        강정치킨 = ProductFixture.create(1L, "강정치킨", BigDecimal.valueOf(17_000));
        추천메뉴 = MenuGroupFixture.create(1L, "추천메뉴");

        MenuProduct 더블강정_메뉴_상품 = MenuProductFixture.create(강정치킨, 2L);
        MenuProduct 트리플강정_메뉴_상품 = MenuProductFixture.create(강정치킨, 3L);

        더블강정 = MenuFixture.create(1L, "더블강정", BigDecimal.valueOf(32_000), 추천메뉴, 더블강정_메뉴_상품);
        트리플강정 = MenuFixture.create(2L, "트리플강정", BigDecimal.valueOf(45_000), 추천메뉴, 트리플강정_메뉴_상품);

        더블강정_메뉴_상품.updateMenu(더블강정);
        트리플강정_메뉴_상품.updateMenu(트리플강정);
    }

    @DisplayName("메뉴 등록 확인")
    @Test
    void 메뉴_등록_확인() {
        // given
        MenuProductRequest 메뉴_상품 = MenuProductRequest.of(강정치킨.getId(), 2L);
        MenuRequest 메뉴_등록_요청_데이터 = MenuRequest.of("더블강정", BigDecimal.valueOf(32_000), 추천메뉴.getId(), Collections.singletonList(메뉴_상품));

        given(menuGroupRepository.findById(any(Long.TYPE))).willReturn(Optional.of(추천메뉴));
        given(productRepository.findById(any(Long.TYPE))).willReturn(Optional.of(강정치킨));
        given(menuRepository.save(any(Menu.class))).willReturn(더블강정);

        // when
        MenuResponse 등록된_메뉴 = menuService.create(메뉴_등록_요청_데이터);

        // then
        assertAll(
                () -> assertThat(등록된_메뉴.getName()).isEqualTo(메뉴_등록_요청_데이터.getName()),
                () -> assertThat(등록된_메뉴.getPrice()).isEqualTo(메뉴_등록_요청_데이터.getPrice())
        );
    }

    @DisplayName("메뉴 목록 확인")
    @Test
    void 메뉴_목록_확인() {
        // given
        given(menuRepository.findAll()).willReturn(Arrays.asList(더블강정, 트리플강정));

        // when
        List<MenuResponse> 메뉴_목록 = menuService.list();

        // then
        assertThat(메뉴_목록).hasSize(2);
    }

    @DisplayName("메뉴 등록 실패 테스트")
    @Nested
    class CreateFailTest {
        @DisplayName("가격이 존재하지 않음")
        @Test
        void 가격이_존재하지_않음() {
            // given
            MenuProductRequest 메뉴_상품 = MenuProductRequest.of(강정치킨.getId(), 2L);
            MenuRequest 메뉴_등록_요청_데이터 = MenuRequest.of("더블강정", null, 추천메뉴.getId(), Collections.singletonList(메뉴_상품));

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> menuService.create(메뉴_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 0원 이상이 아님")
        @Test
        void 가격이_0원_이상이_아님() {
            MenuProductRequest 메뉴_상품 = MenuProductRequest.of(강정치킨.getId(), 2L);
            MenuRequest 메뉴_등록_요청_데이터 = MenuRequest.of("더블강정", BigDecimal.valueOf(-1), 추천메뉴.getId(), Collections.singletonList(메뉴_상품));

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> menuService.create(메뉴_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 그룹이 존재하지 않음")
        @Test
        void 메뉴_그룹이_존재하지_않음() {
            // given
            MenuProductRequest 메뉴_상품 = MenuProductRequest.of(강정치킨.getId(), 2L);
            MenuRequest 메뉴_등록_요청_데이터 = MenuRequest.of("더블강정", BigDecimal.valueOf(32_000), null, Collections.singletonList(메뉴_상품));

            given(menuGroupRepository.findById(any())).willReturn(Optional.empty());

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> menuService.create(메뉴_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 상품이 포함되지 않음")
        @Test
        void 메뉴_상품이_포함되지_않음() {
            // given
            MenuRequest 메뉴_등록_요청_데이터 = MenuRequest.of("더블강정", BigDecimal.valueOf(32_000), 추천메뉴.getId(), Collections.emptyList());

            given(menuGroupRepository.findById(any(Long.TYPE))).willReturn(Optional.of(추천메뉴));

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> menuService.create(메뉴_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 상품이 존재하지 않음")
        @Test
        void 메뉴_상품이_존재하지_않음() {
            // given
            MenuProductRequest 메뉴_상품 = MenuProductRequest.of(강정치킨.getId(), 2L);
            MenuRequest 메뉴_등록_요청_데이터 = MenuRequest.of("더블강정", BigDecimal.valueOf(32_000), 추천메뉴.getId(), Collections.singletonList(메뉴_상품));

            given(menuGroupRepository.findById(any(Long.TYPE))).willReturn(Optional.of(추천메뉴));
            given(productRepository.findById(any(Long.TYPE))).willReturn(Optional.empty());

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> menuService.create(메뉴_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 가격이 메뉴 상품의 총합보다 큼")
        @Test
        void 메뉴_가격이_메뉴_상품의_총합보다_큼() {
            // given
            MenuProductRequest 메뉴_상품 = MenuProductRequest.of(강정치킨.getId(), 2L);
            MenuRequest 메뉴_등록_요청_데이터 = MenuRequest.of("더블강정", BigDecimal.valueOf(35_000), 추천메뉴.getId(), Collections.singletonList(메뉴_상품));

            given(menuGroupRepository.findById(any(Long.TYPE))).willReturn(Optional.of(추천메뉴));
            given(productRepository.findById(any(Long.TYPE))).willReturn(Optional.of(강정치킨));

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> menuService.create(메뉴_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }
}

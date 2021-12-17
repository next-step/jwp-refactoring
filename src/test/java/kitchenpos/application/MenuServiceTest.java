package kitchenpos.application;

import kitchenpos.application.fixture.MenuFixture;
import kitchenpos.application.fixture.MenuGroupFixture;
import kitchenpos.application.fixture.MenuProductFixture;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
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
    ProductRepository productRepository;

    @InjectMocks
    MenuService menuService;

    private Product 강정치킨;
    private MenuGroup 추천메뉴;
    private MenuProduct 더블강정_메뉴_상품;
    private Menu 더블강정;
    private Menu 트리플강정;

    @BeforeEach
    void setup() {
        강정치킨 = ProductFixture.create(1L, "강정치킨", BigDecimal.valueOf(17_000));
        추천메뉴 = MenuGroupFixture.create(1L, "추천메뉴");

        더블강정_메뉴_상품 = MenuProductFixture.create(강정치킨, 2);
        MenuProduct 트리플강정_메뉴_상품 = MenuProductFixture.create(강정치킨, 3);

        더블강정 = MenuFixture.create(1L, "더블강정", BigDecimal.valueOf(32_000), 추천메뉴, 더블강정_메뉴_상품);
        트리플강정 = MenuFixture.create(2L, "트리플강정", BigDecimal.valueOf(45_000), 추천메뉴, 트리플강정_메뉴_상품);
    }

    @DisplayName("메뉴 등록 확인")
    @Test
    void 메뉴_등록_확인() {
        // given
        MenuProduct 메뉴_상품 = new MenuProduct();
        메뉴_상품.setProductId(강정치킨.getId());
        메뉴_상품.setQuantity(2);

        Menu 메뉴_등록_요청_데이터 = new Menu();
        메뉴_등록_요청_데이터.setName("더블강정");
        메뉴_등록_요청_데이터.setPrice(BigDecimal.valueOf(32_000));
        메뉴_등록_요청_데이터.setMenuGroupId(추천메뉴.getId());
        메뉴_등록_요청_데이터.setMenuProducts(Collections.singletonList(메뉴_상품));

        given(menuGroupDao.existsById(any(Long.TYPE))).willReturn(true);
        given(productRepository.findById(any(Long.TYPE))).willReturn(Optional.of(강정치킨));
        given(menuDao.save(any(Menu.class))).willReturn(더블강정);
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(더블강정_메뉴_상품);

        // when
        Menu 등록된_메뉴 = menuService.create(메뉴_등록_요청_데이터);

        // then
        assertThat(등록된_메뉴).isEqualTo(더블강정);
        assertThat(등록된_메뉴.getMenuProducts()).first()
                .isEqualTo(더블강정_메뉴_상품);
    }

    @DisplayName("메뉴 목록 확인")
    @Test
    void 메뉴_목록_확인() {
        // given
        given(menuDao.findAll()).willReturn(Arrays.asList(더블강정, 트리플강정));

        // when
        List<Menu> 메뉴_목록 = menuService.list();

        // then
        assertThat(메뉴_목록).containsExactly(더블강정, 트리플강정);
    }

    @DisplayName("메뉴 등록 실패 테스트")
    @Nested
    class CreateFailTest {
        @DisplayName("가격이 존재하지 않음")
        @Test
        void 가격이_존재하지_않음() {
            // given
            MenuProduct 메뉴_상품 = new MenuProduct();
            메뉴_상품.setProductId(강정치킨.getId());
            메뉴_상품.setQuantity(2);

            Menu 메뉴_등록_요청_데이터 = new Menu();
            메뉴_등록_요청_데이터.setName("더블강정");
            메뉴_등록_요청_데이터.setPrice(null);
            메뉴_등록_요청_데이터.setMenuGroupId(추천메뉴.getId());
            메뉴_등록_요청_데이터.setMenuProducts(Collections.singletonList(메뉴_상품));

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> menuService.create(메뉴_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 0원 이상이 아님")
        @Test
        void 가격이_0원_이상이_아님() {
            // given
            MenuProduct 메뉴_상품 = new MenuProduct();
            메뉴_상품.setProductId(강정치킨.getId());
            메뉴_상품.setQuantity(2);

            Menu 메뉴_등록_요청_데이터 = new Menu();
            메뉴_등록_요청_데이터.setName("더블강정");
            메뉴_등록_요청_데이터.setPrice(BigDecimal.valueOf(-1));
            메뉴_등록_요청_데이터.setMenuGroupId(추천메뉴.getId());
            메뉴_등록_요청_데이터.setMenuProducts(Collections.singletonList(메뉴_상품));

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> menuService.create(메뉴_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 그룹이 존재하지 않음")
        @Test
        void 메뉴_그룹이_존재하지_않음() {
            // given
            MenuProduct 메뉴_상품 = new MenuProduct();
            메뉴_상품.setProductId(강정치킨.getId());
            메뉴_상품.setQuantity(2);

            Menu 메뉴_등록_요청_데이터 = new Menu();
            메뉴_등록_요청_데이터.setName("더블강정");
            메뉴_등록_요청_데이터.setPrice(BigDecimal.valueOf(32_000));
            메뉴_등록_요청_데이터.setMenuGroupId(추천메뉴.getId());
            메뉴_등록_요청_데이터.setMenuProducts(Collections.singletonList(메뉴_상품));

            given(menuGroupDao.existsById(any(Long.TYPE))).willReturn(false);

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> menuService.create(메뉴_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 상품이 포함되지 않음")
        @Test
        void 메뉴_상품이_포함되지_않음() {
            // given
            Menu 메뉴_등록_요청_데이터 = new Menu();
            메뉴_등록_요청_데이터.setName("더블강정");
            메뉴_등록_요청_데이터.setPrice(BigDecimal.valueOf(32_000));
            메뉴_등록_요청_데이터.setMenuGroupId(추천메뉴.getId());
            메뉴_등록_요청_데이터.setMenuProducts(Collections.emptyList());

            given(menuGroupDao.existsById(any(Long.TYPE))).willReturn(true);

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> menuService.create(메뉴_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 상품이 존재하지 않음")
        @Test
        void 메뉴_상품이_존재하지_않음() {
            // given
            MenuProduct 메뉴_상품 = new MenuProduct();
            메뉴_상품.setProductId(강정치킨.getId());
            메뉴_상품.setQuantity(2);

            Menu 메뉴_등록_요청_데이터 = new Menu();
            메뉴_등록_요청_데이터.setName("더블강정");
            메뉴_등록_요청_데이터.setPrice(BigDecimal.valueOf(32_000));
            메뉴_등록_요청_데이터.setMenuGroupId(추천메뉴.getId());
            메뉴_등록_요청_데이터.setMenuProducts(Collections.singletonList(메뉴_상품));

            given(menuGroupDao.existsById(any(Long.TYPE))).willReturn(true);
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
            MenuProduct 메뉴_상품 = new MenuProduct();
            메뉴_상품.setProductId(강정치킨.getId());
            메뉴_상품.setQuantity(2);

            Menu 메뉴_등록_요청_데이터 = new Menu();
            메뉴_등록_요청_데이터.setName("더블강정");
            메뉴_등록_요청_데이터.setPrice(BigDecimal.valueOf(35_000));
            메뉴_등록_요청_데이터.setMenuGroupId(추천메뉴.getId());
            메뉴_등록_요청_데이터.setMenuProducts(Collections.singletonList(메뉴_상품));

            given(menuGroupDao.existsById(any(Long.TYPE))).willReturn(true);
            given(productRepository.findById(any(Long.TYPE))).willReturn(Optional.of(강정치킨));

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> menuService.create(메뉴_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }
}

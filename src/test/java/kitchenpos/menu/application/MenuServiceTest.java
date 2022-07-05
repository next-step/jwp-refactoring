package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.ProductRepository;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 서비스")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuGroupService menuGroupService;

    @InjectMocks
    protected MenuService menuService;

    private MenuGroup 분식_그룹;

    @BeforeEach
    void setUp() {
        // given
        분식_그룹 = new MenuGroup(1L, "분식");
    }

    @DisplayName("전체 메뉴를 조회할 수 있다.")
    @Test
    void 메뉴_조회() {
        // given
        Product 떡볶이 = new Product(1L, "떡볶이", Price.from(1000));
        Product 순대 = new Product(2L, "순대", Price.from(1500));

        MenuProduct 떡볶이_메뉴_상품 = MenuProduct.of(떡볶이, 1L);
        MenuProduct 순대_메뉴_상품 = MenuProduct.of(순대, 1L);
        MenuProducts 떡볶이_순대 = new MenuProducts(Arrays.asList(떡볶이_메뉴_상품, 순대_메뉴_상품));

        Menu 떡볶이_세트1 = new Menu("떡볶이 세트1", BigDecimal.ZERO, 분식_그룹, 떡볶이_순대);
        Menu 떡볶이_세트2 = new Menu("떡볶이 세트2", BigDecimal.ZERO, 분식_그룹, 떡볶이_순대);
        Menu 떡볶이_세트3 = new Menu("떡볶이 세트3", BigDecimal.ZERO, 분식_그룹, 떡볶이_순대);

        given(menuRepository.findAll()).willReturn(Arrays.asList(떡볶이_세트1, 떡볶이_세트2, 떡볶이_세트3));

        // when
        List<MenuResponse> menus = menuService.list();

        // then
        assertThat(menus).hasSize(3);
    }

    @DisplayName("메뉴 생성")
    @Nested
    class 메뉴_생성 {
        @Test
        void 메뉴_생성_성공() {
            // given
            MenuRequest menuRequest = new MenuRequest("떡볶이 세트", BigDecimal.ZERO, 분식_그룹.getId());

            given(menuGroupService.findById(eq(분식_그룹.getId()))).willReturn(분식_그룹);
            given(menuRepository.save(any(Menu.class))).willReturn(new Menu(1L, "떡볶이 세트", BigDecimal.ZERO, 분식_그룹));

            // when
            MenuResponse menuResponse = menuService.create(menuRequest);

            // then
            assertAll(() -> assertThat(menuResponse).isNotNull(), () -> assertThat(menuResponse.getId()).isNotNull());
        }

        @DisplayName("메뉴 생성 실패")
        @Nested
        class 메뉴_생성_실패 {
            @DisplayName("메뉴의 가격이 없으면 생성할 수 없다.")
            @Test
            void 가격이_없는_메뉴_생성() {
                // given
                MenuRequest menu = new MenuRequest("떡볶이 세트", 분식_그룹.getId());

                // when / then
                assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("메뉴의 가격이 0보다 작으면 생성할 수 없다.")
            @Test
            void 가격이_0보다_작은_메뉴_생성() {
                // given
                MenuRequest menu = new MenuRequest("떡볶이 세트", new BigDecimal(-1), 분식_그룹.getId());

                // when / then
                assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("존재하지 않는 메뉴 그룹의 메뉴를 생성할 수 없다.")
            @Test
            void 존재하지_않는_메뉴_그룹의_메뉴_생성() {
                // given
                Long 존재하지_않는_메뉴_그룹의_아이디 = 99999L;
                MenuRequest menu = new MenuRequest("떡볶이 세트", new BigDecimal(1000), 존재하지_않는_메뉴_그룹의_아이디);

                // when /then
                assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("존재하지 않는 상품을 포함한 메뉴는 생성할 수 없다.")
            @Test
            void 존재하지_않는_상품을_포함한_메뉴_상품을_가진_메뉴_생성() {
                // given
                Long 존재하지_않는_상품_아이디 = 99999L;
                MenuProductRequest 존재하지_않는_상품을_포함한_메뉴_상품 = MenuProductRequest.of(존재하지_않는_상품_아이디, 1L);

                MenuRequest menu = new MenuRequest("떡볶이 세트", new BigDecimal(1000), 분식_그룹.getId(),
                        Collections.singletonList(존재하지_않는_상품을_포함한_메뉴_상품));
                given(menuGroupService.findById(eq(분식_그룹.getId()))).willReturn(분식_그룹);
                given(productRepository.findById(eq(존재하지_않는_상품_아이디))).willReturn(Optional.empty());

                // when /then
                assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("메뉴 상품 전체의 금액보다 큰 가격을 가진 메뉴를 생성할 수 없다.")
            @Test
            void 메뉴_상품_전체의_금액_보다_큰_가격의_메뉴_생성() {
                Product 떡볶이 = new Product(1L, "떡볶이", Price.from(1000));
                Product 순대 = new Product(2L, "순대", Price.from(1500));

                MenuProductRequest 떡볶이_메뉴_상품 = MenuProductRequest.of(떡볶이.getId(), 2L);
                MenuProductRequest 순대_메뉴_상품 = MenuProductRequest.of(순대.getId(), 1L);

                MenuRequest menu = new MenuRequest("떡볶이 세트", new BigDecimal(3600), 분식_그룹.getId(),
                        Arrays.asList(떡볶이_메뉴_상품, 순대_메뉴_상품));

                given(menuGroupService.findById(eq(분식_그룹.getId()))).willReturn(분식_그룹);
                given(productRepository.findById(eq(떡볶이.getId()))).willReturn(Optional.ofNullable(떡볶이));
                given(productRepository.findById(eq(순대.getId()))).willReturn(Optional.ofNullable(순대));

                // when /then
                assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}

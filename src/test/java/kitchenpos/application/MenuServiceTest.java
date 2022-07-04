package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 서비스")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @InjectMocks
    protected MenuService menuService;
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;

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
        Menu 떡볶이_세트1 = new Menu("떡볶이 세트1", BigDecimal.ZERO, 분식_그룹.getId());
        Menu 떡볶이_세트2 = new Menu("떡볶이 세트2", BigDecimal.ZERO, 분식_그룹.getId());
        Menu 떡볶이_세트3 = new Menu("떡볶이 세트3", BigDecimal.ZERO, 분식_그룹.getId());

        given(menuDao.findAll()).willReturn(Arrays.asList(떡볶이_세트1, 떡볶이_세트2, 떡볶이_세트3));

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).hasSize(3);
    }

    @DisplayName("메뉴 생성")
    @Nested
    class 메뉴_생성 {
        @Test
        void 메뉴_생성_성공() {
            // given
            Menu menu = new Menu("떡볶이 세트", BigDecimal.ZERO, 분식_그룹.getId());
            given(menuGroupDao.existsById(eq(menu.getMenuGroupId()))).willReturn(Boolean.TRUE);
            given(menuDao.save(any(Menu.class))).willReturn(new Menu(1L, "떡볶이 세트", BigDecimal.ZERO, 분식_그룹.getId()));

            // when
            Menu savedMenu = menuService.create(menu);

            // then
            assertAll(() -> assertThat(savedMenu).isNotNull(), () -> assertThat(savedMenu.getId()).isNotNull());
        }

        @DisplayName("메뉴 생성 실패")
        @Nested
        class 메뉴_생성_실패 {
            @DisplayName("메뉴의 가격이 없으면 생성할 수 없다.")
            @Test
            void 가격이_없는_메뉴_생성() {
                // given
                Menu menu = new Menu("떡볶이 세트", 분식_그룹.getId());

                // when / then
                assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("메뉴의 가격이 0보다 작으면 생성할 수 없다.")
            @Test
            void 가격이_0보다_작은_메뉴_생성() {
                // given
                Menu menu = new Menu("떡볶이 세트", new BigDecimal(-1), 분식_그룹.getId());

                // when / then
                assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("존재하지 않는 메뉴 그룹의 메뉴를 생성할 수 없다.")
            @Test
            void 존재하지_않는_메뉴_그룹의_메뉴_생성() {
                // given
                Long 존재하지_않는_메뉴_그룹의_아이디 = 99999L;
                Menu menu = new Menu("떡볶이 세트", new BigDecimal(1000), 존재하지_않는_메뉴_그룹의_아이디);
                given(menuGroupDao.existsById(eq(존재하지_않는_메뉴_그룹의_아이디))).willReturn(false);

                // when /then
                assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("존재하지 않는 상품을 포함한 메뉴는 생성할 수 없다.")
            @Test
            void 존재하지_않는_상품을_포함한_메뉴_상품을_가진_메뉴_생성() {
                // given
                Long 존재하지_않는_상품_아이디 = 99999L;
                MenuProduct 존재하지_않는_상품을_포함한_메뉴_상품 = new MenuProduct(존재하지_않는_상품_아이디, 1);

                Menu menu = new Menu("떡볶이 세트", new BigDecimal(1000), 분식_그룹.getId(),
                        Collections.singletonList(존재하지_않는_상품을_포함한_메뉴_상품));
                given(menuGroupDao.existsById(anyLong())).willReturn(Boolean.TRUE);
                given(productDao.findById(eq(존재하지_않는_상품_아이디))).willReturn(Optional.empty());

                // when /then
                assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("메뉴 상품 전체의 금액보다 큰 가격을 가진 메뉴를 생성할 수 없다.")
            @Test
            void 메뉴_상품_전체의_금액_보다_큰_가격의_메뉴_생성() {
                Product 떡볶이 = new Product(1L, "떡볶이", new BigDecimal(1000));
                Product 순대 = new Product(2L, "순대", new BigDecimal(1500));

                MenuProduct 떡볶이_메뉴_상품 = new MenuProduct(떡볶이.getId(), 2);
                MenuProduct 순대_메뉴_상품 = new MenuProduct(순대.getId(), 1);

                Menu menu = new Menu("떡볶이 세트", new BigDecimal(3600), 분식_그룹.getId(), Arrays.asList(떡볶이_메뉴_상품, 순대_메뉴_상품));

                given(menuGroupDao.existsById(anyLong())).willReturn(Boolean.TRUE);
                given(productDao.findById(eq(떡볶이.getId()))).willReturn(Optional.ofNullable(떡볶이));
                given(productDao.findById(eq(순대.getId()))).willReturn(Optional.ofNullable(순대));

                // when /then
                assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}

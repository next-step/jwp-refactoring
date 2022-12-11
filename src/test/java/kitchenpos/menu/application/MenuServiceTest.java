package kitchenpos.menu.application;

import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.*;
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

import static kitchenpos.fixture.ProductFixture.후라이드치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductRepository productRepository;


    private Product 상품_후라이드치킨;
    private Menu 메뉴_기본;
    private MenuGroup 메뉴_그룹_기본;
    private MenuProduct 메뉴_상품_후라이드_치킨;

    @BeforeEach
    void set_up() {
        상품_후라이드치킨 = ProductFixture.create("후라이드치킨", BigDecimal.valueOf(15_000));
        메뉴_상품_후라이드_치킨 = MenuProductFixture.create(1L, ProductFixture.후라이드치킨.getId(), 1L);
        메뉴_그룹_기본 = MenuGroupFixture.create(1L, "메뉴 그룹 기본");
        메뉴_기본 = MenuFixture.create(
                1L, BigDecimal.valueOf(15_000), 메뉴_그룹_기본.getId(), Arrays.asList(메뉴_상품_후라이드_치킨)
        );
    }

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void create() {
        // given
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productRepository.findById(any())).thenReturn(Optional.of(후라이드치킨));
        when(menuDao.save(any())).thenReturn(메뉴_기본);

        // when
        Menu 메뉴_기본_등록 = menuService.create(메뉴_기본);

        // then
        assertThat(메뉴_기본_등록).isEqualTo(메뉴_기본);
    }

    @DisplayName("메뉴를 구성하는 메뉴 상품들에 상품이 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_error_not_found_product() {
        // given
        메뉴_상품_후라이드_치킨.setProductId(null);
        메뉴_기본.setMenuProducts(Arrays.asList(메뉴_상품_후라이드_치킨));

        when(menuGroupDao.existsById(any())).thenReturn(true);

        // when && then
        assertThatThrownBy(() -> menuService.create(메뉴_기본))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 0 원 이상이어야 한다.")
    @Test
    void create_error_menu_price_minus() {
        // given
        Menu 메뉴_가격_이상 = new Menu();
        메뉴_가격_이상.setPrice(BigDecimal.valueOf(-100_000));

        // given && when && then
        assertThatThrownBy(() -> menuService.create(메뉴_가격_이상))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메인그룹이 없다면 메뉴를 등록할 수 없다.")
    @Test
    void create_error_no_exist_menu_group() {
        // given
        Menu 메뉴_오류_그룹_없음 = new Menu();
        메뉴_오류_그룹_없음.setPrice(BigDecimal.valueOf(100_000));

        // when && then
        assertThatThrownBy(() -> menuService.create(메뉴_오류_그룹_없음))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴 상품 가격의 합보다 크다면 등록할 수 없다")
    @Test
    void create_error_menu_for_too_much_money() {
        // given
        Menu 메뉴_요금_많이 = new Menu();
        메뉴_요금_많이.setPrice(BigDecimal.valueOf(1_000_000));
        메뉴_요금_많이.setMenuProducts(Arrays.asList(메뉴_상품_후라이드_치킨));
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productRepository.findById(any())).thenReturn(Optional.of(후라이드치킨));

        // when && then
        assertThatThrownBy(() -> menuService.create(메뉴_요금_많이))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록 조회 시 메뉴 상품도 함께 조회한다.")
    @Test
    void list() {
        // given
        when(menuDao.findAll()).thenReturn(Arrays.asList(메뉴_기본));
        when(menuProductDao.findAllByMenuId(메뉴_기본.getId())).thenReturn(Arrays.asList(메뉴_상품_후라이드_치킨));

        // when
        List<Menu> 메뉴_목록_조회 = menuService.list();

        // then
        assertAll(
                () -> assertThat(메뉴_목록_조회).hasSize(1),
                () -> assertThat(메뉴_목록_조회).containsExactly(메뉴_기본)
        );
    }

}

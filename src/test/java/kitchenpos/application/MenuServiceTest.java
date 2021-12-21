package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.fixture.MenuFixture;
import kitchenpos.application.fixture.MenuGroupFixture;
import kitchenpos.application.fixture.MenuProductFixture;
import kitchenpos.application.fixture.ProductFixture;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    Product 상품_후라이드;

    MenuGroup 한마리_메뉴그룹;
    MenuProduct 메뉴상품1;
    MenuProduct 메뉴상품2;

    Menu 후라이드치킨;
    Menu 양념치킨;

    @BeforeEach
    void setUp() {
        상품_후라이드 = ProductFixture.create(1L, "후라이드", BigDecimal.valueOf(16_000L));

        한마리_메뉴그룹 = MenuGroupFixture.create(1L, "한마리 메뉴");
        메뉴상품1 = MenuProductFixture.createMenuProduct(1L, 1L, 1L);
        메뉴상품2 = MenuProductFixture.createMenuProduct(2L, 2L, 1L);

        후라이드치킨 = MenuFixture.createMenu(1L, "후라이드치킨", 16_000L, 한마리_메뉴그룹, Arrays.asList(메뉴상품1));
        양념치킨 = MenuFixture.createMenu(2L, "양념치킨", 16_000L, 한마리_메뉴그룹, Arrays.asList(메뉴상품1));
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        given(menuGroupRepository.existsById(후라이드치킨.getMenuGroupId())).willReturn(true);
        given(productRepository.findById(메뉴상품1.getProductId())).willReturn(Optional.of(상품_후라이드));
        given(menuDao.save(후라이드치킨)).willReturn(후라이드치킨);

        Menu savedMenu = menuService.create(후라이드치킨);

        assertThat(savedMenu).isEqualTo(후라이드치킨);
    }

    @DisplayName("메뉴를 등록할 때, 가격이 null이면 예외가 발생한다.")
    @Test
    void createImpossible1() {
        Menu 가격이_null인_메뉴 = 후라이드치킨;
        가격이_null인_메뉴.setPrice(null);

        assertThatThrownBy(() -> menuService.create(가격이_null인_메뉴))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록할 때, 가격이 0보다 작으면 예외가 발생한다.")
    @Test
    void createImpossible2() {
        Menu 가격이_0보다_작은메뉴 = MenuFixture.createMenu(1L, "후라이드치킨", -1_000L, 한마리_메뉴그룹, Arrays.asList(메뉴상품1));

        assertThatThrownBy(() -> menuService.create(가격이_0보다_작은메뉴))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록할 때, 메뉴 그룹에 포함되어 있지 않으면 예외가 발생한다.")
    @Test
    void createImpossible3() {
        given(menuGroupRepository.existsById(후라이드치킨.getMenuGroupId())).willReturn(false);

        assertThatThrownBy(() -> menuService.create(후라이드치킨))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록할 때, 메뉴에 포함된 상품이 등록되어 있지 않으면 예외가 발생한다.")
    @Test
    void createImpossible4() {
        given(menuGroupRepository.existsById(후라이드치킨.getMenuGroupId())).willReturn(true);
        given(productRepository.findById(메뉴상품1.getProductId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.create(후라이드치킨))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록할 때, 메뉴의 가격이 포함된 상품들 가격의 총합보다 크면 예외가 발생한다. ")
    @Test
    void createImpossible5() {
        Menu 가격이_총합보다_큰_메뉴 = MenuFixture.createMenu(1L, "후라이드치킨", 100_000L, 한마리_메뉴그룹, Arrays.asList(메뉴상품1));
        given(menuGroupRepository.existsById(가격이_총합보다_큰_메뉴.getMenuGroupId())).willReturn(true);
        given(productRepository.findById(메뉴상품1.getProductId())).willReturn(Optional.of(상품_후라이드));

        assertThatThrownBy(() -> menuService.create(가격이_총합보다_큰_메뉴))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        given(menuDao.findAll()).willReturn(Arrays.asList(후라이드치킨, 양념치킨));
        given(menuProductRepository.findAllByMenuId(후라이드치킨.getId())).willReturn(Arrays.asList(메뉴상품1));
        given(menuProductRepository.findAllByMenuId(양념치킨.getId())).willReturn(Arrays.asList(메뉴상품2));

        List<Menu> menus = menuService.list();

        assertThat(menus).containsExactly(후라이드치킨, 양념치킨);
    }
}
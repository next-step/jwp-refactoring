package kitchenpos.menu.application;

import static kitchenpos.menu.application.MenuGroupServiceTest.메뉴_그룹_생성;
import static kitchenpos.product.application.ProductServiceTest.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public
class MenuServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuDao menuDao;

    @InjectMocks
    private MenuService menuService;

    private Product 반반;
    private Product 감자튀김;

    private MenuGroup 한마리메뉴;

    private Menu 반반치킨세트;

    private MenuProduct 메뉴반반치킨;
    private MenuProduct 메뉴감자튀김;

    @BeforeEach
    void init() {
        반반 = 상품_생성(1L, "반반", 16_000L);
        감자튀김 = 상품_생성(2L, "감자튀김", 3_000L);
        한마리메뉴 = 메뉴_그룹_생성(1L, "한마리메뉴");

        메뉴반반치킨 = 메뉴_상품_생성(반반.getId(), 1L);
        메뉴감자튀김 = 메뉴_상품_생성(감자튀김.getId(), 1L);

        반반치킨세트 = 메뉴_생성(1L, "반반치킨", 16_000L, 한마리메뉴, Arrays.asList(메뉴반반치킨, 메뉴감자튀김));
    }

    @Test
    @DisplayName("메뉴를 생성한다.")
    void createMenu() {
        // given
        given(menuGroupDao.findById(한마리메뉴.getId())).willReturn(Optional.of(한마리메뉴));
        given(productRepository.findById(반반.getId())).willReturn(Optional.of(반반));
        given(productRepository.findById(감자튀김.getId())).willReturn(Optional.of(감자튀김));
        given(menuDao.save(any(Menu.class))).willReturn(반반치킨세트);

        //when
        MenuResponse savedMenu = menuService.create(new MenuRequest(반반치킨세트));

        // then
        assertAll(
            () -> assertThat(savedMenu).isNotNull(),
            () -> assertThat(savedMenu.getName()).isEqualTo(반반치킨세트.getName()),
            () -> assertThat(savedMenu.getPrice()).isEqualTo(반반치킨세트.getPrice())
        );
    }

    @Test
    @DisplayName("유효하지 않은 메뉴그룹을 가진 메뉴를 생성할 경우 - 오류")
    void notExistMenuGroup() {
        // given
        given(menuGroupDao.findById(한마리메뉴.getId())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> menuService.create(new MenuRequest(반반치킨세트)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("유효하지 않은 상품을 가진 메뉴를 생성할 경우 - 오류")
    void notExistProduct() {
        // given
        given(menuGroupDao.findById(한마리메뉴.getId())).willReturn(Optional.of(한마리메뉴));
        given(productRepository.findById(반반.getId())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> menuService.create(new MenuRequest(반반치킨세트)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void findAll() {
        // given
        given(menuDao.findAll()).willReturn(Arrays.asList(반반치킨세트));

        // when
        List<MenuResponse> menus = menuService.list();

        // then
        assertThat(menus.size()).isEqualTo(1);
    }

    public static  Menu 메뉴_생성(Long menuId, String name, Long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(menuId, name, new BigDecimal(price), menuGroup, menuProducts);
    }

    public static MenuProduct 메뉴_상품_생성(Long productId, long quantity) {
        return new MenuProduct(productId, quantity);
    }
}

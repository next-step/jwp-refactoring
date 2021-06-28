package kitchenpos.application;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.ui.MenuRestControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest {

    private static final long NOT_SAVED_ID = Long.MAX_VALUE;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    private Product 후라이드;
    private Product 양념치킨;

    private MenuProduct mp후라이드;
    private MenuProduct mp양념치킨;

    @BeforeEach
    void setUp() {
        후라이드 = new Product("후라이드", 16000L);
        양념치킨 = new Product("양념치킨", 16000L);

        mp후라이드 = new MenuProduct();
        mp후라이드.setSeq(1L);
        mp후라이드.setMenuId(1L);
        mp후라이드.setProductId(1L);
        mp후라이드.setQuantity(1L);

        mp양념치킨 = new MenuProduct();
        mp양념치킨.setSeq(2L);
        mp양념치킨.setMenuId(1L);
        mp양념치킨.setProductId(2L);
        mp양념치킨.setQuantity(1L);
    }

    @DisplayName("메뉴 생성 성공")
    @Test
    void createMenuSuccess() {

        // given
        Menu menu = createMenu(30000);

        given(menuGroupRepository.existsById(menu.getMenuGroupId())).willReturn(true);
        givenProducts();
        given(menuDao.save(menu)).willReturn(menu);
        givenMenuProducts();

        // when
        Menu actual = menuService.create(menu);

        // then
        assertThat(actual).isEqualTo(menu);
    }

    @DisplayName("메뉴 생성 실패 - 메뉴 가격이 음수이거나 설정되지 않음")
    @NullSource
    @ValueSource(ints = {-1000, -10000 })
    @ParameterizedTest
    void createMenuFail01(Integer price) {
        // given
        Menu menu = createMenu(price);

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 생성 요청 실패 - 등록되지 않은 메뉴 그룹의 ID를 사용")
    @Test
    void createMenuFail02() {
        // given
        Menu menu = createMenu(30000);
        menu.setMenuGroupId(NOT_SAVED_ID);
        given(menuGroupRepository.existsById(menu.getMenuGroupId())).willReturn(false);

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 생성 요청 실패 - 메뉴 가격은 메뉴 상품 가격의 합보다 작아야 함")
    @ValueSource(ints = { 33000, 35000 })
    @ParameterizedTest
    void createMenuFail03(int price) {
        // given
        Menu menu = createMenu(price);
        menu.setMenuGroupId(NOT_SAVED_ID);

        given(menuGroupRepository.existsById(menu.getMenuGroupId())).willReturn(true);
        givenProducts();

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    private Menu createMenu(Integer price) {
        Menu menu = MenuRestControllerTest.createMenu(price);
        menu.setId(1L);
        menu.setMenuProducts(Arrays.asList(mp후라이드, mp양념치킨));
        return menu;
   }

    private void givenProducts() {
        given(productRepository.findById(1L)).willReturn(Optional.of(후라이드));
        given(productRepository.findById(2L)).willReturn(Optional.of(양념치킨));
    }

    private void givenMenuProducts() {
        given(menuProductDao.save(mp후라이드)).willReturn(mp후라이드);
        given(menuProductDao.save(mp양념치킨)).willReturn(mp양념치킨);
    }
}

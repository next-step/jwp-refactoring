package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 Business Object 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    private Product 떡볶이;
    private Product 튀김;
    private Product 순대;

    private MenuProduct 떡튀순_상품_떡볶이;
    private MenuProduct 떡튀순_상품_튀김;
    private MenuProduct 떡튀순_상품_순대;
    private MenuProduct 떡튀순_곱배기_상품_떡볶이;

    private List<MenuProduct> 떡튀순_상품_목록;
    private List<MenuProduct> 떡튀순_곱배기_상품_목록;
    private Menu 떡튀순;
    private Menu 떡튀순_곱배기;

    @BeforeEach
    void setUp() {
        떡볶이 = new Product(1L, "떡볶이", BigDecimal.valueOf(4500));
        튀김 = new Product(2L, "튀김", BigDecimal.valueOf(2500));
        순대 = new Product(3L, "순대", BigDecimal.valueOf(4000));
        떡튀순_상품_떡볶이 = new MenuProduct(1L, 1L, 1L, 1);
        떡튀순_상품_튀김 = new MenuProduct(2L, 1L, 2L, 1);
        떡튀순_상품_순대 = new MenuProduct(3L, 1L, 3L, 1);
        떡튀순_곱배기_상품_떡볶이 = new MenuProduct(4L, 1L, 1L, 2);
        떡튀순_상품_목록 = Arrays.asList(떡튀순_상품_떡볶이, 떡튀순_상품_튀김, 떡튀순_상품_순대);
        떡튀순_곱배기_상품_목록 = Arrays.asList(떡튀순_곱배기_상품_떡볶이, 떡튀순_상품_튀김, 떡튀순_상품_순대);
        떡튀순 = new Menu(1L, "떡튀순", BigDecimal.valueOf(10000), 1L, 떡튀순_상품_목록);
        떡튀순_곱배기 = new Menu(2L, "떡튀순_곱배기", BigDecimal.valueOf(10000), 1L, 떡튀순_곱배기_상품_목록);
    }

    @DisplayName("메뉴 생성")
    @Test
    void 메뉴_생성() {
        when(menuGroupDao.existsById(1L)).thenReturn(true);
        when(productDao.findById(떡볶이.getId())).thenReturn(Optional.of(떡볶이));
        when(productDao.findById(튀김.getId())).thenReturn(Optional.of(튀김));
        when(productDao.findById(순대.getId())).thenReturn(Optional.of(순대));
        when(menuProductDao.save(떡튀순_상품_떡볶이)).thenReturn(떡튀순_상품_떡볶이);
        when(menuProductDao.save(떡튀순_상품_튀김)).thenReturn(떡튀순_상품_튀김);
        when(menuProductDao.save(떡튀순_상품_순대)).thenReturn(떡튀순_상품_순대);
        when(menuDao.save(떡튀순)).thenReturn(떡튀순);

        Menu 생성된_메뉴 = menuService.create(떡튀순);

        assertAll(
                () -> assertThat(생성된_메뉴.getId()).isEqualTo(1L),
                () -> assertThat(생성된_메뉴.getName()).isEqualTo("떡튀순"),
                () -> assertThat(생성된_메뉴.getPrice()).isEqualTo(BigDecimal.valueOf(10000)),
                () -> assertThat(생성된_메뉴.getMenuProducts()).containsAll(떡튀순_상품_목록)
        );
    }

    @DisplayName("존재하지 않는 메뉴 그룹에 속하는 메뉴 생성 요청 시 예외처리")
    @Test
    void 존재하지_않는_메뉴_그룹의_메뉴_생성() {
        when(menuGroupDao.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> menuService.create(떡튀순)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 상품으로 구성된 메뉴 생성 요청 시 예외처리")
    @Test
    void 존재하지_않는_상품_메뉴_생성() {
        when(menuGroupDao.existsById(1L)).thenReturn(true);
        when(productDao.findById(떡볶이.getId())).thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> menuService.create(떡튀순)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 0원 보다 작은 메뉴 생성 요청 시 예외처리")
    @Test
    void 올바르지_않은_가격_예외처리() {
        Menu 잘못된_가격_메뉴 = new Menu(3L, "떡튀순", BigDecimal.valueOf(-200), 1L, 떡튀순_상품_목록);

        assertThatThrownBy(() -> menuService.create(잘못된_가격_메뉴)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("각 상품 가격의 합보다 큰 가격의 메뉴 생성 요청 시 예외처리")
    @Test
    void 초과_가격_예외처리() {
        when(menuGroupDao.existsById(1L)).thenReturn(true);
        when(productDao.findById(떡볶이.getId())).thenReturn(Optional.of(떡볶이));
        when(productDao.findById(튀김.getId())).thenReturn(Optional.of(튀김));
        when(productDao.findById(순대.getId())).thenReturn(Optional.of(순대));
        Menu 초과_가격_메뉴 = new Menu(3L, "떡튀순", BigDecimal.valueOf(12000), 1L, 떡튀순_상품_목록);

        assertThatThrownBy(() -> menuService.create(초과_가격_메뉴)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있는 이름으로 메뉴 생성 요청 시 예외처리")
    @Test
    void 비어있는_이름_예외처리() {
        Menu 비어있는_이름_메뉴 = new Menu(3L, "", BigDecimal.valueOf(10000), 1L, 떡튀순_상품_목록);

        assertThatThrownBy(() -> menuService.create(비어있는_이름_메뉴)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 조회")
    @Test
    void 메뉴_조회() {
        List<Menu> 등록된_메뉴_목록 = Arrays.asList(떡튀순, 떡튀순_곱배기);
        when(menuDao.findAll()).thenReturn(등록된_메뉴_목록);
        when(menuProductDao.findAllByMenuId(떡튀순.getId())).thenReturn(떡튀순_상품_목록);
        when(menuProductDao.findAllByMenuId(떡튀순_곱배기.getId())).thenReturn(떡튀순_곱배기_상품_목록);

        List<Menu> 조회된_메뉴_목록 = menuService.list();

        assertAll(
                () -> assertThat(조회된_메뉴_목록.size()).isEqualTo(등록된_메뉴_목록.size()),
                () -> assertThat(조회된_메뉴_목록).containsAll(등록된_메뉴_목록)
        );
    }

}
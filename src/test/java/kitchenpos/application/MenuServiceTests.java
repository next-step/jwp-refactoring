package kitchenpos.application;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;

@DisplayName("메뉴 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTests {

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

    private MenuGroup 코스메뉴그룹;
    private List<Product> 상품종류;
    private List<MenuProduct> 메뉴상품목록;
    private Menu 코스A;

    @BeforeEach
    public void setUp() {
        코스메뉴그룹 = new MenuGroup(1L, "코스메뉴그룹");
        Product 시저_샐러드 = new Product.builder()
                .id(1L)
                .name("시저 샐러드")
                .price(new BigDecimal(10_000))
                .build();
        Product 크림_파스타 = new Product.builder()
                .id(2L)
                .name("크림 파스타")
                .price(new BigDecimal(16_000))
                .build();
        Product 안심_스테이크 = new Product.builder()
                .id(3L)
                .name("안심 스테이크")
                .price(new BigDecimal(30_000))
                .build();
        상품종류 = Arrays.asList(시저_샐러드, 크림_파스타, 안심_스테이크);
        MenuProduct 시저_샐러드_메뉴 = new MenuProduct.builder()
                .seq(1L)
                .productId(시저_샐러드.getId())
                .quantity(5L)
                .build();
        MenuProduct 크림_파스타_메뉴 = new MenuProduct.builder()
                .seq(2L)
                .productId(크림_파스타.getId())
                .quantity(5L)
                .build();
        MenuProduct 안심_스테이크_메뉴 = new MenuProduct.builder()
                .seq(1L)
                .productId(안심_스테이크.getId())
                .quantity(5L)
                .build();
        메뉴상품목록 = Arrays.asList(시저_샐러드_메뉴, 크림_파스타_메뉴, 안심_스테이크_메뉴);
    }

    @Test
    public void 메뉴_생성() {
        코스A = new Menu.builder()
                .id(1L)
                .name("코스A")
                .price(new BigDecimal(56_000))
                .menuGroupId(코스메뉴그룹.getId())
                .menuProducts(메뉴상품목록)
                .build();

        lenient().when(menuGroupDao.existsById(코스메뉴그룹.getId()))
                .thenReturn(true);
        for (MenuProduct menuProduct : 메뉴상품목록) {
            lenient().when(productDao.findById(menuProduct.getProductId()))
                    .thenReturn(상품종류.stream()
                            .filter(product -> product.getId().equals(menuProduct.getProductId()))
                            .findAny());
        }
        lenient().when(menuDao.save(코스A))
                .thenReturn(코스A);
        for (MenuProduct menuProduct : 메뉴상품목록) {
            lenient().when(menuProductDao.save(menuProduct))
                    .thenReturn(menuProduct);
        }

        assertThat(menuService.create(코스A))
                .isNotNull()
                .isInstanceOf(Menu.class);
    }

    @Test
    public void 메뉴가격이_음수일_경우_생성실패() {
        Menu 코스B = new Menu.builder()
                .id(1L)
                .name("코스B")
                .price(new BigDecimal(-56_000))
                .build();
        assertThatThrownBy(() -> menuService.create(코스B))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("0보다 큰 가격을 입력해야 합니다.");
    }

    @Test
    public void 메뉴가격이_상품가격합계와_차이가_있으면_오류발생() {
        코스A = new Menu.builder()
                .id(1L)
                .name("코스A")
                .price(new BigDecimal(280_001))
                .menuGroupId(코스메뉴그룹.getId())
                .menuProducts(메뉴상품목록)
                .build();

        lenient().when(menuGroupDao.existsById(코스메뉴그룹.getId()))
                .thenReturn(true);
        for (MenuProduct menuProduct : 메뉴상품목록) {
            lenient().when(productDao.findById(menuProduct.getProductId()))
                    .thenReturn(상품종류.stream()
                            .filter(product -> product.getId().equals(menuProduct.getProductId()))
                            .findAny());
        }
        assertThatThrownBy(() -> menuService.create(코스A))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴가격과 상품가격합계가 일치하지 않습니다.");
    }

    @Test
    public void 등록되지_않은_메뉴그룹일_경우_생성실패() {
        Menu 코스C = new Menu.builder()
                .id(1L)
                .name("코스C")
                .price(new BigDecimal(56_000))
                .menuGroupId(-1L)
                .build();
        lenient().when(menuGroupDao.existsById(코스C.getMenuGroupId()))
                .thenReturn(false);
        assertThatThrownBy(() -> menuService.create(코스C))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되지 않은 메뉴그룹입니다.");
    }

    @Test
    public void 상품목록_조회() {
        메뉴_생성();
        lenient().when(menuDao.findAll())
                .thenReturn(Arrays.asList(코스A));
        assertThat(menuService.list())
                .isNotNull()
                .isInstanceOf(List.class)
                .hasSize(1)
                .isEqualTo(Arrays.asList(코스A));
    }

}

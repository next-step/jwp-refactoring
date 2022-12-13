package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
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
import kitchenpos.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private ProductDao productDao;
    @Mock
    private MenuProductDao menuProductDao;
    @InjectMocks
    private MenuService menuService;
    private Product 후라이드;
    private MenuProduct 메뉴에_등록된_상품;
    private Menu 메뉴;
    private MenuGroup 메뉴_그룹;

    @BeforeEach
    void setUp() {
        후라이드 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
        메뉴에_등록된_상품 = new MenuProduct(후라이드.getId(), 1);
        메뉴_그룹 = new MenuGroup(1L, "한마리메뉴");
        메뉴 = new Menu("후라이드치킨", BigDecimal.valueOf(16000), 메뉴_그룹.getId(), Arrays.asList(메뉴에_등록된_상품));
    }

    @Test
    void 생성() {
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(메뉴에_등록된_상품.getProductId())).willReturn(Optional.of(후라이드));
        given(menuDao.save(any())).willReturn(메뉴);
        given(menuProductDao.save(any())).willReturn(메뉴에_등록된_상품);

        MenuResponse response = menuService.create(메뉴);

        assertAll(
                () -> assertThat(response.getName()).isEqualTo("후라이드치킨"),
                () -> assertThat(response.getPrice()).isEqualTo(BigDecimal.valueOf(16000)),
                () -> assertThat(response.getMenuGroupId()).isEqualTo(1L),
                () -> assertThat(response.getMenuProducts()).containsExactlyElementsOf(Arrays.asList(메뉴에_등록된_상품))
        );
    }

    @Test
    void 메뉴_가격이_0미만인_경우() {
        메뉴 = new Menu("양념치킨", BigDecimal.valueOf(-5000), 메뉴_그룹.getId(), Arrays.asList(메뉴에_등록된_상품));

        assertThatThrownBy(
                () -> menuService.create(메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void 메뉴_가격이_null인_경우(BigDecimal price) {
        메뉴 = new Menu("양념치킨", price, 메뉴_그룹.getId(), Arrays.asList(메뉴에_등록된_상품));

        assertThatThrownBy(
                () -> menuService.create(메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹이_존재하지_않는_경우() {
        given(menuGroupDao.existsById(anyLong())).willReturn(false);

        assertThatThrownBy(
                () -> menuService.create(메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록된_상품이_아닌_경우() {
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(메뉴에_등록된_상품.getProductId())).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> menuService.create(메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_등록된_상품의_가격보다_큰_경우() {
        후라이드 = new Product(1L, "후라이드", BigDecimal.valueOf(6000));
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(메뉴에_등록된_상품.getProductId())).willReturn(Optional.of(후라이드));

        assertThatThrownBy(
                () -> menuService.create(메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_메뉴에_등록된_상품_가격의_합계보다_큰_경우() {
        메뉴에_등록된_상품 = new MenuProduct(후라이드.getId(), 0);
        메뉴 = new Menu("후라이드치킨", BigDecimal.valueOf(16000), 메뉴_그룹.getId(), Arrays.asList(메뉴에_등록된_상품));

        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(메뉴에_등록된_상품.getProductId())).willReturn(Optional.of(후라이드));

        assertThatThrownBy(
                () -> menuService.create(메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 조회() {
        given(menuDao.findAll()).willReturn(Arrays.asList(메뉴));
        given(menuProductDao.findAllByMenuId(메뉴.getId())).willReturn(Arrays.asList(메뉴에_등록된_상품));

        List<MenuResponse> response = menuService.list();

        assertAll(
                () -> assertThat(response.size()).isEqualTo(1),
                () -> assertThat(response.get(0).getMenuProducts()).containsExactly(메뉴에_등록된_상품)
        );
    }
}

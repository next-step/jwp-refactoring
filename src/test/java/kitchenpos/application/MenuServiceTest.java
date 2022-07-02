package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

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
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService service;

    private MenuProduct 치킨메뉴상품;
    private MenuProduct 돼지메뉴상품;
    private Product 치킨까스;
    private Product 돈까스;

    @BeforeEach
    void setUp() {
        치킨메뉴상품 = new MenuProduct(1L, null, 1L, 1L);
        돼지메뉴상품 = new MenuProduct(1L, null, 2L, 2L);

        치킨까스 = new Product(1L, "치킨까스", BigDecimal.valueOf(5000));
        돈까스 = new Product(2L, "돈까스", BigDecimal.valueOf(5000));
    }

    @DisplayName("메뉴 등록 성공")
    @Test
    void create() {
        //given
        List<MenuProduct> menuProducts = Arrays.asList(치킨메뉴상품, 돼지메뉴상품);
        Menu 모듬돈까스 = new Menu(1L, "모듬돈까스", BigDecimal.valueOf(13000), 3L, menuProducts);
        when(menuGroupDao.existsById(3L)).thenReturn(true);
        when(productDao.findById(치킨까스.getId())).thenReturn(Optional.of(치킨까스));
        when(productDao.findById(돈까스.getId())).thenReturn(Optional.of(돈까스));
        when(menuDao.save(모듬돈까스)).thenReturn(모듬돈까스);
        when(menuProductDao.save(치킨메뉴상품)).thenReturn(치킨메뉴상품);
        when(menuProductDao.save(돼지메뉴상품)).thenReturn(돼지메뉴상품);

        //when
        Menu response = service.create(모듬돈까스);

        //then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(모듬돈까스.getName()),
                () -> assertThat(response.getMenuGroupId()).isEqualTo(모듬돈까스.getMenuGroupId()),
                () -> assertThat(response.getMenuProducts()).hasSize(2)
        );
    }

    @DisplayName("존재하지 않는 메뉴 그룹의 메뉴 생성")
    @Test
    void createWithNotExistsMenuGroup() {
        //given
        List<MenuProduct> menuProducts = Arrays.asList(치킨메뉴상품, 돼지메뉴상품);
        Menu 모듬돈까스 = new Menu(1L, "모듬돈까스", BigDecimal.valueOf(13000), 3L, menuProducts);
        when(menuGroupDao.existsById(3L)).thenReturn(false);

        //when & then
        assertThatThrownBy(() -> service.create(모듬돈까스)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 0인 메뉴 생성")
    @Test
    void createWithZeroPrice() {
        //given
        List<MenuProduct> menuProducts = Arrays.asList(치킨메뉴상품, 돼지메뉴상품);
        Menu 모듬돈까스 = new Menu(1L, "모듬돈까스", BigDecimal.valueOf(13000), 3L, menuProducts);

        //when & then
        assertThatThrownBy(() -> service.create(모듬돈까스)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 상품들의 메뉴 생성")
    @Test
    void createWithNotExistsProduct() {
        //given
        List<MenuProduct> menuProducts = Arrays.asList(치킨메뉴상품, 돼지메뉴상품);
        Menu 모듬돈까스 = new Menu(1L, "모듬돈까스", BigDecimal.valueOf(13000), 3L, menuProducts);
        when(menuGroupDao.existsById(3L)).thenReturn(true);
        when(productDao.findById(치킨까스.getId())).thenReturn(Optional.of(치킨까스));
        when(productDao.findById(돈까스.getId())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> service.create(모듬돈까스)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴 상품들의 가격의 합보다 큰 메뉴 생성")
    @Test
    void createWithOverPrice() {
        //given
        List<MenuProduct> menuProducts = Arrays.asList(치킨메뉴상품, 돼지메뉴상품);
        Menu 모듬돈까스 = new Menu(1L, "모듬돈까스", BigDecimal.valueOf(17000), 3L, menuProducts);
        when(menuGroupDao.existsById(3L)).thenReturn(true);
        when(productDao.findById(치킨까스.getId())).thenReturn(Optional.of(치킨까스));
        when(productDao.findById(돈까스.getId())).thenReturn(Optional.of(돈까스));

        //when & then
        assertThatThrownBy(() -> service.create(모듬돈까스)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 메뉴 조회")
    @Test
    void list() {
        //given
        Menu 치킨까스메뉴 = new Menu(1L, "치킨까스", BigDecimal.valueOf(5000), 3L, Collections.singletonList(치킨메뉴상품));
        Menu 돈까스메뉴 = new Menu(2L, "돈까스", BigDecimal.valueOf(5000), 3L, Collections.singletonList(돼지메뉴상품));
        when(menuDao.findAll()).thenReturn(Arrays.asList(치킨까스메뉴, 돈까스메뉴));

        //when
        List<Menu> menus = service.list();

        //then
        assertThat(menus).contains(치킨까스메뉴, 돈까스메뉴);
    }
}

package kitchenpos.application;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SpringBootTest
class MenuServiceTest {

    @MockBean
    private MenuDao menuDao;
    @MockBean
    private MenuGroupDao menuGroupDao;
    @MockBean
    private MenuProductDao menuProductDao;
    @MockBean
    private ProductDao productDao;

    private MenuService menuService;
    private MenuGroup 베스트, 남녀노소;
    private Menu 면종류, 밥종류;
    private MenuProduct 면종류_짜장면, 면종류_짬뽕, 밥종류_볶음밥;
    private Product 짜장면, 짬뽕, 볶음밥;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

        베스트 = new MenuGroup();
        베스트.setId(1L);
        베스트.setName("베스트");

        남녀노소 = new MenuGroup();
        남녀노소.setId(2L);
        남녀노소.setName("남녀노소");

        면종류 = new Menu();
        면종류.setId(1L);
        면종류.setName("면종류");
        면종류.setMenuGroupId(베스트.getId());
        면종류.setPrice(new BigDecimal(100L));

        짜장면 = new Product();
        짜장면.setId(1L);
        짜장면.setName("짜장면");
        짜장면.setPrice(new BigDecimal(50L));

        면종류_짜장면 = new MenuProduct();
        면종류_짜장면.setSeq(1L);
        면종류_짜장면.setMenuId(면종류.getId());
        면종류_짜장면.setProductId(짜장면.getId());
        면종류_짜장면.setQuantity(10L);

        면종류.setMenuProducts(singletonList(면종류_짜장면));
    }

    @DisplayName("메뉴 생성")
    @Test
    void create() {
        when(menuGroupDao.save(베스트)).thenReturn(베스트);
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(짜장면));
        when(menuDao.save(any())).thenReturn(면종류);
        when(menuProductDao.save(any())).thenReturn(면종류_짜장면);

        Menu menu = menuService.create(면종류);
        assertThat(menu.getId()).isEqualTo(1L);
        assertThat(menu.getMenuProducts()).isEqualTo(singletonList(면종류_짜장면));
    }

    @DisplayName("메뉴 조회")
    @Test
    void list() {
        when(menuDao.findAll()).thenReturn(singletonList(면종류));
        when(menuProductDao.findAllByMenuId(any())).thenReturn(singletonList(면종류_짜장면));
        List<Menu> list = menuService.list();

        assertThat(list).contains(면종류);
        assertThat(list.get(0).getMenuProducts()).contains(면종류_짜장면);
    }
}
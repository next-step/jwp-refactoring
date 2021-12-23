package kitchenpos.menu;

import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.menu.product.domain.Product;
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

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private MenuProductDao menuProductDao;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {

        //given
        final boolean isExistedMenuGroup = true;
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setMenuGroupId(1L);
        menu.setName("후라이드세트");
        menu.setPrice(new BigDecimal("24000"));

        MenuProduct 후라이드메뉴 = new MenuProduct();
        후라이드메뉴.setProductId(1L);
        후라이드메뉴.setQuantity(1);

        Product 후라이드 = new Product();
        후라이드.setId(1L);
        후라이드.setName("후라이드");
        후라이드.setPrice(new BigDecimal("22000"));

        MenuProduct 콜라메뉴 = new MenuProduct();
        콜라메뉴.setProductId(2L);
        콜라메뉴.setQuantity(1);

        Product 콜라 = new Product();
        콜라.setId(2L);
        콜라.setName("콜라");
        콜라.setPrice(new BigDecimal("2000"));

        menu.setMenuProducts(Arrays.asList(후라이드메뉴, 콜라메뉴));
        when(menuGroupDao.existsById(anyLong())).thenReturn(isExistedMenuGroup);
        when(menuDao.save(any())).thenReturn(menu);
        when(productDao.findById(후라이드메뉴.getProductId())).thenReturn(Optional.ofNullable(후라이드));
        when(productDao.findById(콜라메뉴.getProductId())).thenReturn(Optional.ofNullable(콜라));
        when(menuProductDao.save(후라이드메뉴)).thenReturn(후라이드메뉴);
        when(menuProductDao.save(콜라메뉴)).thenReturn(콜라메뉴);

        //when
        Menu savedMenu = menuService.create(menu);

        //then
        assertThat(savedMenu).isNotNull();
        assertThat(savedMenu.getId()).isGreaterThan(0L);
        assertThat(savedMenu.getName()).isEqualTo(menu.getName());
    }

    @DisplayName("메뉴 리스트를 조회한다.")
    @Test
    void getMenus() {

        //given
        MenuProduct 후라이드메뉴 = new MenuProduct();
        MenuProduct 햄버거메뉴 = new MenuProduct();
        MenuProduct 콜라메뉴 = new MenuProduct();

        Menu menuA = new Menu();
        menuA.setId(1L);
        menuA.setName("후라이드세트");
        List<MenuProduct> menuAProducts = Arrays.asList(후라이드메뉴, 콜라메뉴);
        menuA.setMenuProducts(menuAProducts);

        Menu menuB = new Menu();
        menuB.setId(2L);
        menuB.setName("햄버거세트");
        List<MenuProduct> menuBProducts = Arrays.asList(햄버거메뉴, 콜라메뉴);
        menuB.setMenuProducts(menuBProducts);

        when(menuDao.findAll()).thenReturn(Arrays.asList(menuA, menuB));
        when(menuProductDao.findAllByMenuId(menuA.getId())).thenReturn(menuAProducts);
        when(menuProductDao.findAllByMenuId(menuB.getId())).thenReturn(menuBProducts);

        //when
        List<Menu> findMenus = menuService.list();

        //then
        assertThat(findMenus).isNotEmpty();
        assertThat(findMenus.stream()
                .flatMap(m -> m.getMenuProducts()
                .stream())
                .distinct()
                .collect(toList()))
                .contains(후라이드메뉴, 콜라메뉴, 햄버거메뉴);
    }
}


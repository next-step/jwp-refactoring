package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

  private Product product1;
  private Product product2;
  private MenuProduct menuProduct1;
  private MenuProduct menuProduct2;
  private MenuGroup menuGroup;

  @BeforeEach
  void setUp() {
    product1 = new Product(1L, "상품1", BigDecimal.valueOf(1_000));
    product2 = new Product(2L, "상품2", BigDecimal.valueOf(2_000));
    menuProduct1 = new MenuProduct(product1.getId(), 2);
    menuProduct2 = new MenuProduct(product2.getId(), 1);
    menuGroup = new MenuGroup(1L, "그룹1");
  }

  @DisplayName("메뉴 이름, 가격, 메뉴그룹, 메뉴상품 목록을 입력받아 저장할 수 있다.")
  @Test
  void createTest() {
    //given
    Menu menu = new Menu("메뉴이름", BigDecimal.valueOf(4_000), menuGroup.getId(), Arrays.asList(menuProduct1, menuProduct2));
    long savedMenuId = 1L;
    when(menuGroupDao.existsById(menuGroup.getId())).thenReturn(true);
    when(productDao.findById(product1.getId())).thenReturn(Optional.of(product1));
    when(productDao.findById(product2.getId())).thenReturn(Optional.of(product2));
    MenuProduct savedMenuProduct1 = new MenuProduct(savedMenuId, product1.getId(), 2);
    MenuProduct savedMenuProduct2 = new MenuProduct(savedMenuId, product2.getId(), 1);
    when(menuProductDao.save(menuProduct1)).thenReturn(savedMenuProduct1);
    when(menuProductDao.save(menuProduct2)).thenReturn(savedMenuProduct2);
    when(menuDao.save(menu)).thenReturn(new Menu(savedMenuId, "메뉴이름", BigDecimal.valueOf(1_000), menuGroup.getId(), Arrays.asList(savedMenuProduct1, savedMenuProduct2)));
    MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

    //when
    Menu savedMenu = menuService.create(menu);

    //then
    assertAll(
        () -> assertThat(savedMenu.getId()).isNotNull(),
        () -> assertThat(savedMenu.getName()).isEqualTo(menu.getName()),
        () -> assertThat(savedMenu.getPrice()).isEqualTo(menu.getPrice()),
        () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
        () -> assertThat(savedMenu.getMenuProducts()).containsExactly(savedMenuProduct1, savedMenuProduct2)
    );
    verify(menuGroupDao, VerificationModeFactory.times(1)).existsById(menuGroup.getId());
    verify(productDao, VerificationModeFactory.times(2)).findById(anyLong());
    verify(menuProductDao, VerificationModeFactory.times(2)).save(any());
    verify(menuDao, VerificationModeFactory.times(1)).save(any());
  }

  @DisplayName("메뉴 저장시 가격은 0 이상이다.")
  @Test
  void createFailCausePrice() {
    //given
    Menu nullPriceMenu = new Menu("메뉴이름", null, menuGroup.getId(), Arrays.asList(menuProduct1, menuProduct2));
    Menu negativePriceMenu = new Menu("메뉴이름", BigDecimal.valueOf(-1_000), menuGroup.getId(), Arrays.asList(menuProduct1, menuProduct2));
    MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

    //when & then
    assertAll(() -> assertThatThrownBy(() -> menuService.create(nullPriceMenu)).isInstanceOf(IllegalArgumentException.class),
        () -> assertThatThrownBy(() -> menuService.create(negativePriceMenu)).isInstanceOf(IllegalArgumentException.class));
  }

  @DisplayName("메뉴그룹은 존재하는 메뉴그룹만 지정할 수 있다.")
  @Test
  void createFailCauseNotExistMenuGroup() {
    //given
    long notExistMenuGroupId = 999L;
    Menu menuContainsNotExistMenuGroup = new Menu("메뉴이름", BigDecimal.valueOf(1_000), notExistMenuGroupId, Arrays.asList(menuProduct1, menuProduct2));
    when(menuGroupDao.existsById(notExistMenuGroupId)).thenReturn(false);
    MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

    //when * then
    assertThatThrownBy(() -> menuService.create(menuContainsNotExistMenuGroup)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("가격이 메뉴상품 목록 세부 가격의 합보다 작거나 같아야 한다.")
  @Test
  void createFailCauseNotMatchedPrice() {
    //given
    MenuProduct threeMenuProduct1 = new MenuProduct(product1.getId(), 3);
    MenuProduct twoMenuProduct2 = new MenuProduct(product2.getId(), 2);
    when(menuGroupDao.existsById(menuGroup.getId())).thenReturn(true);
    when(productDao.findById(product1.getId())).thenReturn(Optional.of(product1));
    when(productDao.findById(product2.getId())).thenReturn(Optional.of(product2));
    Menu menuNotMatchedPrice = new Menu("메뉴이름", BigDecimal.valueOf(10_000), menuGroup.getId(), Arrays.asList(threeMenuProduct1, twoMenuProduct2));
    MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

    //when
    assertThatThrownBy(() -> menuService.create(menuNotMatchedPrice)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("전체 메뉴의 목록을 조회할 수 있다.")
  @Test
  void findAllTest() {
    //given
    long savedMenu1Id = 1L;
    long savedMenu2Id = 2L;
    MenuProduct savedMenu1Product = new MenuProduct(savedMenu1Id, product1.getId(), 2);
    MenuProduct savedMenu2Product = new MenuProduct(savedMenu2Id, product2.getId(), 1);
    Menu menu1 = new Menu(1L, "메뉴이름1", BigDecimal.valueOf(1_000), menuGroup.getId(), Arrays.asList(savedMenu1Product));
    Menu menu2 = new Menu(2L, "메뉴이름2", BigDecimal.valueOf(2_000), menuGroup.getId(), Arrays.asList(savedMenu2Product));
    when(menuDao.findAll()).thenReturn(Arrays.asList(menu1, menu2));
    when(menuProductDao.findAllByMenuId(savedMenu1Id)).thenReturn(Arrays.asList(savedMenu1Product));
    when(menuProductDao.findAllByMenuId(savedMenu2Id)).thenReturn(Arrays.asList(savedMenu2Product));
    MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

    //when
    List<Menu> menuList = menuService.list();

    //then
    assertThat(menuList).containsExactly(menu1, menu2);
  }
}

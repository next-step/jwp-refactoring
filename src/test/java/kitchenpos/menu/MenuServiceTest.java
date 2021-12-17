package kitchenpos.menu;

import kitchenpos.application.MenuService;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
  @InjectMocks
  private MenuService menuService;

  @Mock
  private MenuDao menuDao;

  @Mock
  private MenuGroupDao menuGroupDao;

  @Mock
  private MenuProductDao menuProductDao;

  @Mock
  private ProductDao productDao;

  private MenuGroup 튀김종류;
  private Menu 치킨세트;
  private List<MenuProduct> 치킨세트_상품_리스트;
  private Product 치킨;
  private Product 맥주;
  MenuProduct 치킨_상품메뉴;
  MenuProduct 맥주_상품메뉴;


  @BeforeEach
  void setUp() {
    튀김종류 = MenuFactory.ofMenuGroup(1L, "튀김종류");

    치킨세트 = MenuFactory.ofMenu(1L, "치킨세트_모형", 튀김종류.getId(), 24000);

    치킨 = MenuFactory.ofProduct(1L, "치킨", 19000);
    맥주 = MenuFactory.ofProduct(2L, "맥주", 5000);

    치킨_상품메뉴 = MenuFactory.ofMenuProduct(1L, 치킨세트.getId(), 치킨.getId(), 1);
    맥주_상품메뉴 = MenuFactory.ofMenuProduct(2L, 치킨세트.getId(), 맥주.getId(), 1);

    치킨세트_상품_리스트 = MenuFactory.ofMenuProductList(Arrays.asList(치킨_상품메뉴, 맥주_상품메뉴));

    치킨세트.setMenuProducts(치킨세트_상품_리스트);

  }

  @DisplayName("메뉴를 생성한다.")
  @Test
  void 메뉴_생성() {
    // given
    메뉴_생성_정보_설정됨();

    // when
    Menu response = menuService.create(치킨세트);

    // then
    assertAll(
            () -> assertThat(response.getName()).isEqualTo(치킨세트.getName()),
            () -> assertThat(response.getMenuGroupId()).isEqualTo(튀김종류.getId()),
            () -> assertThat(response.getMenuProducts()).isEqualTo(치킨세트_상품_리스트)
    );
  }

  @DisplayName("메뉴가 메뉴 묶음에 존재하는 메뉴여야 한다.")
  @Test
  void 메뉴_생성_메뉴_그룹에_존재하지_않음() {
    // given
    given(menuGroupDao.existsById(치킨세트.getMenuGroupId())).willReturn(false);

    Throwable thrown = catchThrowable(() -> menuService.create(치킨세트));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("메뉴 생성 가격이 null 이거나 0원보다 낮으면 안된다.")
  @Test
  void 메뉴_생성_가격_0이하_예외() {
    // given
    치킨세트.setPrice(BigDecimal.valueOf(-1));

    Throwable thrown = catchThrowable(() -> menuService.create(치킨세트));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("메뉴 목록을 조회한다.")
  @Test
  void 메뉴_목록_조회() {
    // given
    메뉴_생성됨();

    // when
    List<Menu> response = menuService.list();

    // then
    assertThat(response.size()).isEqualTo(1);
  }

  private void 메뉴_생성됨() {
    given(menuDao.findAll()).willReturn(new ArrayList<>(Collections.singletonList(치킨세트)));
    given(menuProductDao.findAllByMenuId(치킨세트.getId())).willReturn(치킨세트_상품_리스트);
  }

  private void 메뉴_생성_정보_설정됨() {
    given(menuGroupDao.existsById(치킨세트.getMenuGroupId())).willReturn(true);

    given(productDao.findById(치킨_상품메뉴.getProductId())).willReturn(Optional.ofNullable(치킨));
    given(productDao.findById(맥주_상품메뉴.getProductId())).willReturn(Optional.ofNullable(맥주));


    given(menuDao.save(치킨세트)).willReturn(치킨세트);
    given(menuProductDao.save(치킨_상품메뉴)).willReturn(치킨_상품메뉴);
    given(menuProductDao.save(맥주_상품메뉴)).willReturn(맥주_상품메뉴);
  }
}

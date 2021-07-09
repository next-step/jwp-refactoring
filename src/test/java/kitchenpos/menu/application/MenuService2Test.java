package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuEntity;
import kitchenpos.menu.domain.MenuProductEntity;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroupEntity;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.ProductEntity;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuService2Test {

  @Mock
  private MenuRepository menuRepository;
  @Mock
  private MenuGroupRepository menuGroupRepository;
  @Mock
  private ProductRepository productRepository;

  private String menuName;
  private Double menuPrice;
  private Long menuGroupId;
  private ProductEntity productEntity1;
  private ProductEntity productEntity2;
  private MenuRequest.MenuProductRequest menuProductRequest1;
  private MenuRequest.MenuProductRequest menuProductRequest2;
  private MenuProductEntity menuProductEntity1;
  private MenuProductEntity menuProductEntity2;

  private MenuService2 menuService;

  @BeforeEach
  void setUp() {
    //given
    menuName = "메뉴이름";
    menuPrice = 4_000D;
    menuGroupId = 1L;
    productEntity1 = new ProductEntity(1L, "상품1", 1_000D);
    productEntity2 = new ProductEntity(2L, "상품2", 2_000D);
    menuProductRequest1 = new MenuRequest.MenuProductRequest(productEntity1.getId(), 2L);
    menuProductRequest2 = new MenuRequest.MenuProductRequest(productEntity2.getId(), 1L);
    menuProductEntity1 = new MenuProductEntity(1L, productEntity1.getId(), 2L);
    menuProductEntity2 = new MenuProductEntity(2L, productEntity2.getId(), 1L);

    menuService = new MenuService2(menuRepository, menuGroupRepository, productRepository);
  }

  @DisplayName("메뉴 이름, 가격, 메뉴그룹, 메뉴상품 목록을 입력받아 저장할 수 있다.")
  @Test
  void createTest() {
    //given
    MenuRequest menu = new MenuRequest(menuName, menuPrice, menuGroupId, Arrays.asList(menuProductRequest1, menuProductRequest2));
    when(menuGroupRepository.existsById(menuGroupId)).thenReturn(true);
    when(productRepository.findAllById(Arrays.asList(productEntity1.getId(), productEntity2.getId()))).thenReturn(Arrays.asList(productEntity1, productEntity2));
    when(menuRepository.save(any())).thenReturn(new MenuEntity(1L, menuName, menuPrice, menuGroupId, Arrays.asList(menuProductEntity1, menuProductEntity2)));

    //when
    MenuResponse savedMenu = menuService.create(menu);

    //then
    assertAll(
        () -> assertThat(savedMenu.getId()).isPositive(),
        () -> assertThat(savedMenu.getName()).isEqualTo(menu.getName()),
        () -> assertThat(savedMenu.getPrice()).isEqualTo(BigDecimal.valueOf(menu.getPrice())),
        () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
        () -> assertThat(savedMenu.getMenuProducts()).contains(MenuResponse.MenuProductResponse.from(menuProductEntity1), MenuResponse.MenuProductResponse.from(menuProductEntity2))
    );
    verify(menuGroupRepository, VerificationModeFactory.times(1)).existsById(menuGroupId);
    verify(productRepository, VerificationModeFactory.times(1)).findAllById(Arrays.asList(productEntity1.getId(), productEntity2.getId()));
    verify(menuRepository, VerificationModeFactory.times(1)).save(any());
  }

  @DisplayName("메뉴 저장시 가격은 0 이상이다.")
  @NullSource
  @ValueSource(doubles = {-0.1, -1})
  @ParameterizedTest
  void createFailCausePrice(Double givenPrice) {
    //given
    MenuRequest menu = new MenuRequest(menuName, givenPrice, menuGroupId, Arrays.asList(menuProductRequest1, menuProductRequest2));
    when(menuGroupRepository.existsById(menuGroupId)).thenReturn(true);
    when(productRepository.findAllById(Arrays.asList(productEntity1.getId(), productEntity2.getId()))).thenReturn(Arrays.asList(productEntity1, productEntity2));

    //when & then
    assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("메뉴그룹은 존재하는 메뉴그룹만 지정할 수 있다.")
  @Test
  void createFailCauseNotExistMenuGroup() {
    //given
    long notExistMenuGroupId = 999L;
    MenuRequest menu = new MenuRequest(menuName, menuPrice, notExistMenuGroupId, Arrays.asList(menuProductRequest1, menuProductRequest2));
    when(menuGroupRepository.existsById(notExistMenuGroupId)).thenReturn(false);

    //when & then
    assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("가격이 메뉴상품 목록 세부 가격의 합보다 작거나 같아야 한다.")
  @Test
  void createFailCauseNotMatchedPrice() {
    //given
    Double menuPriceLargerThanMenuProductsAmount = 5_000D;
    MenuRequest menu = new MenuRequest(menuName, menuPriceLargerThanMenuProductsAmount, menuGroupId, Arrays.asList(menuProductRequest1, menuProductRequest2));
    when(menuGroupRepository.existsById(menuGroupId)).thenReturn(true);
    when(productRepository.findAllById(Arrays.asList(productEntity1.getId(), productEntity2.getId()))).thenReturn(Arrays.asList(productEntity1, productEntity2));

    //when & then
    assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("전체 메뉴의 목록을 조회할 수 있다.")
  @Test
  void findAllTest() {
    //given
    MenuEntity savedMenu1 = new MenuEntity(1L, "메뉴이름1", 2_000D, menuGroupId, Collections.singletonList(menuProductEntity1));
    MenuEntity savedMenu2 = new MenuEntity(2L, "메뉴이름2", 2_000D, menuGroupId, Collections.singletonList(menuProductEntity2));
    when(menuRepository.findAll()).thenReturn(Arrays.asList(savedMenu1, savedMenu2));

    //when
    List<MenuResponse> menuList = menuService.findAllMenus();

    //then
    assertThat(menuList).contains(MenuResponse.from(savedMenu1), MenuResponse.from(savedMenu2));
  }
}

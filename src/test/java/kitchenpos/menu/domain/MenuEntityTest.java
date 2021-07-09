package kitchenpos.menu.domain;

import kitchenpos.product.domain.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuEntityTest {

  private String menuName;
  private Double menuPrice;
  private Long menuGroupId;
  private ProductEntity productEntity1;
  private ProductEntity productEntity2;
  private MenuProductEntity menuProductEntity1;
  private MenuProductEntity menuProductEntity2;
  private List<MenuProductEntity> menuProductEntities;

  @BeforeEach
  void setUp() {
    //given
    menuName = "메뉴이름";
    menuPrice = 4_000D;
    menuGroupId = 1L;
    productEntity1 = new ProductEntity(1L, "상품1", 1_000D);
    productEntity2 = new ProductEntity(2L, "상품2", 2_000D);
    menuProductEntity1 = new MenuProductEntity(productEntity1.getId(), 2L);
    menuProductEntity2 = new MenuProductEntity(productEntity2.getId(), 1L);
    menuProductEntities = Arrays.asList(menuProductEntity1, menuProductEntity2);
  }


  @DisplayName("메뉴 이름, 가격, 메뉴그룹, 메뉴상품 목록을 입력받아 메뉴를 만든다.")
  @Test
  void createTest() {
    //when
    MenuEntity menuEntity = new MenuEntity(menuName, menuPrice, menuGroupId, menuProductEntities);

    //then
    assertAll(
        () -> assertThat(menuEntity.getName()).isEqualTo(menuName),
        () -> assertThat(menuEntity.getPrice()).isEqualTo(BigDecimal.valueOf(menuPrice)),
        () -> assertThat(menuEntity.getMenuGroupId()).isEqualTo(menuGroupId),
        () -> assertThat(menuEntity.getMenuProducts()).contains(menuProductEntity1, menuProductEntity2)
    );
  }

  @DisplayName("메뉴 저장시 가격은 0 이상이다.")
  @NullSource
  @ValueSource(doubles = {-0.1, -1})
  @ParameterizedTest
  void createFailCausePrice(Double givenPrice) {
    //when & then
    assertThatThrownBy(() -> new MenuEntity(menuName, givenPrice, menuGroupId, menuProductEntities)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("가격이 메뉴상품 목록 세부 가격의 합보다 작거나 같아야 한다.")
  @Test
  void createFailCauseNotMatchedPrice() {
    //given
    Double menuPriceLargerThanMenuProductsAmount = 5_000D;
    MenuEntity invalidPriceMenu = new MenuEntity(menuName, menuPriceLargerThanMenuProductsAmount, menuGroupId, menuProductEntities);

    //when & then
    assertThatThrownBy(() -> invalidPriceMenu.validatePrice(Arrays.asList(productEntity1, productEntity2))).isInstanceOf(IllegalArgumentException.class);
  }
}

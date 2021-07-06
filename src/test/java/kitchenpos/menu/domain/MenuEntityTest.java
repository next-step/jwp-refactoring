package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroupEntity;
import kitchenpos.product.domain.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuEntityTest {

  private String menuName;
  private Double menuPrice;
  private MenuGroupEntity menuGroup;
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
    menuGroup = new MenuGroupEntity(1L, "그룹1");
    productEntity1 = new ProductEntity("상품1", 1_000D);
    productEntity2 = new ProductEntity("상품2", 2_000D);
    menuProductEntity1 = new MenuProductEntity(productEntity1, 2L);
    menuProductEntity2 = new MenuProductEntity(productEntity2, 1L);
    menuProductEntities = Arrays.asList(menuProductEntity1, menuProductEntity2);
  }


  @DisplayName("메뉴 이름, 가격, 메뉴그룹, 메뉴상품 목록을 입력받아 메뉴를 만든다.")
  @Test
  void createTest() {
    //when
    MenuEntity menuEntity = new MenuEntity(menuName, menuPrice, menuGroup, menuProductEntities);

    //then
    assertAll(
        () -> assertThat(menuEntity.getName()).isEqualTo(menuName),
        () -> assertThat(menuEntity.getPrice()).isEqualTo(Price.from(menuPrice)),
        () -> assertThat(menuEntity.getMenuGroup()).isEqualTo(menuGroup),
        () -> assertThat(menuEntity.getMenuProducts()).contains(menuProductEntity1, menuProductEntity2)
    );
  }

  @DisplayName("메뉴 저장시 가격은 0 이상이다.")
  @NullSource
  @ValueSource(doubles = {-0.1, -1})
  @ParameterizedTest
  void createFailCausePrice(Double givenPrice) {
    //when & then
    assertThatThrownBy(() -> new MenuEntity(menuName, givenPrice, menuGroup, menuProductEntities)).isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("가격이 메뉴상품 목록 세부 가격의 합보다 작거나 같아야 한다.")
  @Test
  void createFailCauseNotMatchedPrice() {
    //given
    Double menuPriceLargerThanMenuProductsAmount = 5_000D;

    //when & then
    assertThatThrownBy(() -> new MenuEntity(menuName, menuPriceLargerThanMenuProductsAmount, menuGroup, menuProductEntities)).isInstanceOf(IllegalArgumentException.class);
  }
}

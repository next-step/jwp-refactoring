package kitchenpos.menugroup.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupEntityTest {

  @DisplayName("메뉴그룹의 이름을 입력받아 메뉴그룹을 만든다.")
  @Test
  void createTest() {
    //given
    String menuGroupName = "그룹1";

    //when
    MenuGroupEntity menuGroupEntity = new MenuGroupEntity(menuGroupName);

    //then
    assertThat(menuGroupEntity.getName()).isEqualTo(menuGroupName);
  }

}

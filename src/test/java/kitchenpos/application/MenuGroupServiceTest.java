package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MenuGroupServiceTest {

  @Autowired
  private MenuGroupService menuGroupService;

  @DisplayName("메뉴 그룹을 등록한다.")
  @Test
  void create() {
    // given
    MenuGroup menuGroup = new MenuGroup("음료수");

    // when
    MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

    // then
    assertThat(savedMenuGroup.getId()).isNotNull();
    assertThat(savedMenuGroup.getName()).isEqualTo("음료수");
  }

  @DisplayName("메뉴 그룹 목록을 조회한다.")
  @Test
  void list() {
    // given
    MenuGroup menuGroup = new MenuGroup("음료수");
    MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

    // when
    List<MenuGroup> list = menuGroupService.list();

    // then
    assertThat(list).extracting("id").contains(savedMenuGroup.getId());
  }
}

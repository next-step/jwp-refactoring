package kitchenpos.menugroup.application;

import kitchenpos.menugroup.dao.MenuGroupDao;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

  @Mock
  private MenuGroupDao menuGroupDao;

  @DisplayName("메뉴그룹의 이름을 입력받아 저장할 수 있다.")
  @Test
  void createTest() {
    //given
    MenuGroup menuGroup = new MenuGroup("그룹1");
    when(menuGroupDao.save(any())).thenReturn(new MenuGroup(1L, "그룹1"));
    MenuGroupService menuGroupService = new MenuGroupService(menuGroupDao);

    //when
    MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

    //then
    assertAll(() -> assertThat(savedMenuGroup.getId()).isNotNull(),
        () -> assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName()));
    verify(menuGroupDao, VerificationModeFactory.times(1)).save(menuGroup);
  }

  @DisplayName("전체 메뉴그룹의 목록을 조회할 수 있다.")
  @Test
  void findAllTest() {
    //given
    MenuGroup menuGroup1 = new MenuGroup("그룹1");
    MenuGroup menuGroup2 = new MenuGroup("그룹2");
    MenuGroup menuGroup3 = new MenuGroup("그룹3");
    when(menuGroupDao.findAll()).thenReturn(Arrays.asList(menuGroup1, menuGroup2, menuGroup3));
    MenuGroupService menuGroupService = new MenuGroupService(menuGroupDao);

    //when
    List<MenuGroup> menuGroupList = menuGroupService.list();

    //then
    assertThat(menuGroupList).containsExactly(menuGroup1, menuGroup2, menuGroup3);
  }

}

package kitchenpos.menugroup.application;

import kitchenpos.menugroup.domain.MenuGroupEntity;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
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
class MenuGroupService2Test {

  @Mock
  private MenuGroupRepository menuGroupRepository;

  @DisplayName("메뉴그룹의 이름을 입력받아 저장할 수 있다.")
  @Test
  void createTest() {
    //given
    MenuGroupRequest newMenuGroup = new MenuGroupRequest("그룹1");
    when(menuGroupRepository.save(any())).thenReturn(new MenuGroupEntity(1L, "그룹1"));
    MenuGroupService2 menuGroupService = new MenuGroupService2(menuGroupRepository);

    //when
    MenuGroupResponse savedMenuGroup = menuGroupService.create(newMenuGroup);

    //then
    assertAll(() -> assertThat(savedMenuGroup.getId()).isNotNull(),
        () -> assertThat(savedMenuGroup.getName()).isEqualTo(newMenuGroup.getName()));
    verify(menuGroupRepository, VerificationModeFactory.times(1)).save(any());
  }

  @DisplayName("전체 메뉴그룹의 목록을 조회할 수 있다.")
  @Test
  void findAllTest() {
    //given
    MenuGroupEntity menuGroup1 = new MenuGroupEntity("그룹1");
    MenuGroupEntity menuGroup2 = new MenuGroupEntity("그룹2");
    MenuGroupEntity menuGroup3 = new MenuGroupEntity("그룹3");
    when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(menuGroup1, menuGroup2, menuGroup3));
    MenuGroupService2 menuGroupService = new MenuGroupService2(menuGroupRepository);

    //when
    List<MenuGroupResponse> menuGroupList = menuGroupService.findAllMenuGroups();

    //then
    assertThat(menuGroupList).isEqualTo(MenuGroupResponse.ofList(Arrays.asList(menuGroup1, menuGroup2, menuGroup3)));
  }

}

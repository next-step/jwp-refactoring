package kitchenpos.menu.domain.menugroup;

import kitchenpos.menu.aplication.MenuGroupService;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kitchenpos.menu.domain.fixture.MenuGroupDomainFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("메뉴 그룹 관리")
class MenuGroupMockitoTest {

    private MenuGroupRepository menuGroupRepository;
    private MenuGroupService menuGroupService;

    private void setUpMock() {
        menuGroupRepository = mock(MenuGroupRepository.class);
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @Test
    @DisplayName("메뉴 그룹 생성")
    public void createMenu() {
        //given
        setUpMock();
        when(menuGroupRepository.save(any(MenuGroup.class))).thenReturn(패밀리_세트);

        //when
        MenuGroupResponse actual = menuGroupService.saveMenuGroup(패밀리_세트_요청);

        //then
        assertThat(actual.getName()).isEqualTo(패밀리_세트.getName());
    }

    @Test
    @DisplayName("메뉴 그룹 조회")
    public void findAllMenuGroup() {
        //given
        setUpMock();
        when(menuGroupRepository.findAll()).thenReturn(Lists.newArrayList(일인_세트, 패밀리_세트));

        //when
        List<MenuGroupResponse> actual = menuGroupService.findAllMenuGroup();

        //then
        assertThat(actual).hasSize(2);
    }

}

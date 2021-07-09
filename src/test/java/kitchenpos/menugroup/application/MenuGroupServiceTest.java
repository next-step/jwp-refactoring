package kitchenpos.menugroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.utils.domain.MenuGroupObjects;

@DisplayName("메뉴그룹 서비스")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroupObjects menuGroupObjects;
    private MenuGroup menuGroup1;
    private MenuGroupRequest createMenuGroupRequest;

    @BeforeEach
    void setUp() {
        menuGroupObjects = new MenuGroupObjects();
        menuGroup1 = menuGroupObjects.getMenuGroup1();
        createMenuGroupRequest = menuGroupObjects.getMenuGroupRequest1();
    }

    @Test
    @DisplayName("메뉴그룹을 생성한다.")
    void create_menuGroup() {
        // given
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(menuGroupObjects.getMenuGroup1());

        // when
        MenuGroupResponse savedMenuGroup = menuGroupService.create(createMenuGroupRequest);

        // then
        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroupObjects.getMenuGroup1().getName());
    }

    @Test
    @DisplayName("메뉴그룹 목록을 조회한다.")
    void find_menuGroupList() {
        // given
        List<MenuGroupResponse> menuGroupResponses = menuGroupObjects.getMenuGroupResponses();
        given(menuGroupRepository.findAll()).willReturn(menuGroupObjects.getMenuGroups());

        // when
        List<MenuGroupResponse> resultResponses = menuGroupService.findAllMenuGroups();

        // then
        assertThat(resultResponses.size()).isEqualTo(menuGroupResponses.size());
    }
}

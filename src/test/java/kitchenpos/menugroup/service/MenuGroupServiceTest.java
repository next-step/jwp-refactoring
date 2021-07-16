package kitchenpos.menugroup.service;

import static kitchenpos.menugroup.domain.MenuGroupTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.domain.MenuGroupTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 그룹 서비스")
class MenuGroupServiceTest {

    @InjectMocks
    MenuGroupService menuGroupService;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴 그룹을 생성한다")
    void create() {
        // given
        MenuGroupRequest 바베큐치킨메뉴_요청 = new MenuGroupRequest("바베큐치킨메뉴");
        MenuGroup 바베큐치킨메뉴 = MenuGroupTest.menuGroup(100L, "바베큐치킨메뉴");
        when(menuGroupRepository.save(any())).thenReturn(바베큐치킨메뉴);

        // when
        MenuGroup savedGroup = menuGroupService.create(바베큐치킨메뉴_요청);

        // then
        assertThat(savedGroup.getId()).isEqualTo(바베큐치킨메뉴.getId());
        assertThat(savedGroup.getName()).isEqualTo(바베큐치킨메뉴.getName());
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 가져온다")
    void list() {
        // given
        List<MenuGroup> groups = Arrays.asList(두마리메뉴, 한마리메뉴, 순살파닭두마리메뉴);
        when(menuGroupRepository.findAll()).thenReturn(groups);

        // when
        List<MenuGroup> allGroups = menuGroupService.list();

        // then
        assertThat(allGroups).containsExactly(두마리메뉴, 한마리메뉴, 순살파닭두마리메뉴);
        assertIterableEquals(groups, allGroups);
    }
}

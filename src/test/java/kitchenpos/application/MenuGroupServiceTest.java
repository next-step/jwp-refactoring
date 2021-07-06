package kitchenpos.application;

import static kitchenpos.domain.MenuGroupTest.*;
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

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuGroupTest;

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
        MenuGroup 바베큐치킨메뉴 = new MenuGroup(100L, "바베큐치킨메뉴");
        when(menuGroupRepository.save(바베큐치킨메뉴)).thenReturn(바베큐치킨메뉴);

        // when
        MenuGroup savedGroup = menuGroupService.create(바베큐치킨메뉴);

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

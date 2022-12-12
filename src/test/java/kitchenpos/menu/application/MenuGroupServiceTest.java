package kitchenpos.menu.application;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 뼈치킨;
    private MenuGroup 순살치킨;

    @BeforeEach
    void setUp() {
        뼈치킨 = new MenuGroup("뼈치킨");
        순살치킨 = new MenuGroup("순살치킨");

        ReflectionTestUtils.setField(뼈치킨, "id", 1L);
        ReflectionTestUtils.setField(순살치킨, "id", 2L);
    }

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void 메뉴그룹_생성() {
        // given
        when(menuGroupRepository.save(any(MenuGroup.class))).thenReturn(뼈치킨);

        // when
        MenuGroupResponse response = menuGroupService.create(new MenuGroupRequest("뼈치킨"));

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(뼈치킨.getId()),
                () -> assertThat(response.getName()).isEqualTo(뼈치킨.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void 메뉴그룹_목록_조회() {
        // given
        List<MenuGroup> menuGroups = Arrays.asList(뼈치킨, 순살치킨);
        when(menuGroupRepository.findAll()).thenReturn(menuGroups);

        // when
        List<MenuGroupResponse> selectMenuGroups = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(selectMenuGroups).hasSize(menuGroups.size()),
                () -> assertThat(selectMenuGroups.stream().map(MenuGroupResponse::getName)
                        .collect(Collectors.toList())).containsExactly(뼈치킨.getName(), 순살치킨.getName())
        );
    }
}

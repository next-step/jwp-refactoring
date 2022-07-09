package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 양식;
    private MenuGroup 한식;

    @BeforeEach
    void init() {
        양식 = 메뉴_그룹_생성(1L, "양식");
        한식 = 메뉴_그룹_생성(2L, "한식");
    }

    @Test
    @DisplayName("메뉴그룹을 생성한다.")
    void createMenuGroup() {
        // given
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(양식);

        // when
        MenuGroupResponse menuGroupResponse = menuGroupService.create(new MenuGroupRequest(양식));

        // then
        assertAll(
            () -> assertThat(menuGroupResponse).isNotNull(),
            () -> assertThat(menuGroupResponse.getName()).isEqualTo(양식.getName())
        );
    }

    @Test
    @DisplayName("메뉴그룹 목록을 조회한다.")
    void findAll() {
        // given
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(양식, 한식));

        // when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        // then
        assertAll(
            () -> assertThat(menuGroups).isNotNull(),
            () -> assertThat(menuGroups.size()).isEqualTo(2)
        );
    }

    public static MenuGroup 메뉴_그룹_생성(Long id, String name) {
        return new MenuGroup(id, name);
    }
}

package kitchenpos.menugroup.application;

import static kitchenpos.utils.DomainFixtureFactory.createMenuGroup;
import static kitchenpos.utils.DomainFixtureFactory.createMenuGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    private MenuGroupService menuGroupService;
    private MenuGroup 한마리메뉴;

    @BeforeEach
    void setUp() {
        한마리메뉴 = createMenuGroup(2L, "한마리메뉴");
    }

    @DisplayName("메뉴 그룹 생성 테스트")
    @Test
    void create() {
        MenuGroupRequest menuGroupRequest = createMenuGroupRequest("한마리메뉴");
        given(menuGroupRepository.save(menuGroupRequest.toMenuGroup())).willReturn(한마리메뉴);
        MenuGroupResponse menuGroup = menuGroupService.create(menuGroupRequest);
        assertAll(
                () -> assertThat(menuGroup.getName()).isEqualTo("한마리메뉴"),
                () -> assertThat(menuGroup.getId()).isEqualTo(한마리메뉴.id())
        );
    }

    @DisplayName("메뉴 그룹 목록 조회 테스트")
    @Test
    void list() {
        given(menuGroupRepository.findAll()).willReturn(Lists.newArrayList(한마리메뉴));
        List<MenuGroupResponse> menuGroups = menuGroupService.list();
        assertThat(menuGroups).containsExactlyElementsOf(Lists.newArrayList(MenuGroupResponse.from(한마리메뉴)));
    }

    @DisplayName("메뉴 그룹 찾기 테스트")
    @Test
    void findMenuGroup() {
        given(menuGroupRepository.findById(한마리메뉴.id())).willReturn(Optional.ofNullable(한마리메뉴));
        MenuGroup menuGroup = menuGroupService.findMenuGroup(한마리메뉴.id());
        assertThat(menuGroup).isEqualTo(한마리메뉴);
    }

    @DisplayName("메뉴 그룹 찾을 수 없음")
    @Test
    void findMenuGroupEmpty() {
        given(menuGroupRepository.findById(한마리메뉴.id())).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuGroupService.findMenuGroup(한마리메뉴.id()))
                .withMessage("메뉴그룹을 찾을 수 없습니다.");
    }
}

package kitchenpos.MenuGroup;

import kitchenpos.menuGroup.application.MenuGroupService;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import kitchenpos.menuGroup.dto.MenuGroupRequest;
import kitchenpos.menuGroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.util.testFixture.두마리_메뉴_그룹_생성;
import static kitchenpos.util.testFixture.한마리_메뉴_그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @InjectMocks
    MenuGroupService menuGroupService;

    @Mock
    MenuGroupRepository menuGroupRepository;

    private MenuGroup 한마리메뉴;
    private MenuGroup 두마리메뉴;

    @BeforeEach
    void setUp() {
        한마리메뉴 = 한마리_메뉴_그룹_생성();
        두마리메뉴 = 두마리_메뉴_그룹_생성();
    }

    @DisplayName("메뉴 그룹 등록")
    @Test
    void createMenuGroup() {
        // given
        when(menuGroupRepository.save(any()))
                .thenReturn(두마리메뉴);

        // when
        MenuGroupResponse result = menuGroupService.create(new MenuGroupRequest(두마리메뉴.getName()));

        // then
        assertThat(result.getName()).isEqualTo(두마리메뉴.getName());
    }

    @DisplayName("메뉴 그룹 전체 조회")
    @Test
    void findAllMenuGroups() {
        // given
        when(menuGroupRepository.findAll())
                .thenReturn(Arrays.asList(두마리메뉴, 한마리메뉴));

        // when
        List<MenuGroupResponse> list = menuGroupService.list();

        // then
        assertThat(list).hasSize(2);
    }
}

package kitchenpos.menugroup.application;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static kitchenpos.menugroup.fixtures.MenuGroupFixtures.두마리메뉴그룹요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * packageName : kitchenpos.application
 * fileName : MenuGroupServiceTest
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
@DisplayName("메뉴그룹 통합 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    private final MenuGroupRequest request = 두마리메뉴그룹요청();

    @Mock
    MenuGroupRepository menuGroupRepository;

    @InjectMocks
    MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 조회할 수 있다.")
    public void list() {
        // given
        given(menuGroupRepository.findAll()).willReturn(Lists.newArrayList(request.toEntity()));

        // when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).hasSize(1);
    }

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    public void create() {
        // given
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(request.toEntity());

        // when
        MenuGroupResponse actual = menuGroupService.create(request);

        // then
        assertThat(actual.getName()).isEqualTo(request.getName());
    }
}

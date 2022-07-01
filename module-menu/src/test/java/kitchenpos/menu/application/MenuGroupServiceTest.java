package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 그룹 관련 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    MenuGroupRepository menuGroupRepository;

    @InjectMocks
    MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성할 수 있다")
    @Test
    void create() {
        // given
        MenuGroup request = new MenuGroup(null, "런치메뉴");
        MenuGroup 예상값 = new MenuGroup(1L, "런치메뉴");
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(예상값);

        // when
        MenuGroupResponse 메뉴_그룹_생성_결과 = 메뉴_그룹_생성(request);

        // then
        메뉴_그룹_값_비교(메뉴_그룹_생성_결과, MenuGroupResponse.of(예상값));
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다")
    @Test
    void list() {
        // given
        List<MenuGroup> 예상값 = Arrays.asList(
                new MenuGroup(1L, "런치메뉴"),
                new MenuGroup(1L, "디너메뉴")
        );
        given(menuGroupRepository.findAll()).willReturn(예상값);

        // when
        List<MenuGroupResponse> 메뉴_그룹_목록_조회_결과 = menuGroupService.list();

        // then
        assertAll(
                () -> 메뉴_그룹_값_비교(메뉴_그룹_목록_조회_결과.get(0), MenuGroupResponse.of(예상값.get(0))),
                () -> 메뉴_그룹_값_비교(메뉴_그룹_목록_조회_결과.get(1), MenuGroupResponse.of(예상값.get(1)))
        );
    }

    private MenuGroupResponse 메뉴_그룹_생성(MenuGroup menuGroup) {
        return menuGroupService.create(MenuGroupRequest.of(menuGroup));
    }

    private void 메뉴_그룹_값_비교(MenuGroupResponse result, MenuGroupResponse expectation) {
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(expectation.getId()),
                () -> assertThat(result.getName()).isEqualTo(expectation.getName())
        );
    }
}

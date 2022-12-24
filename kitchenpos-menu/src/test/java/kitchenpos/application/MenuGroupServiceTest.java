package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.domain.MenuGroupFixture.메뉴그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 그룹 테스트")
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다")
    @Test
    void 메뉴_그룹_생성() {
        // given
        MenuGroupRequest 오일 = MenuGroupRequest.of(메뉴그룹(1L, "오일"));
        given(menuGroupRepository.save(any())).willReturn(오일.toMenuGroup());

        // when
        MenuGroupResponse menuGroupResponse = menuGroupService.create(오일);

        // then
        verify(menuGroupRepository).save(any());
        assertThat(menuGroupResponse.getName()).isEqualTo("오일");
    }

    @DisplayName("전체 메뉴 그룹 목록을 조회한다")
    @Test
    void 전체_메뉴_그룹_목록_조회() {
        // given
        MenuGroup 오일 = 메뉴그룹(1L, "오일");
        MenuGroup 토마토 = 메뉴그룹(2L, "토마토");
        MenuGroup 크림 = 메뉴그룹(3L, "크림");
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(오일, 토마토, 크림));

        // when
        List<MenuGroupResponse> menuGroupsResponses = menuGroupService.list();
        List<String> menuGroupNames = menuGroupsResponses.stream()
                .map(MenuGroupResponse::getName)
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(menuGroupsResponses).hasSize(3),
                () -> assertThat(menuGroupNames).containsExactly(오일.getName(), 토마토.getName(), 크림.getName())
        );
    }
}

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
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void createMenuGroup() {
        // given
        MenuGroupRequest 생성할_메뉴그룹_요청 = new MenuGroupRequest("커플코스A");
        MenuGroup 생성할_메뉴그룹 = new MenuGroup(1L, 생성할_메뉴그룹_요청.getName());
        given(menuGroupRepository.save(any()))
                .willReturn(생성할_메뉴그룹);

        // when
        MenuGroupResponse 생성된_메뉴그룹_응답 = menuGroupService.create(생성할_메뉴그룹_요청);

        // then
        메뉴그룹_생성_성공(생성된_메뉴그룹_응답, 생성할_메뉴그룹_요청);
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void listMenuGroup() {
        // given
        List<MenuGroup> 조회할_메뉴그룹_목록 = Arrays.asList(
                new MenuGroup(1L, "커플코스A"),
                new MenuGroup(2L, "커플코스B"),
                new MenuGroup(3L, "런치코스A")
        );
        given(menuGroupRepository.findAll())
                .willReturn(조회할_메뉴그룹_목록);

        // when
        List<MenuGroupResponse> 조회된_메뉴그룹_목록 = menuGroupService.list();

        // then
        메뉴그룹_목록_조회_성공(조회된_메뉴그룹_목록, 조회할_메뉴그룹_목록);
    }

    private void 메뉴그룹_생성_성공(MenuGroupResponse actual, MenuGroupRequest expected) {
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    private void 메뉴그룹_목록_조회_성공(List<MenuGroupResponse> 조회된_메뉴그룹_목록, List<MenuGroup> 조회할_메뉴그룹_목록) {
        assertAll(
                () -> assertThat(조회된_메뉴그룹_목록).hasSize(조회할_메뉴그룹_목록.size()),
                () -> assertThat(조회된_메뉴그룹_목록).extracting("id", "name")
                        .containsExactly(
                                tuple(조회할_메뉴그룹_목록.get(0).getId(), 조회할_메뉴그룹_목록.get(0).getName()),
                                tuple(조회할_메뉴그룹_목록.get(1).getId(), 조회할_메뉴그룹_목록.get(1).getName()),
                                tuple(조회할_메뉴그룹_목록.get(2).getId(), 조회할_메뉴그룹_목록.get(2).getName()))
        );
    }
}

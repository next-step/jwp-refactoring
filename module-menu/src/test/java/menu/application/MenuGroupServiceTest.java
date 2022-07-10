package menu.application;

import menu.repository.MenuGroupRepository;
import menu.domain.MenuGroup;
import menu.dto.MenuGroupRequest;
import menu.dto.MenuGroupResponse;
import menu.application.MenuGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    private MenuGroup 메뉴_그룹;

    @BeforeEach
    void setUp() {
        메뉴_그룹 = new MenuGroup(1L, "세트 메뉴");
    }

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        // given
        MenuGroupRequest request = new MenuGroupRequest("세트 메뉴");
        given(menuGroupRepository.save(any())).willReturn(메뉴_그룹);

        // when
        MenuGroupResponse response = menuGroupService.create(request);

        // then
        assertThat(response.getName()).isEqualTo("세트 메뉴");
    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void list() {
        // given
        given(menuGroupRepository.findAll()).willReturn(Collections.singletonList(메뉴_그룹));

        // when
        List<MenuGroupResponse> responses = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(responses).isNotEmpty(),
                () -> assertThat(responses).hasSize(1),
                () -> assertThat(responses.stream().map(MenuGroupResponse::getName)).containsExactly("세트 메뉴")
        );
    }
}

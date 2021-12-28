package kitchenpos.menu.applicaiton;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
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

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    MenuGroupRequest 한마리메뉴;
    MenuGroupRequest 두마리메뉴;

    @BeforeEach
    void setUp() {
        한마리메뉴 = MenuGroupRequest.from("한마리메뉴");
        두마리메뉴 = MenuGroupRequest.from("두마리메뉴");
    }

    @Test
    void 메뉴_그룹_생성() {
        // given
        given(menuGroupRepository.save(any())).willReturn(MenuGroup.from(두마리메뉴.getName()));

        // when
        MenuGroupResponse actual = menuGroupService.create(두마리메뉴);

        // then
        assertThat(actual.getName()).isEqualTo("두마리메뉴");
    }

    @Test
    void 메뉴_그룹_조회() {
        // given
        List<MenuGroup> menuGroups = Arrays.asList(MenuGroup.from(한마리메뉴.getName()), MenuGroup.from(두마리메뉴.getName()));
        given(menuGroupRepository.findAll()).willReturn(menuGroups);

        // when
        List<MenuGroupResponse> actual = menuGroupService.list();

        // then
        assertAll(() -> {
            assertThat(actual).hasSize(2);
            assertThat(actual).extracting("name")
                    .containsExactly("한마리메뉴", "두마리메뉴");
        });
    }
}

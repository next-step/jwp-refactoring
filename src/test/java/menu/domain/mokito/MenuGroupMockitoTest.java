package menu.domain.mokito;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.assertj.core.util.*;
import org.junit.jupiter.api.*;

import fixture.*;
import menu.application.*;
import menu.dto.*;
import menu.repository.*;

@DisplayName("메뉴 그룹 관련(Mockito)")
class MenuGroupMockitoTest {
    private MenuGroupRepository menuGroupRepository;
    private MenuGroupService menuGroupService;

    private void setUpMock() {
        menuGroupRepository = mock(MenuGroupRepository.class);
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @BeforeEach
    void setUp() {
        menuGroupRepository = mock(MenuGroupRepository.class);
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @DisplayName("메뉴 그룹 생성하기")
    @Test
    void createTest() {
        when(menuGroupRepository.save(any())).thenReturn(MenuGroupFixture.메뉴그룹_한마리메뉴);

        MenuGroupRequest request = MenuGroupRequest.from(MenuGroupFixture.메뉴그룹_한마리메뉴.getName());
        assertThat(menuGroupService.save(request)).isInstanceOf(MenuGroupResponse.class);
    }

    @DisplayName("메뉴 조회하기")
    @Test
    void findAllTest() {
        when(menuGroupRepository.findAll()).thenReturn(Lists.newArrayList(
            MenuGroupFixture.메뉴그룹_두마리메뉴, MenuGroupFixture.메뉴그룹_한마리메뉴));
        assertThat(menuGroupService.findAll()).containsExactly(MenuGroupResponse.from(MenuGroupFixture.메뉴그룹_두마리메뉴), MenuGroupResponse.from(
            MenuGroupFixture.메뉴그룹_한마리메뉴));
    }
}

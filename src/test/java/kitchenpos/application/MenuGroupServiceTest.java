package kitchenpos.application;

import kitchenpos.fixture.TestMenuGroupFactory;
import kitchenpos.fixture.TestMenuGroupRequestFactory;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 분식류;
    private MenuGroup 양식류;

    @BeforeEach
    void setUp() {
        분식류 = TestMenuGroupFactory.create("분식류");
        양식류 = TestMenuGroupFactory.create("양식류");
    }

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다")
    void create() {
        //given
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(분식류);

        //when
        MenuGroupResponse menuGroupResponse = menuGroupService.create(TestMenuGroupRequestFactory.create(분식류));

        //then
        assertThat(menuGroupResponse).isEqualTo(MenuGroupResponse.of(분식류));
    }

    @Test
    @DisplayName("전체 메뉴 그룹을 조회할 수 있다")
    void list() {
        //given
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(분식류, 양식류));

        //when
        List<MenuGroupResponse> results = menuGroupService.list();

        //then
        assertThat(results).containsExactly(MenuGroupResponse.of(분식류), MenuGroupResponse.of(양식류));
    }

}

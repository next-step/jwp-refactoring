package kitchenpos.application;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.MenuGroupTestFixture.중국집1인메뉴세트그룹;
import static kitchenpos.fixture.MenuGroupTestFixture.중국집1인메뉴세트그룹요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroupRequest 중국집1인메뉴세트그룹요청;
    private MenuGroup 중국집1인메뉴세트그룹;

    @BeforeEach
    void setUp() {
        중국집1인메뉴세트그룹요청 = 중국집1인메뉴세트그룹요청();
        중국집1인메뉴세트그룹 = 중국집1인메뉴세트그룹(중국집1인메뉴세트그룹요청);
    }

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        // given
        when(menuGroupRepository.save(any())).thenReturn(중국집1인메뉴세트그룹);

        // when
        MenuGroupResponse saveMenuGroup = menuGroupService.create(중국집1인메뉴세트그룹요청);

        // then
        assertThat(saveMenuGroup).isNotNull();
    }

    @DisplayName("메뉴 그룹 전체 목록을 조회한다.")
    @Test
    void list() {
        // given
        List<MenuGroup> menuGroups = Collections.singletonList(중국집1인메뉴세트그룹);
        when(menuGroupRepository.findAll()).thenReturn(menuGroups);

        // when
        List<MenuGroupResponse> findMenuGroups = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(findMenuGroups).hasSize(menuGroups.size()),
                () -> assertThat(findMenuGroups).containsExactly(MenuGroupResponse.from(중국집1인메뉴세트그룹))
        );
    }
}

package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.MenuGroupTestFixture.중국집_1인_메뉴_세트;
import static kitchenpos.fixture.MenuGroupTestFixture.중국집_1인_메뉴_세트_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroupRequest 중국집_1인_메뉴_세트_요청;
    private MenuGroup 중국집_1인_메뉴_세트;

    @BeforeEach
    void setUp() {
        중국집_1인_메뉴_세트_요청 = 중국집_1인_메뉴_세트_요청();
        중국집_1인_메뉴_세트 = 중국집_1인_메뉴_세트(중국집_1인_메뉴_세트_요청);
    }

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        // given
        when(menuGroupDao.save(any())).thenReturn(중국집_1인_메뉴_세트);

        // when
        MenuGroupResponse saveMenuGroup = menuGroupService.create(중국집_1인_메뉴_세트_요청);

        // then
        assertThat(saveMenuGroup.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹 전체 목록을 조회한다.")
    @Test
    void list() {
        // given
        List<MenuGroup> menuGroups = Collections.singletonList(중국집_1인_메뉴_세트);
        when(menuGroupDao.findAll()).thenReturn(menuGroups);

        // when
        List<MenuGroupResponse> findMenuGroups = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(findMenuGroups).hasSize(menuGroups.size()),
                () -> assertThat(findMenuGroups).containsExactly(MenuGroupResponse.from(중국집_1인_메뉴_세트))
        );
    }
}

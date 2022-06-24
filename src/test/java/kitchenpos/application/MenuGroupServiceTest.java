package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroupEntity;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.request.MenuGroupRequest;
import kitchenpos.menu.domain.response.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 서비스에 대한 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroupRequest 치킨_그룹;

    private MenuGroupEntity 치킨_그룹_entity;
    private MenuGroupEntity 피자_그룹_entity;

    @BeforeEach
    void setUp() {
        치킨_그룹 = new MenuGroupRequest("치킨 그룹");

        치킨_그룹_entity = MenuGroupEntity.of("치킨 그룹");
        피자_그룹_entity = MenuGroupEntity.of("피자 그룹");
    }

    @DisplayName("메뉴그룹을 등록하면 정상적으로 등록되어야 한다")
    @Test
    void create_test() {
        // given
        when(menuGroupRepository.save(any()))
            .thenReturn(치킨_그룹_entity);

        // when
        MenuGroupResponse result = menuGroupService.create(치킨_그룹);

        // then
        assertThat(result.getName()).isEqualTo(치킨_그룹.getName());
    }

    @DisplayName("메뉴그룹의 목록을 조회한다")
    @Test
    void findAll_test() {
        // given
        when(menuGroupRepository.findAll())
            .thenReturn(Arrays.asList(치킨_그룹_entity, 피자_그룹_entity));

        // when
        List<MenuGroupResponse> result = menuGroupService.list();

        // then
        assertThat(result).hasSize(2);
    }
}

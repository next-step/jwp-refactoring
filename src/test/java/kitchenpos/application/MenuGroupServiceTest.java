package kitchenpos.application;

import kitchenpos.domain.MenuGroupEntity;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.repository.MenuGroupRepository;
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
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void 메뉴_그룹_생성_성공() {
        // given
        MenuGroupEntity menuGroup = new MenuGroupEntity("양식");
        given(menuGroupRepository.save(any(MenuGroupEntity.class))).willReturn(menuGroup);

        // when
        MenuGroupResponse result = menuGroupService.create(new MenuGroupRequest("양식"));

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void 메뉴_그룹_목록_조회() {
        // given
        MenuGroupEntity 양식 = new MenuGroupEntity(1L, "양식");
        MenuGroupEntity 한식 = new MenuGroupEntity(2L, "한식");
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(양식, 한식));

        // when
        List<MenuGroupResponse> 메뉴_그룹_목록 = menuGroupService.list();

        // then
        assertThat(메뉴_그룹_목록).hasSize(2);
    }
}

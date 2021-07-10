package kitchenpos.menu.application;

import static kitchenpos.util.TestDataSet.추천_메뉴_그륩;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("그룹을 생성 정상 성공 케이스")
    void create() {
        //given
        given(menuGroupRepository.save(any())).willReturn(추천_메뉴_그륩);
        MenuGroupRequest request = new MenuGroupRequest("추천 메뉴");
        //when
        MenuGroupResponse result = menuGroupService.create(request);

        // then
        assertThat(result.getName()).isEqualTo(추천_메뉴_그륩.getName());
        verify(menuGroupRepository, times(1)).save(any());
    }

}

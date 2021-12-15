package kitchenpos.product.sample;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

import kitchenpos.common.domain.Name;
import kitchenpos.product.domain.MenuGroup;

public class MenuGroupSample {

    public static MenuGroup 두마리메뉴() {
        MenuGroup menuGroup = spy(MenuGroup.from(Name.from("두마리메뉴")));
        lenient().when(menuGroup.id())
            .thenReturn(1L);
        return menuGroup;
    }
}

package kitchenpos.application.menu;

import java.math.BigDecimal;
import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.application.util.dto.SaveMenuDto;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuTestFixture {

    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;

    public MenuTestFixture(MenuGroupRepository menuGroupRepository,
        MenuRepository menuRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Menu 메뉴_만들기(SaveMenuDto saveMenuDto) {
        List<MenuProduct> menuProducts = saveMenuDto.getMenuProducts();

        MenuGroup menuGroup = this.menuGroupRepository.save(saveMenuDto.getMenuGroup());

        Menu menu = new Menu(
            saveMenuDto.getMenuName(), BigDecimal.valueOf(saveMenuDto.getPrice()), menuGroup.getId(), menuProducts);

        return this.menuRepository.save(menu);
    }

}

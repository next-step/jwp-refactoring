package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.event.MenuCreatedEvent;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ApplicationEventPublisher eventPublisher;


    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository, ApplicationEventPublisher eventPublisher) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {

        MenuGroup menuGroup  = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                                                    .orElseThrow(() -> new IllegalArgumentException("메뉴그룹이 등록되어있지 않습니다."));

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);

        final List<MenuProductRequest> menuProducts = menuRequest.getMenuProducts();

        menuProducts.stream()
                    .map(menuProductRequest -> new MenuProduct(menu, new Product(menuProductRequest.getProductId()), menuProductRequest.getQuantity()))
                    .forEach(menu::addMenuProduct);

        eventPublisher.publishEvent(new MenuCreatedEvent(menu));
        return menuRepository.save(menu);
    }
    @Transactional(readOnly=true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}

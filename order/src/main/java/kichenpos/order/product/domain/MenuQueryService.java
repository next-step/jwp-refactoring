package kichenpos.order.product.domain;

import java.util.List;
import java.util.stream.Collectors;
import kichenpos.common.domain.Name;
import kichenpos.common.domain.Price;
import kichenpos.order.product.infrastructure.MenuClient;
import kichenpos.order.product.infrastructure.dto.MenuDto;
import org.springframework.stereotype.Service;

@Service
public class MenuQueryService {

    private final MenuClient menuClient;

    public MenuQueryService(MenuClient menuClient) {
        this.menuClient = menuClient;
    }

    public List<Menu> findAllById(List<Long> ids) {
        return menuClient.list(ids)
            .stream()
            .map(this::toMenu)
            .collect(Collectors.toList());
    }

    private Menu toMenu(MenuDto menuDto) {
        return Menu.of(
            menuDto.getId(), Name.from(menuDto.getName()), Price.from(menuDto.getPrice()));
    }
}

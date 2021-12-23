package kichenpos.order.product.infrastructure;

import java.util.List;
import kichenpos.order.product.infrastructure.dto.MenuDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "menu", url = "${kichenpos.product.url}", path = "/api/menus")
public interface MenuClient {

    @GetMapping
    List<MenuDto> list(@RequestParam(value = "ids") List<Long> ids);
}

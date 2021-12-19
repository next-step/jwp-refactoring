package kichenpos.order.table.client;

import kichenpos.order.table.model.OrderTable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "table", url = "${kichenpos.table.url}", path = "api")
public interface OrderTableClient {

    @GetMapping("/api/tables/{id}")
    OrderTable getTable(@PathVariable("id") long id);
}

package kitchenpos.orders.ordertable.ui;

import java.net.URI;
import kitchenpos.orders.ordertable.application.TableGroupService;
import kitchenpos.orders.ordertable.dto.TableGroupRequest;
import kitchenpos.orders.ordertable.dto.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableGroupController {

    private static final String BASE_PATH = "/api/table-groups/";

    private final TableGroupService tableGroupService;

    public TableGroupController(final TableGroupService tableGroupService) {
        this.tableGroupService = tableGroupService;
    }

    @PostMapping(BASE_PATH)
    public ResponseEntity<TableGroupResponse> group(
        @RequestBody final TableGroupRequest request) {
        final TableGroupResponse response = tableGroupService.group(request);
        final URI uri = URI.create(BASE_PATH + response.getId());
        return ResponseEntity.created(uri)
            .body(response);
    }

    @GetMapping(BASE_PATH)
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        tableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}

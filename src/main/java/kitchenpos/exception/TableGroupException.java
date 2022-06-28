package kitchenpos.exception;

import kitchenpos.dto.dto.ExceptionDTO;

public class TableGroupException extends CustomException{
    private static final String CLASSIFICATION = "TABLEGROUP";
    public TableGroupException(String message) {
        super(CLASSIFICATION, message);
    }
}

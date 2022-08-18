package base.core.faceit.util;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class SortUtil {
    private static final int SORT_DIRECTION_INDEX = 1;
    private static final int SORT_BY_INDEX = 0;
    private static final String DIRECTION_SPLITERATOR = ":";
    private static final String ORDER_SPLITERATOR = ";";

    public List<Sort.Order> getOrders(String sortBy) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sortBy.contains(DIRECTION_SPLITERATOR) || sortBy.contains(ORDER_SPLITERATOR)) {
            String[] sortingFields = sortBy.split(ORDER_SPLITERATOR);
            for (String field : sortingFields) {
                Sort.Order order;
                if (field.contains(DIRECTION_SPLITERATOR)) {
                    String[] fieldsAndDirections = field.split(DIRECTION_SPLITERATOR);
                    order = new Sort.Order(Sort.Direction.valueOf(
                            fieldsAndDirections[SORT_DIRECTION_INDEX]),
                            fieldsAndDirections[SORT_BY_INDEX]);
                } else {
                    order = new Sort.Order(Sort.Direction.DESC, field);
                }
                orders.add(order);
            }
        } else {
            Sort.Order order = new Sort.Order(Sort.Direction.DESC, sortBy);
            orders.add(order);
        }
        return orders;
    }
}

package base.core.faceit.service;

import base.core.faceit.model.Location;
import java.util.Optional;

public interface LocationService {
    Location save(Location location);

    Optional<Location> findByName(String name);
}

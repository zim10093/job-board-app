package base.core.faceit.service;

import base.core.faceit.model.Location;

public interface LocationService {
    Location save(Location location);

    Location findByName(String name);
}

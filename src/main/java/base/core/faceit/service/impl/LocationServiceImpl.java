package base.core.faceit.service.impl;

import base.core.faceit.model.Location;
import base.core.faceit.repository.LocationRepository;
import base.core.faceit.service.LocationService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public Location save(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Optional<Location> findByName(String name) {
        return locationRepository.findByName(name);
    }
}

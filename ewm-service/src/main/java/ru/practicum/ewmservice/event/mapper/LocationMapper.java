package ru.practicum.ewmservice.event.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.event.dto.LocationDto;
import ru.practicum.ewmservice.event.model.Location;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationMapper {
    public static Location locationDtoToLocation(LocationDto locationDto) {
        Location out = new Location();
        out.setLon(locationDto.getLon());
        out.setLat(locationDto.getLat());
        return out;
    }

    public static LocationDto locationToLocationDto(Location location) {
        return LocationDto.builder()
                .lon(location.getLon())
                .lat(location.getLat())
                .build();
    }
}
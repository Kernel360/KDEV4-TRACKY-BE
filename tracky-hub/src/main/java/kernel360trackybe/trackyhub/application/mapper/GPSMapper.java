package kernel360trackybe.trackyhub.application.mapper;

import kernel360trackybe.trackyhub.application.dto.CycleGpsRequest;
import kernel360trackybe.trackyhub.application.dto.CycleInfoRequest;
import kernel360trackybe.trackyhub.application.dto.GPSDto;
import org.springframework.stereotype.Component;

@Component
public class GPSMapper {
    
    public GPSDto toGPSDto(CycleInfoRequest carInfo, CycleGpsRequest cycleGpsRequest) {
        return GPSDto.builder()
                .mdn(carInfo.getMdn())
                .tid(carInfo.getTid())
                .mid(carInfo.getMid())
                .pv(carInfo.getPv())
                .did(carInfo.getDid())
                .oTime(carInfo.getOTime())
                .sec(cycleGpsRequest.getSec())
                .gcd(cycleGpsRequest.getGcd())
                .lat(cycleGpsRequest.getLat())
                .lon(cycleGpsRequest.getLon())
                .ang(cycleGpsRequest.getAng())
                .spd(cycleGpsRequest.getSpd())
                .build();
    }
}
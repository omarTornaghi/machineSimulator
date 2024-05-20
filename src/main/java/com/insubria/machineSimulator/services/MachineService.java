package com.insubria.machineSimulator.services;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class MachineService {
    private enum MachineCodes{
        SMOOTH_200, SMOOTH_250, VRX
    }
    private final Map<String,Integer>indexesMap = new HashMap<>();
    @Value("classpath:data/smooth_200/*")
    private Resource[] smooth200DataArray;
    @Value("classpath:data/Smooth_250/*")
    private Resource[] smooth250DataArray;
    @Value("classpath:data/VRX/*")
    private Resource[] vrxDataArray;
    private boolean checkIfAvailable(String machineId) {
        return Arrays.stream(MachineCodes.values()).anyMatch(x -> x.name().equals(machineId));
    }

    public String getData (String machineId) throws IOException {
        if(!checkIfAvailable(machineId)) throw new BadRequestException("Machine " + machineId + " not available");
        return getNextData(MachineCodes.valueOf(machineId));
    }

    private String getNextData (MachineCodes machineId) throws IOException {
        List<Resource>dataList = getDataList(machineId);
        dataList.sort(Comparator.comparing(Resource::getFilename));
        Integer integer = indexesMap.get(machineId.name());
        int currentIndex = integer != null ? integer : 0;
        String content = dataList.get(currentIndex)
                .getContentAsString(StandardCharsets.UTF_8);
        indexesMap.put(machineId.name(), currentIndex + 1);
        return content;
    }

    private List<Resource> getDataList (MachineCodes machineId) throws BadRequestException {
        switch (machineId){
            case SMOOTH_200 -> {
                return Arrays.asList(smooth200DataArray);
            }
            case SMOOTH_250 -> {
                return Arrays.asList(smooth250DataArray);
            }
            case VRX -> {
                return Arrays.asList(vrxDataArray);
            }
        }
        throw new BadRequestException("Resource list not found");
    }
}

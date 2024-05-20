package com.insubria.machineSimulator.controllers;

import com.insubria.machineSimulator.services.MachineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@Validated
@RequestMapping("/data")
@RequiredArgsConstructor
public class MachineController {
    private final MachineService machineService;
    @GetMapping("/{machineId}")
    public ResponseEntity<Object> getMachineDataByMachineId(@PathVariable String machineId) throws IOException {
        log.info("Requesting data for machine: " + machineId);
        return ResponseEntity.ok(machineService.getData(machineId.toUpperCase()));
    }
}

package de.lorenz.ticketsystem.controller;

import de.lorenz.ticketsystem.utils.ResponseWrapper;
import org.springframework.web.bind.annotation.GetMapping;

public abstract class ControllerDefaults {

    @GetMapping("/version")
    public ResponseWrapper<?> getVersion() {
        return ResponseWrapper.ok(getVersionString(), "");
    }

    protected abstract String getVersionString();
}
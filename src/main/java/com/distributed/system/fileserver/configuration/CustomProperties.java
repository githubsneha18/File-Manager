package com.distributed.system.fileserver.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomProperties {

    @Value("${base_directory}")
    private String baseDirectoryPath;

    public String getBaseDirectoryPath() {
        return baseDirectoryPath;
    }

    public void setBaseDirectoryPath(String baseDirectoryPath) {
        this.baseDirectoryPath = baseDirectoryPath;
    }
}

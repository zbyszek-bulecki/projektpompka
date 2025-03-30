package com.sharks.gardenManager.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.Map;

public record SettingsDTO (@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]X") Instant timestamp, Map<String, String> settings){}

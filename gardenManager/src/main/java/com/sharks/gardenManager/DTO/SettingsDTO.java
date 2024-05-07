package com.sharks.gardenManager.DTO;

import java.time.Instant;
import java.util.Map;

public record SettingsDTO (Instant timestamp, Map<String, String> settings){}

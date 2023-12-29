package com.sharks.gardenManager.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageDTO<T> {
    private int page;
    private long totalElements;
    private int size;
    private T content;

    public static <T> PageDTO<T> of(int page, long totalElements, int size, T content) {
        return new PageDTO<T>(page, totalElements, size, content);
    }
}

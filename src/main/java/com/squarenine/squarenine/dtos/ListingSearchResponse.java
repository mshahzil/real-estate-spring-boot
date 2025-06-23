package com.squarenine.squarenine.dtos;

import java.util.List;

import com.squarenine.squarenine.entity.ListingEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListingSearchResponse {
    private List<ListingEntity> listings;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private int currentPageElements;
    private int totalElements;
}

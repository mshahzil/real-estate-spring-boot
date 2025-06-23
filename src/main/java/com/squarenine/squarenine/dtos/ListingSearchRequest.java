package com.squarenine.squarenine.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListingSearchRequest {
    private String type;
    private String purpose;
    private long price_start;
    private long price_end;
    private String address;
    private float area_marla;
    private int beds;
    private int baths;
    private String sortByPriceOrder;
}

package com.squarenine.squarenine.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListingAlertPayload {
  private Long price;
  private int beds;
  private int baths;
  private Long area_marla;
  private String purpose;
  private String type;
  private String uid;
  private String address;
}

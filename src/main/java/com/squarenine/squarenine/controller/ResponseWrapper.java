package com.squarenine.squarenine.controller;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper {

  @JsonProperty(value = "status")
  private Boolean status;

  @JsonProperty(value = "data")
  private Object data;
  
  private HttpStatus httpStatus;

  private Integer totalPages;
  
  private Integer currentPage;
  
  private Long totalEntries;

  public static class ResponseWrapperBuilder {
    public ResponseWrapperBuilder data(Object data) {
      this.data = data != null ? data : new ArrayList<>();
      return this;
    }
  }  
}

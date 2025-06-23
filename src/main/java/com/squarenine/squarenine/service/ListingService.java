package com.squarenine.squarenine.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.squarenine.squarenine.dtos.ListingSearchRequest;
import com.squarenine.squarenine.dtos.ListingSearchResponse;
import com.squarenine.squarenine.entity.ListingEntity;

public interface ListingService {
    ListingEntity addListing(String listing, MultipartFile[] files) throws JsonProcessingException;

    List<ListingEntity> getListings(ListingSearchRequest request);

    Optional<ListingEntity> getListing(Long id);

    ListingSearchResponse getListingsWithPagination(int offset, int pageSize, ListingSearchRequest request);

    List<ListingEntity> getFeaturedListings();

    ListingSearchResponse getFeaturedListingsWithPagination(int pageNum, int pageSize);

    List<ListingEntity> getSimilarListings(Long id);

    String likeListing(JsonNode jsonNode);

    List<ListingEntity> getLikedListings(JsonNode jsonNode);

    String verifyListing(String data, MultipartFile[] files) throws JsonProcessingException;

    List<ListingEntity> getPostedListings(JsonNode jsonNode);

    String unlikeListing(JsonNode jsonNode);

    ListingEntity addListingV2(ListingEntity listing, MultipartFile[] files);

    ListingEntity addListingImagesV2(Long listingId, MultipartFile[] files);

    ListingEntity addListingJsonV2(ListingEntity listingEntity);
}

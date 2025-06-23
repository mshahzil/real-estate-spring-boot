package com.squarenine.squarenine.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.squarenine.squarenine.dtos.ListingSearchRequest;
import com.squarenine.squarenine.dtos.ListingSearchResponse;
import com.squarenine.squarenine.entity.ListingEntity;
import com.squarenine.squarenine.service.ListingService;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api")
public class ListingController {

    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @GetMapping("/listing/get/{id}")
    private Optional<ListingEntity> getListing(@PathVariable Long id){
        return listingService.getListing(id);
    }

    @GetMapping("/listing/get/all")
    private List<ListingEntity> getListings(@RequestBody(required = false) ListingSearchRequest request){
        return listingService.getListings(request);
    }

    @PostMapping("/listing/get/all")
    private List<ListingEntity> getListingsPost(@RequestBody(required = false) ListingSearchRequest request){
        return listingService.getListings(request);
    }

    @GetMapping("/listing/get/paged/{pageNum}/{pageSize}")
    private ListingSearchResponse getPagedListings(@PathVariable int pageNum, @PathVariable int pageSize, @RequestBody(required = false) ListingSearchRequest request){
       return listingService.getListingsWithPagination(pageNum, pageSize, request);
    }

    @PostMapping("/listing/get/paged/{pageNum}/{pageSize}")
    private ListingSearchResponse getPagedListingsPost(@PathVariable int pageNum, @PathVariable int pageSize, @RequestBody(required = false) ListingSearchRequest request){
        return listingService.getListingsWithPagination(pageNum, pageSize, request);
    }

    @GetMapping("/listing/get/featured/all")
    private List<ListingEntity> getFeaturedListings(){
        return listingService.getFeaturedListings();
    }

    @GetMapping("/listing/get/similar/{id}")
    private List<ListingEntity> getSimilarListings(@PathVariable Long id){
        return listingService.getSimilarListings(id);
    }

    @PostMapping(value = "/listing/add",consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE })
    private ListingEntity addListing(@RequestPart("json") String listing,
                                      @RequestPart("files") MultipartFile[] files) throws JsonProcessingException {
        return listingService.addListing(listing, files);
    }

    @PostMapping(value = "/listing/like")
    private String likeListing(@RequestBody JsonNode jsonNode){
        return listingService.likeListing(jsonNode);
    }

    @PostMapping(value = "/listing/unlike")
    private String unlikeListing(@RequestBody JsonNode jsonNode){
        return listingService.unlikeListing(jsonNode);
    }

    @PostMapping(value = "/listing/liked/allByUid")
    private List<ListingEntity> likedListing(@RequestBody JsonNode jsonNode){
        return listingService.getLikedListings(jsonNode);
    }

    @PostMapping(value = "/listing/posted/allByUid")
    private List<ListingEntity> postedListing(@RequestBody JsonNode jsonNode){
        return listingService.getPostedListings(jsonNode);
    }

    @PostMapping(value = "/listing/verify-request",consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE })
    private String verifyListing(@RequestPart("listingId") String id,
        @RequestPart("files") MultipartFile[] files) throws JsonProcessingException {
        return listingService.verifyListing(id, files);
    }

    @PostMapping(value = "/listing/addJson/v2")
    private ListingEntity addListingJsonV2(
        @RequestBody ListingEntity listingEntity) {
        return listingService.addListingJsonV2(listingEntity);
    }
    
    @PostMapping(value = "/listing/addImages/v2/{listingId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    private ListingEntity addListingImagesV2(
        @PathVariable Long listingId,
        @RequestPart("files") MultipartFile[] files) {
        return listingService.addListingImagesV2(listingId, files);
    }
}

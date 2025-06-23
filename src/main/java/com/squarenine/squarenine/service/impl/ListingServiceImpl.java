package com.squarenine.squarenine.service.impl;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingLong;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squarenine.squarenine.dtos.ListingSearchRequest;
import com.squarenine.squarenine.dtos.ListingSearchResponse;
import com.squarenine.squarenine.entity.ListingEntity;
import com.squarenine.squarenine.entity.UserEntity;
import com.squarenine.squarenine.repository.ListingRepository;
import com.squarenine.squarenine.repository.UserRepository;
import com.squarenine.squarenine.service.ListingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ListingServiceImpl implements ListingService {
    @Value("${application.bucket.name}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(ListingServiceImpl.class);

    @Override
    public Optional<ListingEntity> getListing(Long id) {
        logger.info("Get Listing with id {}",id);
        return listingRepository.findById(id);
    }

    @Override
    public List<ListingEntity> getListings(ListingSearchRequest request) {
        return getListingsFiltered(request);
    }

    @Override
    public ListingSearchResponse getListingsWithPagination(int pageNum, int pageSize, ListingSearchRequest request){
        List<ListingEntity> listings = getListingsFiltered(request);
        int totalElements = listings.size();
        if (pageSize == 0) return null;
        int totalPages = totalElements/pageSize;
        int lastPageElems = totalElements%pageSize;
        if (lastPageElems != 0) totalPages+=1;
        int currentPageElements = pageSize;
        if (pageNum>=totalPages) return null;
        int from = pageNum*pageSize;
        int upto;
        if (pageNum == totalPages-1){
            upto = from+lastPageElems;
            currentPageElements = lastPageElems;
        }
        else {
            upto = from+pageSize;
        }
        return new ListingSearchResponse(listings.subList(from,upto),pageNum,pageSize,totalPages,currentPageElements,totalElements);
    }

    @Override
    public List<ListingEntity> getFeaturedListings() {
        return listingRepository.findByFeatured();
    }

    @Override
    public ListingSearchResponse getFeaturedListingsWithPagination(int pageNum, int pageSize) {
        List<ListingEntity> listings = listingRepository.findByFeatured();
        int totalElements = listings.size();
        System.out.println(totalElements);
        if (pageSize == 0) return null;
        int totalPages = totalElements/pageSize;
        System.out.println(totalPages);
        int lastPageElems = totalElements%pageSize;
        System.out.println(lastPageElems);
        if (lastPageElems != 0) totalPages+=1;
        System.out.println(totalPages);
        int currentPageElements = pageSize;
        System.out.println(currentPageElements);
        if (pageNum>=totalPages) return null;
        int from = pageNum*pageSize;
        System.out.println(from);
        int upto;
        if (pageNum == totalPages-1){
            upto = from+lastPageElems;
            currentPageElements = lastPageElems;
        }
        else upto = from+pageSize;
        System.out.println(upto+" "+currentPageElements);
        return new ListingSearchResponse(listings.subList(from,upto),pageNum,pageSize,totalPages,currentPageElements,totalElements);

    }

    @Override
    public List<ListingEntity> getSimilarListings(Long id) {
        Optional<ListingEntity> listing = listingRepository.findById(id);
        return listing.map(listingEntity -> listingRepository.findByAreaLike(listingEntity.getArea_marla(),listingEntity.getType())).orElse(null);
    }

    @Override
    public String likeListing(JsonNode jsonNode) {
        String uid = jsonNode.get("uid").asText();
        String listingId = jsonNode.get("listingId").asText();
        UserEntity user = userRepository.findByUid(uid).orElseThrow(()-> new NotFoundException("user with this uid not found"));
        List<String> liked = new ArrayList<>();
        if (user.getLikedListings() != null && !user.getLikedListings().isEmpty()) {
            liked = new ArrayList<>(Arrays.stream(user.getLikedListings().split(",")).toList());
        }
        if (!liked.contains(listingId)) {
            liked.add(listingId);
        }
        user.setLikedListings(String.join(",",liked));
        userRepository.save(user);
        return "successfully liked.";
    }

    @Override
    public List<ListingEntity> getLikedListings(JsonNode jsonNode) {
        String listString = userRepository.findByUid(jsonNode.get("uid").asText()).orElseThrow(()->new NotFoundException("user not found with this id")).getLikedListings();
        if (listString != null && !listString.isEmpty()){
            List<Long> ids = Arrays.stream(listString.split(",")).map(Long::valueOf).collect(Collectors.toList());
            List<ListingEntity> listings = new ArrayList<>();
            for (Long id: ids) {
                if (listingRepository.findById(id).isPresent()) {
                    ListingEntity le = listingRepository.findById(id).get();
                    le.setLiked(true);
                    listings.add(le);
                }
            }
            return listings;
        }
        return null;
    }

    @Override
    public String verifyListing(String listingId, MultipartFile[] files) {
        System.out.println(Long.valueOf(listingId));
        ListingEntity listingEntity = listingRepository.findById(Long.valueOf(listingId)).orElseThrow(()->new NotFoundException("Listing Not Found with this id"));
        ArrayList<String> imageUrls = new ArrayList<>();
        for (MultipartFile file:files) {
            imageUrls.add(uploadFile(file,"listingDocs/"));
        }
        String imagesString = String.join(", ", imageUrls);
        listingEntity.setDocuments(imagesString);
        listingEntity.setVerification_status("PENDING");
        listingRepository.save(listingEntity);
        return "Verification request sent.";
    }

    @Override
    public List<ListingEntity> getPostedListings(JsonNode jsonNode) {
        return listingRepository.findByCb(userRepository.findByUid(jsonNode.get("uid").asText()).get().getId());
    }

    @Override
    public String unlikeListing(JsonNode jsonNode) {
        String uid = jsonNode.get("uid").asText();
        String listingId = jsonNode.get("listingId").asText();
        UserEntity user = userRepository.findByUid(uid).orElseThrow(()-> new NotFoundException("user with this uid not found"));
        List<String> liked = new ArrayList<>();
        if (user.getLikedListings() != null && !user.getLikedListings().isEmpty()) {
            liked = new ArrayList<>(Arrays.stream(user.getLikedListings().split(",")).toList());
        }
        liked.remove(listingId);
        user.setLikedListings(String.join(",",liked));
        userRepository.save(user);
        return "successfully unliked.";
    }

    @Override
    public ListingEntity addListingV2(ListingEntity listingEntity, MultipartFile[] files) {
        ArrayList<String> imageUrls = new ArrayList<>();
        for (MultipartFile file:files) {
            imageUrls.add(uploadFile(file,"listing/"));
            System.out.println(file);
        };
        String imagesString = String.join(", ", imageUrls);
        listingEntity.setImages(imagesString);
        if (listingEntity.getAddress() == null || listingEntity.getAddress().isEmpty()){
            listingEntity.setAddress(listingEntity.getSector()+", "+listingEntity.getSub_sector()+", "+listingEntity.getStreet());
        }
        listingEntity.setVerification_status("UNVERIFIED");
        listingEntity.setListed_date(LocalDateTime.now());
        System.out.println("cb:"+listingEntity.getCreatedBy());
        listingEntity.setCreatedById(userRepository.findByUid(listingEntity.getCreatedBy()).get().getId());
        return listingRepository.save(listingEntity);
    }

    @Override
    public ListingEntity addListingImagesV2(Long listingId, MultipartFile[] files) {
        ListingEntity listingEntity = listingRepository.findById(listingId).get();
        ArrayList<String> imageUrls = new ArrayList<>();
        for (MultipartFile file:files) {
            imageUrls.add(uploadFile(file,"listing/"));
            System.out.println(file);
        };
        String imagesString = String.join(", ", imageUrls);
        listingEntity.setImages(imagesString);
        listingRepository.save(listingEntity);
        return listingEntity;
    }

    @Override
    public ListingEntity addListingJsonV2(ListingEntity listingEntity) {
        if (listingEntity.getAddress() == null || listingEntity.getAddress().isEmpty()){
            listingEntity.setAddress(listingEntity.getSector()+", "+listingEntity.getSub_sector()+", "+listingEntity.getStreet());
        }
        listingEntity.setVisible(1);
        listingEntity.setVerification_status("UNVERIFIED");
        listingEntity.setListed_date(LocalDateTime.now());
        System.out.println("cb:"+listingEntity.getCreatedBy());
        listingEntity.setCreatedById(userRepository.findByUid(listingEntity.getCreatedBy()).get().getId());
        return listingRepository.save(listingEntity);
    }

    private List<ListingEntity> getListingsFiltered(ListingSearchRequest request) {
        List<ListingEntity> matching = new ArrayList<>(listingRepository.findAll());
        if (request != null){
            if (request.getAddress() != null && !request.getAddress().isEmpty()) {
                matching.clear();
                matching = listingRepository.findByAddressLike(request.getAddress());
            }
            int beds = request.getBeds();
            if (beds != 0) {
                matching = matching.stream()
                        .filter(p -> (beds<6?(p.getBeds() == beds):(p.getBeds() >= 6))).collect(Collectors.toList());
            }
            int baths = request.getBaths();
            if (baths != 0) {
                matching = matching.stream()
                        .filter(p -> (baths<6?(p.getBaths() == baths):(p.getBaths() >= 6))).collect(Collectors.toList());
            }if (request.getArea_marla() != 0) {
                matching = matching.stream()
                        .filter(p -> (p.getArea_marla() == request.getArea_marla())).collect(Collectors.toList());
            }if (request.getPrice_start() != 0) {
                matching = matching.stream()
                        .filter(p -> (p.getPrice() >= request.getPrice_start())).collect(Collectors.toList());
            }if (request.getPrice_end() != 0) {
                matching = matching.stream()
                        .filter(p -> (p.getPrice() <= request.getPrice_end())).collect(Collectors.toList());
            }if (request.getType() != null && !request.getType().isEmpty()) {
                matching = matching.stream()
                        .filter(p -> (p.getType().equals(request.getType()))).collect(Collectors.toList());
            }if (request.getPurpose() != null && !request.getPurpose().isEmpty()) {
                matching = matching.stream()
                    .filter(p -> (p.getPurpose().equals(request.getPurpose()))).collect(Collectors.toList());
            }
        }
        if (request != null && request.getSortByPriceOrder() != null && !request.getSortByPriceOrder().isEmpty()
            && request.getSortByPriceOrder().equals("DESC")) {
            matching.sort(Comparator.nullsLast(comparingLong(ListingEntity::getPrice)).reversed());
        } else if (request != null && request.getSortByPriceOrder() != null && !request.getSortByPriceOrder().isEmpty()
            && request.getSortByPriceOrder().equals("ASC")) {
            matching.sort(Comparator.nullsLast(comparingLong(ListingEntity::getPrice)));
        } else {
            matching.sort(
                Comparator.nullsLast(comparing(ListingEntity::getListed_date, nullsLast(naturalOrder()))).reversed());
        }
        return matching;
    }

    @Override
    public ListingEntity addListing(String listing_str, MultipartFile[] files) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ListingEntity listingEntity = mapper.readValue(listing_str, ListingEntity.class);
        ArrayList<String> imageUrls = new ArrayList<>();
        for (MultipartFile file:files) {
            imageUrls.add(uploadFile(file,"listing/"));
            System.out.println(file);
        };
        String imagesString = String.join(", ", imageUrls);
        listingEntity.setImages(imagesString);
        if (listingEntity.getAddress() == null || listingEntity.getAddress().isEmpty()){
            listingEntity.setAddress(listingEntity.getSector()+", "+listingEntity.getSub_sector()+", "+listingEntity.getStreet());
        }
        listingEntity.setVisible(1);
        listingEntity.setVerification_status("UNVERIFIED");
        listingEntity.setListed_date(LocalDateTime.now());
        System.out.println("cb:"+listingEntity.getCreatedBy());
        listingEntity.setCreatedById(userRepository.findByUid(listingEntity.getCreatedBy()).get().getId());
        return listingRepository.save(listingEntity);
    }


    public String uploadFile(MultipartFile file, String path) {

        File fileObj = convertMultiPartFileToFile(file);
        String fileName = path+System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();
        return s3Client.getUrl(bucketName, fileName).toString();
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }

}

package org.example.be_java_hisp_w26_g04.service.seller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.be_java_hisp_w26_g04.dto.FollowersCountDTO;
import org.example.be_java_hisp_w26_g04.exceptions.NotFoundException;
import org.example.be_java_hisp_w26_g04.model.Seller;
import org.example.be_java_hisp_w26_g04.repository.seller.ISellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerService implements ISellerService {

    @Autowired
    ISellerRepository sellerRepository;

    private final ObjectMapper objectMapper;

    @Override
    public FollowersCountDTO findFollowers(int sellerId) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(NotFoundException::new);
        int followerCount = seller.getListFollowers().size();

        FollowersCountDTO followersCountDTO = objectMapper.convertValue(seller, FollowersCountDTO.class);
        followersCountDTO.setFollowersCount(followerCount);

        return followersCountDTO;
    }
}

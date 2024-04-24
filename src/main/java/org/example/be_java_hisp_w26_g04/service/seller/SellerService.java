package org.example.be_java_hisp_w26_g04.service.seller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.example.be_java_hisp_w26_g04.dto.FollowersCountDTO;
import org.example.be_java_hisp_w26_g04.dto.SellerFollowersDTO;
import org.example.be_java_hisp_w26_g04.dto.UserDto;
import org.example.be_java_hisp_w26_g04.exceptions.BadRequestException;
import org.example.be_java_hisp_w26_g04.exceptions.NotFoundException;
import org.example.be_java_hisp_w26_g04.model.Post;
import org.example.be_java_hisp_w26_g04.model.Seller;
import org.example.be_java_hisp_w26_g04.repository.buyer.IBuyersRepository;
import org.example.be_java_hisp_w26_g04.repository.seller.ISellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SellerService implements ISellerService {

  @Autowired
  ISellerRepository sellerRepository;

  @Autowired
  IBuyersRepository buyerRepository;

  private final ObjectMapper objectMapper;

  @Override
  public FollowersCountDTO findFollowers(int sellerId) {
    Seller seller = sellerRepository.findById(sellerId).orElseThrow(NotFoundException::new);
    int followerCount = seller.getFollowers().size();

    FollowersCountDTO followersCountDTO = objectMapper.convertValue(seller, FollowersCountDTO.class);
    followersCountDTO.setFollowersCount(followerCount);

    return followersCountDTO;
  }

  @Override
  public SellerFollowersDTO getFollowers(int userId) {
    Seller seller = sellerRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("Seller with id " + userId + " not found"));

    return converSellerToSellerFollowersDto(seller);
  }

  private SellerFollowersDTO converSellerToSellerFollowersDto(Seller seller) {
    List<UserDto> followers = seller.getFollowers().stream()
            .map(x -> buyerRepository.findById(x)).filter(Optional::isPresent).map(x -> x.get())
        .map(follower -> new UserDto(follower.getUserId(), follower.getUserName()))
        .toList();

    return new SellerFollowersDTO(seller.getUserId(), seller.getUserName(), followers);
  }
  @Override
    public boolean createNewPost(Post post){
        Optional<Seller> optionalSeller = sellerRepository.findById(post.getUserId());
        if (optionalSeller.isEmpty()){
            throw new BadRequestException();
        }
        return sellerRepository.save(post);
    }
}

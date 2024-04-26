package org.example.be_java_hisp_w26_g04.service.buyer;

import org.example.be_java_hisp_w26_g04.dto.BuyerDTO;
import org.example.be_java_hisp_w26_g04.dto.UserDTO;
import org.example.be_java_hisp_w26_g04.exceptions.BadRequestException;
import org.example.be_java_hisp_w26_g04.exceptions.NotFoundException;
import org.example.be_java_hisp_w26_g04.model.Buyer;
import org.example.be_java_hisp_w26_g04.model.Seller;
import org.example.be_java_hisp_w26_g04.repository.buyer.IBuyersRepository;
import org.example.be_java_hisp_w26_g04.repository.seller.ISellerRepository;
import org.example.be_java_hisp_w26_g04.util.crud.exceptionsHandler.ObjectExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BuyerService implements IBuyerService {

    @Autowired
    IBuyersRepository buyersRepository;
    @Autowired
    ISellerRepository sellerRepository;

    @Override
    public void followSeller(int buyerId, int sellerId) {
        Buyer buyer= ObjectExist.getObjectFromOptional(buyersRepository.findById(buyerId));
        Seller seller= ObjectExist.getObjectFromOptional(sellerRepository.findById(sellerId));

        if (!buyer.addFollow(seller) || !seller.addFollower(buyer)) {
            throw new BadRequestException();
        }
    }

    @Override
    public BuyerDTO getFollowed(int id) {
        Buyer buyer= ObjectExist.getObjectFromOptional(buyersRepository.findById(id));
        List<Seller> sellerList = buyer.getSellersFollowing().stream()
        .map(x -> sellerRepository.findById(x)).filter(Optional::isPresent)
                .map(Optional::get).toList();
        List<UserDTO> userDtoList= new ArrayList<>();
        for(Seller seller: sellerList){
            userDtoList.add(new UserDTO(seller.getUserId(), seller.getUserName() ));
        }

        return new BuyerDTO(buyer.getUserId(), buyer.getUserName(), userDtoList);
    }

    @Override
    public void unfollowerSeller(int userId, int userIdToUnfollow) {
        Buyer buyer= ObjectExist.getObjectFromOptional(buyersRepository.findById(userId));
        Seller seller = ObjectExist.getObjectFromOptional(sellerRepository.findById(userIdToUnfollow));

        buyer.getSellersFollowing().remove(seller.getUserId());
        seller.getFollowers().remove(buyer.getUserId());
    }

    @Override
    public BuyerDTO sortGetFollowed(int userId, String order) {
        BuyerDTO buyerDTO= getFollowed(userId);
        if(order.equals("name_asc")){
            buyerDTO.getFollowed().sort(Comparator.comparing(UserDTO::getUsername));
        } else if (order.equals("name_desc")) {
            buyerDTO.getFollowed().sort(Comparator.comparing(UserDTO::getUsername).reversed());
        }
        else{
            throw new BadRequestException();
        }
        return buyerDTO;
    }
}

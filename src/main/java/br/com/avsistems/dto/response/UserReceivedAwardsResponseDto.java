package br.com.avsistems.dto.response;

import br.com.avsistems.entity.UserReceivedAwardsEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserReceivedAwardsResponseDto(
        UUID id,
        UUID userId,
        String userName,
        UUID receivedAwardId,
        String receivedAwardName,
        String receivedAwardImageUrl,
        LocalDateTime awardedDate
) {
    public UserReceivedAwardsResponseDto(UserReceivedAwardsEntity userReceivedAward) {
        this(
                userReceivedAward.id,
                userReceivedAward.user.id,
                userReceivedAward.user.name,
                userReceivedAward.receivedAward.id,
                userReceivedAward.receivedAward.name,
                userReceivedAward.receivedAward.imageUrl,
                userReceivedAward.awardedDate
        );
    }
}

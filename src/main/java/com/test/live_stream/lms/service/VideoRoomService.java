package com.test.live_stream.lms.service;

import io.livekit.server.AccessToken;
import io.livekit.server.RoomJoin;
import io.livekit.server.RoomName;
import io.livekit.server.CanPublish;
import io.livekit.server.CanSubscribe;
import io.livekit.server.CanPublishData;
import io.livekit.server.RoomAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VideoRoomService {

    @Value("${livekit.api.key}")
    private String apiKey;

    @Value("${livekit.api.secret}")
    private String apiSecret;

    /**
     * Generate a LiveKit join token with role-based grants.
     * 
     * @param participantIdentity unique user ID
     * @param participantName     display name
     * @param roomName            the LiveKit room to join
     * @param isModerator         if true, gets admin grants (mute others, kick,
     *                            etc.)
     */
    public String generateJoinToken(String participantIdentity, String participantName,
            String roomName, boolean isModerator) {
        AccessToken token = new AccessToken(apiKey, apiSecret);
        token.setIdentity(participantIdentity);
        token.setName(participantName);

        // Everyone can join, publish, subscribe, and publish data
        token.addGrants(
                new RoomJoin(true),
                new RoomName(roomName),
                new CanPublish(true),
                new CanSubscribe(true),
                new CanPublishData(true));

        // Moderators (session creator) get admin privileges
        if (isModerator) {
            token.addGrants(new RoomAdmin(true));
        }

        return token.toJwt();
    }
}

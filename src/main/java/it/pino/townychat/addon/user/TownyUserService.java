package it.pino.townychat.addon.user;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import it.pino.townychat.addon.TownyChat;
import it.pino.townychat.addon.chat.channel.TownyChannelType;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class TownyUserService {

    private final @NotNull Map<UUID, TownyChannelType> usersChannelsMap = new ConcurrentHashMap<>();

    @NotNull
    private final TownyChat mainInstance;

    public TownyUserService(final @NotNull TownyChat mainInstance) {
        this.mainInstance = mainInstance;
    }

    public Optional<TownyChannelType> getUserChannel(final @NotNull UUID userID){
        return Optional.ofNullable(usersChannelsMap.get(userID));
    }

    public void setUserChannel(final @NotNull UUID userID, final @NotNull TownyChannelType channelType){
        usersChannelsMap.put(userID, channelType);
    }

    public void removeUserChannel(final @NotNull UUID userID){
        usersChannelsMap.remove(userID);
    }

    public Optional<Town> getTown(@NotNull Player ownerPlayer){
        final var town = mainInstance.getTownyAPI().getTown(ownerPlayer);
        return Optional.ofNullable(town);
    }

    public Optional<Nation> getNation(@NotNull Player ownerPlayer){
        final var nation = mainInstance.getTownyAPI().getNation(ownerPlayer);
        return Optional.ofNullable(nation);
    }

    @NotNull
    public Collection<Audience> getTownOnlineResidentsAudience(@NotNull Town town) {
        final Set<Audience> onlineResidents = new HashSet<>();
        for (final var resident : town.getResidents()) {
            if (resident.isOnline()) {
                onlineResidents.add(resident.audience());
            }
        }
        return onlineResidents;
    }

    @NotNull
    public Collection<Audience> getNationOnlineResidentsAudience(@NotNull Nation nation) {
        final Set<Audience> onlineResidents = new HashSet<>();
        for (final var resident : nation.getResidents()) {
            if (resident.isOnline()) {
                onlineResidents.add(resident.audience());
            }
        }
        return onlineResidents;
    }
    @NotNull
    public Collection<Audience> getAlliesOnlineResidentsAudience(@NotNull Town town){
        final Set<Audience> onlineResidents = new HashSet<>();
        for(final var allyTown : town.getAllies()){
            for(final var resident : allyTown.getResidents()){
                if(resident.isOnline()){
                    onlineResidents.add(resident.audience());
                }
            }
        }
        return onlineResidents;
    }

}

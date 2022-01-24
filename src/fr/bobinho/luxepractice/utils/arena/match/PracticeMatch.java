package fr.bobinho.luxepractice.utils.arena.match;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import net.md_5.bungee.api.chat.BaseComponent;

import javax.annotation.Nonnull;
import java.util.List;

public interface PracticeMatch {

    public BaseComponent[] getStartMessage(@Nonnull PracticePlayer receiver);

    public BaseComponent[] getEndMessage(@Nonnull PracticePlayer receiver);

    public List<PracticePlayer> getALlMembers();

}

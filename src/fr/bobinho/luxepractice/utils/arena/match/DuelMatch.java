package fr.bobinho.luxepractice.utils.arena.match;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

public class DuelMatch implements PracticeMatch {

    @Override
    public BaseComponent[] getStartMessage(@NotNull PracticePlayer receiver) {
        return new BaseComponent[0];
    }

    @Override
    public BaseComponent[] getEndMessage(@NotNull PracticePlayer receiver) {
        return new BaseComponent[0];
    }

}

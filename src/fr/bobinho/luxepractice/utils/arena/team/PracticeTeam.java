package fr.bobinho.luxepractice.utils.arena.team;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.lang.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PracticeTeam {

    /**
     * Fields
     */
    private final PracticePlayer leader;
    private final List<PracticePlayer> members;
    private String name;
    private ChatColor color;

    /**
     * Creates a new practice team
     *
     * @param leader the practice team leader
     */
    public PracticeTeam(@Nonnull PracticePlayer leader) {
        Validate.notNull(leader, "leader is null");

        this.leader = leader;
        this.members = new ArrayList<>(List.of(leader));
    }

    /**
     * Gets the practice team leader
     *
     * @return the practice team leader
     */
    @Nonnull
    public PracticePlayer getLeader() {
        return leader;
    }

    /**
     * Gets the practice team members
     *
     * @return the practice team memners
     */
    @Nonnull
    public List<PracticePlayer> getMembers() {
        return members;
    }

    /**
     * Adds a new practice team member
     *
     * @param practicePlayer the new practice team member
     */
    public void addMember(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");

        getMembers().add(practicePlayer);
    }

    /**
     * Remove the practice player from the team
     *
     * @param practicePlayer the practice team player removed from the team
     */
    public void removeMember(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");

        getMembers().remove(practicePlayer);
    }

    /**
     * Gets the practice team members name list as string
     *
     * @return the practice team members name list as string
     */
    @Nonnull
    public String getMembersAsString() {
        return getMembers().stream().map(PracticePlayer::getName).collect(Collectors.joining(", "));
    }

    /**
     * Gets the clickable text to open the practice team members inventory
     *
     * @return the clickable text
     */
    @Nonnull
    public BaseComponent[] getMembersClickableInventoryAccessAsString() {
        ComponentBuilder builder = new ComponentBuilder();

        //Loops all practice team members
        for (PracticePlayer practiceMember : getMembers()) {

            //Gets practice team members clickable inventory access
            builder.append(practiceMember.getClickableInventoryAccessAsString()).color(getColor())
                    .append(", ").color(ChatColor.GRAY);
        }

        //Removes the last ","
        builder.removeComponent(builder.getCursor());
        return builder.create();
    }

    /**
     * Gets the practice team name
     *
     * @return the practice team name
     */
    @Nullable
    public String getName() {
        return name;
    }

    /**
     * Sets the practice team name
     *
     * @param name the practice team name
     */
    public void setName(@Nonnull String name) {
        Validate.notNull(name, "name is null");

        this.name = name;
    }

    /**
     * Sets the practice team color
     *
     * @return the practice team color
     */
    @Nullable
    public ChatColor getColor() {
        return color;
    }

    /**
     * Sets the practice team color
     *
     * @param color the practice team color
     */
    public void setColor(@Nonnull ChatColor color) {
        Validate.notNull(color, "color is null");

        this.color = color;
    }

}
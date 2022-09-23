package fr.bobinho.luxepractice.utils.arena.request;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PracticeRequest {

    /**
     * Fields
     */
    private final PracticePlayer practiceSender;
    private final PracticePlayer practiceReceiver;

    /**
     * Creates a new practice request
     * @param practiceSender the practice sender
     * @param practiceReceiver the practice receiver
     */
    public PracticeRequest(@Nonnull PracticePlayer practiceSender, @Nonnull PracticePlayer practiceReceiver) {
        Objects.requireNonNull(practiceSender, "practiceSender is null");
        Objects.requireNonNull(practiceReceiver, "practiceReceiver is null");

        this.practiceSender = practiceSender;
        this.practiceReceiver = practiceReceiver;
    }

    /**
     * Gets the request sender
     *
     * @return the sender
     */
    public PracticePlayer getPracticeSender() {
        return practiceSender;
    }

    /**
     * Gets the request receiver
     *
     * @return the receiver
     */
    public PracticePlayer getPracticeReceiver() {
        return practiceReceiver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PracticeRequest)) {
            return false;
        }
        PracticeRequest testedPracticeRequest = (PracticeRequest) o;
        return testedPracticeRequest.getPracticeSender().equals(getPracticeSender()) && testedPracticeRequest.getPracticeReceiver().equals(getPracticeReceiver());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(getPracticeSender(), getPracticeSender());
    }

}

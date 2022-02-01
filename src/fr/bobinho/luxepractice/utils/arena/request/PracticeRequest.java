package fr.bobinho.luxepractice.utils.arena.request;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import org.atlanmod.commons.Guards;
import javax.annotation.Nonnull;
import java.util.Objects;

public class PracticeRequest {

    private final PracticePlayer practiceSender;
    private final PracticePlayer practiceReceiver;

    public PracticeRequest(@Nonnull PracticePlayer practiceSender, @Nonnull PracticePlayer practiceReceiver) {
        Guards.checkNotNull(practiceSender, "practiceSender is null");
        Guards.checkNotNull(practiceReceiver, "practiceReceiver is null");

        this.practiceSender = practiceSender;
        this.practiceReceiver = practiceReceiver;
    }

    /**
     * Gets the request sender
     * @return the sender
     */
    public PracticePlayer getPracticeSender() {
        return practiceSender;
    }

    /**
     * Gets the request receiver
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
        if (!(o instanceof  PracticeRequest)) {
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

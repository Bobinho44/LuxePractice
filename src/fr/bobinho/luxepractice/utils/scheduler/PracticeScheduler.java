package fr.bobinho.luxepractice.utils.scheduler;

import fr.bobinho.luxepractice.LuxePracticeCore;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PracticeScheduler {

    /**
     * Scheduler types.
     */
    public enum Type {
        SYNC,
        ASYNC;
    }

    private final Type type;

    private int delay;
    private TimeUnit delayType;

    private int repeatingDelay;
    private TimeUnit repeatingDelayType;

    private Runnable cachedRunnable;

    private int bukkitTaskId = -1;

    private boolean shouldWait = false;

    /**
     * Creates scheduler.
     *
     * @param type Type.
     */
    public PracticeScheduler(@Nonnull Type type) {
        //Objects null control
        Objects.requireNonNull(type, "type cannot be null!");
        //Root construction
        this.type = type;
    }

    /**
     * Creates sync scheduler builder.
     *
     * @return Appa sync scheduler builder.
     */
    @Nonnull
    public static PracticeScheduler syncScheduler() {
        return new PracticeScheduler(PracticeScheduler.Type.SYNC);
    }

    /**
     * Creates async scheduler builder.
     *
     * @return Appa async scheduler builder.
     */
    @Nonnull
    public static PracticeScheduler asyncScheduler() {
        return new PracticeScheduler(PracticeScheduler.Type.ASYNC);
    }

    /**
     * Runs scheduler after declared time.
     *
     * @param delay Scheduler after delay.
     * @return Appa Scheduler builder.
     */
    @Nonnull
    public PracticeScheduler after(int delay) {
        this.delay = delay * 50;
        this.delayType = TimeUnit.MILLISECONDS;
        return this;
    }

    /**
     * Runs scheduler after declared time.
     *
     * @param delay     Scheduler after delay.
     * @param delayType Scheduler after delay type.
     * @return Appa Scheduler builder.
     */
    @Nonnull
    public PracticeScheduler after(int delay, @Nonnull TimeUnit delayType) {
        Objects.requireNonNull(delayType, "delay type cannot be null!");
        this.delay = delay;
        this.delayType = delayType;
        return this;
    }

    /**
     * Runs scheduler every declared time.
     *
     * @param repeatingDelay Scheduler repeating time.
     * @return Appa Scheduler builder.
     */
    @Nonnull
    public PracticeScheduler every(int repeatingDelay) {
        this.repeatingDelay = repeatingDelay * 50;
        this.repeatingDelayType = TimeUnit.MILLISECONDS;
        return this;
    }

    /**
     * Runs scheduler every declared time.
     *
     * @param repeatingDelay     Scheduler repeating time.
     * @param repeatingDelayType Scheduler repeating time type.
     * @return Appa Scheduler builder.
     */
    @Nonnull
    public PracticeScheduler every(int repeatingDelay, @Nonnull TimeUnit repeatingDelayType) {
        Objects.requireNonNull(repeatingDelayType, "repeating delay type cannot be null!");
        this.repeatingDelay = repeatingDelay;
        this.repeatingDelayType = repeatingDelayType;
        return this;
    }

    /**
     * Gets cached runnable.
     *
     * @return Runnable.
     */
    public Runnable getCachedRunnable() {
        return cachedRunnable;
    }

    /**
     * Sets cached runnable
     *
     * @param cachedRunnable Runnable.
     * @return Appa scheduler builder.
     */
    @Nonnull
    public PracticeScheduler setCachedRunnable(@Nonnull Runnable cachedRunnable) {
        Objects.requireNonNull(cachedRunnable, "cached runnable cannot be null!");
        this.cachedRunnable = cachedRunnable;
        return this;
    }

    /**
     * Waits task to complete.
     *
     * @return Appa scheduler builder.
     */
    @Nonnull
    public PracticeScheduler block() {
        this.shouldWait = true;
        return this;
    }

    /**
     * Runs cached Appa Scheduler.
     *
     * @return Bukkit task id.
     */
    public int runCached() {
        return this.run(this.cachedRunnable);
    }

    /**
     * If there is an ongoing task, it will stop it.
     */
    public void stop() {
        if (this.bukkitTaskId == -1)
            return;
        Bukkit.getScheduler().cancelTask(this.bukkitTaskId);
    }

    /**
     * Runs configured Appa Scheduler.
     *
     * @param runnable Runnable.
     * @return Bukkit task id.
     */
    public synchronized int run(@Nonnull Runnable runnable) throws IllegalArgumentException {
        //Objects null check.
        Objects.requireNonNull(runnable, "runnable cannot be null!");

        long delay = this.delayType == null ? 0 : Math.max(this.delayType.toMillis(this.delay) / 50, 0);
        long repeating_delay = this.repeatingDelayType == null ? 0 : Math.max(this.repeatingDelayType.toMillis(this.repeatingDelay) / 50, 0);

        if (this.type == Type.SYNC) {
            if (repeating_delay != 0)
                bukkitTaskId = Bukkit.getScheduler().runTaskTimer(LuxePracticeCore.getInstance(), runnable, delay, repeating_delay).getTaskId();
            else if (delay != 0)
                bukkitTaskId = Bukkit.getScheduler().runTaskLater(LuxePracticeCore.getInstance(), runnable, delay).getTaskId();
            else
                bukkitTaskId = Bukkit.getScheduler().runTask(LuxePracticeCore.getInstance(), runnable).getTaskId();
        } else {
            if (repeating_delay != 0)
                bukkitTaskId = Bukkit.getScheduler().runTaskTimerAsynchronously(LuxePracticeCore.getInstance(), runnable, delay, repeating_delay).getTaskId();
            else if (delay != 0)
                bukkitTaskId = Bukkit.getScheduler().runTaskLaterAsynchronously(LuxePracticeCore.getInstance(), runnable, delay).getTaskId();
            else
                bukkitTaskId = Bukkit.getScheduler().runTaskAsynchronously(LuxePracticeCore.getInstance(), runnable).getTaskId();
        }

        //Waits task to complete.
        if (this.shouldWait) {
            while (true) {
                if (Bukkit.getScheduler().isCurrentlyRunning(bukkitTaskId) || Bukkit.getScheduler().isQueued(bukkitTaskId))
                    continue;
                break;
            }
        }

        return bukkitTaskId;
    }

    /**
     * Runs configured Appa Scheduler.
     *
     * @param task Bukkit task.
     */
    public synchronized void run(@Nonnull Consumer<BukkitTask> task) throws IllegalArgumentException {
        //Objects null check.
        Objects.requireNonNull(task, "task cannot be null!");

        long delay = this.delayType == null ? 0 : Math.max(this.delayType.toMillis(this.delay) / 50, 0);
        long repeating_delay = this.repeatingDelayType == null ? 0 : Math.max(this.repeatingDelayType.toMillis(this.repeatingDelay) / 50, 0);

        if (this.type == Type.SYNC) {
            if (repeating_delay != 0)
                Bukkit.getScheduler().runTaskTimer(LuxePracticeCore.getInstance(), task, delay, repeating_delay);
            else if (delay != 0)
                Bukkit.getScheduler().runTaskLater(LuxePracticeCore.getInstance(), task, delay);
            else
                Bukkit.getScheduler().runTask(LuxePracticeCore.getInstance(), task);
        } else {
            if (repeating_delay != 0)
                Bukkit.getScheduler().runTaskTimerAsynchronously(LuxePracticeCore.getInstance(), task, delay, repeating_delay);
            else if (delay != 0)
                Bukkit.getScheduler().runTaskLaterAsynchronously(LuxePracticeCore.getInstance(), task, delay);
            else
                Bukkit.getScheduler().runTaskAsynchronously(LuxePracticeCore.getInstance(), task);
        }
    }
}

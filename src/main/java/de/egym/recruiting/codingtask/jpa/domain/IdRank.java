package de.egym.recruiting.codingtask.jpa.domain;

import javax.annotation.Nonnull;

/**
 * Class for storing id-rank pair and being able to sort based on higher rank first or lower id first
 * in case ranks are the same
 */
public class IdRank implements Comparable<IdRank> {
    public final long id;
    public final long rank;

    public IdRank(long id, long rank) {
        this.id = id;
        this.rank = rank;
    }

    @Override
    public int compareTo(@Nonnull IdRank o) {
        return rank != o.rank ? Long.compare(o.rank, rank) : Long.compare(id, o.id);
    }
}

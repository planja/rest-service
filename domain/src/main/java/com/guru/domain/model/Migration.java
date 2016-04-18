package com.guru.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Никита on 18.04.2016.
 */

@Entity
@Table(name = "migrations")
public class Migration {

    @Column(name = "migration")
    private String migration;

    @Column(name = "batch")
    private Long batch;

    public Migration() {
    }

    public Migration(String migration, Long batch) {
        this.migration = migration;
        this.batch = batch;
    }

    public String getMigration() {
        return migration;
    }

    public void setMigration(String migration) {
        this.migration = migration;
    }

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Migration migration1 = (Migration) o;

        if (migration != null ? !migration.equals(migration1.migration) : migration1.migration != null) return false;
        return !(batch != null ? !batch.equals(migration1.batch) : migration1.batch != null);

    }

    @Override
    public int hashCode() {
        int result = migration != null ? migration.hashCode() : 0;
        result = 31 * result + (batch != null ? batch.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Migration{" +
                "migration='" + migration + '\'' +
                ", batch=" + batch +
                '}';
    }
}
